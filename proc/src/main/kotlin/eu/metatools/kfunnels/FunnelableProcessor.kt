package eu.metatools.kfunnels

import com.google.auto.common.BasicAnnotationProcessor
import eu.metatools.kfunnels.utils.*
import java.io.Writer
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.tools.*
import kotlin.reflect.jvm.internal.impl.serialization.Flags
import kotlin.reflect.jvm.internal.impl.serialization.ProtoBuf
import kotlin.reflect.jvm.internal.impl.serialization.jvm.JvmProtoBufUtil

/**
 * Annotates elements that should have a funneler generated.
 */
annotation class Funnelable

/**
 * Abstraction of qualified names.
 */
private data class Name(val packageName: String?, val className: String) {
    val joined get() = if (packageName == null) className else packageName + "." + className
}

/**
 * Maps the package of the [Name].
 */
private inline fun Name.mapPackage(block: (String?) -> String?) = Name(block(packageName), className)

/**
 * Maps the class of the [Name].
 */
private inline fun Name.mapClass(block: (String) -> String) = Name(packageName, block(className))

/**
 * Computes the Kotlin package declaration.
 */
private val Name.kotlinPackage get() = if (packageName == null) "" else "package $packageName"

/**
 * Computes the Java package declaration.
 */
private val Name.javaPackage get() = if (packageName == null) "" else "package $packageName;"

@SupportedAnnotationTypes("eu.metatools.kfunnels.Funnelable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("eu.metatools.kfunnels.noservices")
class FunnelableProcessor : BasicAnnotationProcessor() {
    /**
     * Computes the name of the given [Element].
     */
    private val Element.name: Name
        get() {
            val container = enclosingElement
            if (container != null && container is QualifiedNameable)
                return Name(container.qualifiedName.toString(), simpleName.toString())
            else
                return Name(null, simpleName.toString())
        }

    /**
     * Creates a resource for a given path, optionally from a given number of source elements.
     * @param path The file path.
     * @param src The source elements.
     * @param block The block used to write the resource content.
     * @return Returns the generated file object.
     */
    private inline fun res(path: String, vararg src: Element, block: Writer.() -> Unit): FileObject {
        // Open resource and write to it using the block.
        return processingEnv.filer
                .createResource(StandardLocation.CLASS_OUTPUT, "", path, *src)
                .apply {
                    openWriter().use(block)
                }
    }

    /**
     * Creates a resource for a given name and file extension, optionally from a given number of source elements.
     * @param name The name of the resource.
     * @param ext The file extension.
     * @param src The source elements.
     * @param block The block used to write the resource content.
     * @return Returns the generated file object.
     */
    private inline fun src(name: Name, ext: String, vararg src: Element, block: Writer.() -> Unit): FileObject {
        // Open resource and write to it using the block.
        return processingEnv.filer
                .createResource(StandardLocation.SOURCE_OUTPUT, name.packageName ?: "", "${name.className}.$ext", *src)
                .apply {
                    openWriter().use(block)
                }
    }

    /**
     * True if META-INF services should be skipped
     */
    private fun noServices() =
            processingEnv.options.getOrDefault("eu.metatools.kfunnels.noservices", "false") == "true"

    override fun initSteps() = listOf(
            processingStep<Funnelable> {
                // Store unprocessed elements
                val unprocessed = HashSet<Element>(it)

                // Get all type elements with metadata annotation
                val annotated = it
                        .filterIsInstance<TypeElement>()
                        .filter {
                            it.annotationMirrors.any {
                                it.annotationType.toString() == "kotlin.Metadata"
                            }
                        }


                // Compute funneler names to be generated
                val funnelers = annotated
                        .map { it.name.mapClass { "${it}Funneler" } }

                // Compute common root
                val base = funnelers
                        .map { it.packageName ?: "" }
                        .reduce { l, r -> l.commonPrefixWith(r) }
                        .let { if (it.isEmpty()) null else it }

                // Compute the name
                val name = (base?.substringAfterLast('.') ?: "").toFirstUpper() + "Module"

                // Make the module name
                val moduleName = Name(base, name)


                // Process all elements
                for (e in annotated)
                    try {
                        generate(e, moduleName)
                        unprocessed -= e
                    } catch (x: Exception) {
                        processingEnv.messager.printMessage(
                                Diagnostic.Kind.NOTE,
                                x.toString(),
                                e)
                    }

                // Create the module content
                src(moduleName, "kt") {
                    writeTrimmed("""
        |${moduleName.kotlinPackage}
        |
        |import eu.metatools.kfunnels.*
        |import eu.metatools.kfunnels.base.*
        |
        |/**
        | * Wraps the provision of [${moduleName.joined}] so that [java.util.ServiceLoader] can instantiate it.
        | */
        |class ${moduleName.className}Provider : ModuleProvider {
        |   override fun provide() = ${moduleName.className}
        |}
        |
        |/**
        | * Holds and resolves the funnelers ${funnelers.joinToString(", ") { "[${it.joined}]" }}. Use [std] to
        | * support primitive types and collections.
        | */
        |object ${moduleName.className} : GeneratedModule {
        |
        |   override val types = listOf(${funnelers.joinToString(", ") { it.joined + ".type" }})
        |
        |   override fun <T> resolve(type: Type): Funneler<T> {""")
                    for (funneler in funnelers)
                        writeTrimmed("""
        |
        |       @Suppress("unchecked_cast")
        |       if(${funneler.joined}.type == type)
        |           return ${funneler.joined} as Funneler<T>""".trimIndent())

                    writeTrimmed("""
        |
        |       @Suppress("unchecked_cast")
        |       return NoFunneler as Funneler<T>
        |   }
        |}""")
                }

                // Write services if not deactivated
                if (!noServices())
                    res("META-INF/services/eu.metatools.kfunnels.ModuleProvider") {
                        write(moduleName.joined)
                        write("Provider\r\n")
                    }

                // Return unprocessed elements
                unprocessed
            }
    )

    /**
     * Prepares the metadata, primary constructor information, values, variables.
     */
    private class TypeEnvironment(val element: TypeElement) {
        /**
         * Metadata as defined by the Kotlin metadata annotation.
         */
        val metadata by lazy {
            element.annotationMirrors.first {
                it.annotationType.toString() == "kotlin.Metadata"
            }
        }

        /**
         * Class data, this is read from the Kotlin metadata.
         */
        val classData by lazy {
            // Get data for proto buf deserialization
            val d1 = metadata.elementValues
                    .getByName("d1")!!
                    .asList()
                    .map { it.asString() }
                    .toTypedArray()

            val d2 = metadata.elementValues
                    .getByName("d2")!!
                    .asList()
                    .map { it.asString() }
                    .toTypedArray()

            JvmProtoBufUtil.readClassDataFrom(d1, d2)
        }

        /**
         * Name resolver of the class data.
         */
        val nameResolver get() = classData.component1()

        /**
         * Class of the class data.
         */
        val data get() = classData.component2()

        /**
         * The only non-secondary constructor.
         */
        val primaryConstructor by lazy {
            // Get the primary construcotr
            val result = data.constructorList.singleOrNull { !Flags.IS_SECONDARY[it.flags] }

            // Assert that all parameters of the constructor are also exported as properties.
            if (result != null)
                for (p in result.valueParameterList)
                    check(data.propertyList.any { it.name == p.name })

            // Return the constructor
            result
        }

        /**
         * Runs the block with the primary constructor if there is one.
         */
        inline fun withPrimaryConstructor(block: (ProtoBuf.Constructor) -> Unit) {
            primaryConstructor?.let(block)
        }

        /**
         * All vars in the class.
         */
        val variables by lazy {
            data.propertyList.filter { Flags.HAS_GETTER[it.flags] && Flags.HAS_SETTER[it.flags] }
        }

        /**
         * Vars in the class that are not initialized in the constructor.
         */
        val postVariables by lazy {
            variables.filter { x ->
                primaryConstructor?.valueParameterList?.none { it.name == x.name } ?: true
            }
        }

        /**
         * All vals in the class.
         */
        val values by lazy {
            data.propertyList.filter { Flags.HAS_GETTER[it.flags] && !Flags.HAS_SETTER[it.flags] }
        }

        /**
         * Vals in the class that are not initialized in the constructor.
         */
        val postValues by lazy {
            values.filter { x ->
                primaryConstructor?.valueParameterList?.none { it.name == x.name } ?: true
            }
        }

        /**
         * All types that are required for assignment
         */
        val requiredTypes by lazy {
            val inCtor = primaryConstructor?.valueParameterList?.map { it.type } ?: emptyList()
            val inPostVariables = postVariables.map { it.returnType }

            inCtor + inPostVariables
        }

        override fun toString() = buildString {

            /**
             * Prettifies a type.
             */
            fun ProtoBuf.Type.pretty() = nameResolver.getClassId(className).shortClassName

            /**
             * Prettifies a constructor.
             */
            fun ProtoBuf.Constructor.pretty() = "<init>(" + valueParameterList.joinToString(", ") {
                nameResolver.getString(it.name) + ": " + it.type.pretty()
            } + ")"

            /**
             * Prettifies a property.
             */
            fun ProtoBuf.Property.pretty() = nameResolver.getString(name) + ": " + returnType.pretty()

            // Compose the string representation from the important members and the type mapping.
            appendln("Type environment {")
            appendln("  +primaryConstructor = ${primaryConstructor?.pretty()}")
            appendln("  +variables = ${variables.map { it.pretty() }}")
            appendln("  +postVariables = ${postVariables.map { it.pretty() }}")
            appendln("  +values = ${values.map { it.pretty() }}")
            appendln("  +postValues = ${postValues.map { it.pretty() }}")
            appendln("}")
        }
    }

    /**
     * Wraps the type string and declaration string computation.
     */
    private class FunnelerMapper(val typeEnvironment: TypeEnvironment) {

        /**
         * Computes the type of a type argument, handling projection.
         */
        private fun computeType(argument: ProtoBuf.Type.Argument): String {
            if (argument.hasProjection()) {
                when (argument.projection!!) {
                    ProtoBuf.Type.Argument.Projection.IN -> TODO("computeType Projection IN")
                    ProtoBuf.Type.Argument.Projection.OUT -> TODO("computeType Projection OUT")
                    ProtoBuf.Type.Argument.Projection.INV -> TODO("computeType Projection INV")
                    ProtoBuf.Type.Argument.Projection.STAR -> TODO("computeType Projection STAR")
                }
            }

            return computeType(argument.type)
        }

        /**
         * Checks if the given type is a primitive.
         */
        fun isPrimitive(type: ProtoBuf.Type) =
                when (typeEnvironment.nameResolver.getString(type.className)) {
                    "kotlin/Boolean" -> true
                    "kotlin/Byte" -> true
                    "kotlin/Short" -> true
                    "kotlin/Int" -> true
                    "kotlin/Long" -> true
                    "kotlin/Float" -> true
                    "kotlin/Double" -> true
                    "kotlin/Char" -> true
                    "kotlin/Unit" -> true
                    "kotlin/String" -> true
                    else -> false
                }

        /**
         * Computes the kfunnels type for the protobuf type
         */
        fun computeType(type: ProtoBuf.Type, resetNullity: Boolean = false) =
                if (isPrimitive(type))
                    computePrimitiveType(type)
                else
                    computeComplexType(type, resetNullity)

        /**
         * Computes the type of a primitive type ([isPrimitive]).
         */
        private fun computePrimitiveType(type: ProtoBuf.Type) =
                when (typeEnvironment.nameResolver.getString(type.className)) {
                    "kotlin/Boolean" -> "Type.boolean"
                    "kotlin/Byte" -> "Type.byte"
                    "kotlin/Short" -> "Type.short"
                    "kotlin/Int" -> "Type.int"
                    "kotlin/Long" -> "Type.long"
                    "kotlin/Float" -> "Type.float"
                    "kotlin/Double" -> "Type.double"
                    "kotlin/Char" -> "Type.char"
                    "kotlin/Unit" -> "Type.unit"
                    "kotlin/String" -> "Type.string"
                    else -> throw IllegalArgumentException("Type is not primitive.")
                }

        /**
         * Computes the type of a non-primitive type.
         */
        private fun computeComplexType(type: ProtoBuf.Type, resetNullity: Boolean): String {
            // Compute components
            val root = typeEnvironment.nameResolver.getString(type.className).replace('/', '.')
            val nullable = type.nullable && !resetNullity
            val args = type.argumentList.map { computeType(it) }

            // Return composed string
            return "Type($root::class, $nullable, listOf(${args.joinToString(", ")}))"
        }

        /**
         * Computes the type of a type argument, handling projection.
         */
        private fun computeDeclaration(argument: ProtoBuf.Type.Argument): String {
            if (argument.hasProjection()) {
                when (argument.projection!!) {
                    ProtoBuf.Type.Argument.Projection.IN -> TODO("computeDeclaration Projection IN")
                    ProtoBuf.Type.Argument.Projection.OUT -> TODO("computeDeclaration Projection OUT")
                    ProtoBuf.Type.Argument.Projection.INV -> TODO("computeDeclaration Projection INV")
                    ProtoBuf.Type.Argument.Projection.STAR -> TODO("computeDeclaration Projection STAR")
                }
            }

            return computeDeclaration(argument.type)
        }

        /**
         * Computes the declaration for the protobuf type
         */
        fun computeDeclaration(type: ProtoBuf.Type, resetNullity: Boolean = false) =
                if (isPrimitive(type))
                    computePrimitiveDeclaration(type)
                else
                    computeComplexDeclaration(type, resetNullity)

        /**
         * Computes the declaration of a primitive type ([isPrimitive]).
         */
        private fun computePrimitiveDeclaration(type: ProtoBuf.Type) =
                when (typeEnvironment.nameResolver.getString(type.className)) {
                    "kotlin/Boolean" -> "Boolean"
                    "kotlin/Byte" -> "Byte"
                    "kotlin/Short" -> "Short"
                    "kotlin/Int" -> "Int"
                    "kotlin/Long" -> "Long"
                    "kotlin/Float" -> "Float"
                    "kotlin/Double" -> "Double"
                    "kotlin/Char" -> "Char"
                    "kotlin/Unit" -> "Unit"
                    "kotlin/String" -> "String"
                    else -> throw IllegalArgumentException("Type is not primitive.")
                }

        /**
         * Computes the declaration of a non-primitive type.
         */
        private fun computeComplexDeclaration(type: ProtoBuf.Type, resetNullity: Boolean): String {
            // Compute components
            val root = typeEnvironment.nameResolver.getString(type.className).replace('/', '.')
            val nullable = if (type.nullable && !resetNullity) "?" else ""
            val args = type.argumentList.map { computeDeclaration(it) }

            // Return composed string
            if (args.isEmpty())
                return "$root$nullable"
            else
                return "$root<${args.joinToString(", ")}>$nullable"
        }

        fun funneler(type: ProtoBuf.Type) =
                computeDeclaration(type).replace(Regex("\\W"), "_")

        fun getPrimitive(type: ProtoBuf.Type) =
                when (typeEnvironment.nameResolver.getString(type.className)) {
                    "kotlin/Boolean" -> "getBoolean"
                    "kotlin/Byte" -> "getByte"
                    "kotlin/Short" -> "getShort"
                    "kotlin/Int" -> "getInt"
                    "kotlin/Long" -> "getLong"
                    "kotlin/Float" -> "getFloat"
                    "kotlin/Double" -> "getDouble"
                    "kotlin/Char" -> "getChar"
                    "kotlin/Unit" -> "getUnit"
                    "kotlin/String" -> "getString"
                    else -> throw IllegalArgumentException("not primitive")
                }

        fun putPrimitive(type: ProtoBuf.Type) =
                when (typeEnvironment.nameResolver.getString(type.className)) {
                    "kotlin/Boolean" -> "putBoolean"
                    "kotlin/Byte" -> "putByte"
                    "kotlin/Short" -> "putShort"
                    "kotlin/Int" -> "putInt"
                    "kotlin/Long" -> "putLong"
                    "kotlin/Float" -> "putFloat"
                    "kotlin/Double" -> "putDouble"
                    "kotlin/Char" -> "putChar"
                    "kotlin/Unit" -> "putUnit"
                    "kotlin/String" -> "putString"
                    else -> throw IllegalArgumentException("not primitive")
                }
    }

    private fun generate(element: TypeElement, moduleName: Name) {
        // Compute and store name, map for creation of new class
        val nameOriginal = element.name
        val nameGenerated = nameOriginal
                .mapClass { "${it}Funneler" }

        val target = nameOriginal.className

        // Compute the environment from the element and the mapper from the environment
        val environment = TypeEnvironment(element)
        val mapper = FunnelerMapper(environment)

        // Create file content
        src(nameGenerated, "kt") {
            fun writeFunnelers() {
                // Write funnelers for all non-primitive mappers
                for (t in environment.requiredTypes.filter { !mapper.isPrimitive(it) }) {
                    // Compute the declaration and the type
                    val label = mapper.funneler(t)
                    val decl = mapper.computeDeclaration(t, true)
                    val type = mapper.computeType(t, true)
                    // Write a value with the funneler for that declaration
                    writeTrimmed("""
        |
        |       val $label = module.resolve<$decl>($type)""")
                }
            }

            // First segment before sequential read, contains imports and declaration of the class
            writeTrimmed("""
        |${nameGenerated.kotlinPackage}
        |
        |import eu.metatools.kfunnels.*
        |
        |/**
        | * Funnels and unfunnels $target.
        | */
        |object ${nameGenerated.className} : GeneratedFunneler<$target> {
        |   override val module = ${moduleName.joined}
        |
        |   override val type = Type($target::class, false, listOf())
        |
        |   override fun read(module: Module, source: SeqSource): $target {""")


            ////////////////////////////////////////////////////////////////////////////////////////////////
            // Read from sequential source /////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////

            // Write the funnelers used by this method
            writeFunnelers()

            // Handle a primary constructor that assigns properties
            environment.withPrimaryConstructor {
                for ((i, p) in it.valueParameterList.withIndex())
                    if (mapper.isPrimitive(p.type)) {
                        // Get reader
                        val reader = mapper.getPrimitive(p.type)

                        if (p.type.nullable)
                        // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       val entry$i = if(source.isNull()) null else source.$reader()""")
                        else
                        // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       val entry$i = source.$reader()""")
                    } else {
                        // Get funneler
                        val funneler = mapper.funneler(p.type)

                        if (p.type.nullable)
                        // Write the nested reading fragment for nullable
                            writeTrimmed("""
        |
        |       val entry$i = source.readIfNotNull {
        |           source.beginNested()
        |           val r = $funneler.read(module, source)
        |           source.endNested()
        |           r
        |       }""")
                        else
                        // Write the nested reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       source.beginNested()
        |       val entry$i = $funneler.read(module, source)
        |       source.endNested()""")
                    }

                val arguments = (0 until it.valueParameterCount).joinToString(", ") { "entry$it" }

                writeTrimmed("""
        |
        |       return $target($arguments).apply {""")

            }

            // Handle an empty primary constructor
            if (environment.primaryConstructor == null) {
                writeTrimmed("""
        |
        |       return $target().apply {""")
            }

            for (p in environment.postVariables)
                if (mapper.isPrimitive(p.returnType)) {
                    // Get property name and reader
                    val name = environment.nameResolver.getString(p.name)
                    val reader = mapper.getPrimitive(p.returnType)

                    if (p.returnType.nullable)
                    // Write the reading fragment for nullable
                        writeTrimmed("""
        |
        |       $name = if(source.isNull()) null else source.$reader()""")
                    else
                    // Write the reading fragment for non-nullable
                        writeTrimmed("""
        |
        |       $name = source.$reader()""")
                } else {
                    // Get property name and funneler
                    val name = environment.nameResolver.getString(p.name)
                    val funneler = mapper.funneler(p.returnType)

                    if (p.returnType.nullable)
                    // Write the nested reading fragment for nullable
                        writeTrimmed("""
        |
        |       $name = source.readIfNotNull {
        |           source.beginNested()
        |           r = $funneler.read(module, source)
        |           source.endNested()
        |           r
        |       }""")
                    else
                    // Write the nested reading fragment for non-nullable
                        writeTrimmed("""
        |
        |       source.beginNested()
        |       $name = $funneler.read(module, source)
        |       source.endNested()""")
                }


            writeTrimmed("""
        |
        |       }
        |   }
        |
        |   override fun read(module: Module, source: LabelSource): $target {""")


            ////////////////////////////////////////////////////////////////////////////////////////////////
            // Read from labeled source ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////

            // Write the funnelers used by this method
            writeFunnelers()

            // Handle a primary constructor that assigns properties
            environment.withPrimaryConstructor {
                for ((i, p) in it.valueParameterList.withIndex())
                    if (mapper.isPrimitive(p.type)) {
                        // Get property name and reader
                        val name = environment.nameResolver.getString(p.name)
                        val reader = mapper.getPrimitive(p.type)

                        if (p.type.nullable)
                        // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       val entry$i = if(source.isNull("$name")) null else source.$reader("$name")""")
                        else
                        // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       val entry$i = source.$reader("$name")""")
                    } else {
                        // Get property name and funneler
                        val name = environment.nameResolver.getString(p.name)
                        val funneler = mapper.funneler(p.type)

                        // Write the nested reading fragment
                        writeTrimmed("""
        |
        |       source.beginNested("$name")
        |       val entry$i = $funneler.read(module, source)
        |       source.endNested()""")
                    }

                val arguments = (0 until it.valueParameterCount).joinToString(", ") { "entry$it" }

                writeTrimmed("""
        |
        |       return $target($arguments).apply {""")

            }

            // Handle an empty primary constructor
            if (environment.primaryConstructor == null) {
                writeTrimmed("""
        |
        |       return $target().apply {""")
            }

            for (p in environment.postVariables)
                if (mapper.isPrimitive(p.returnType)) {
                    // Get property name and reader
                    val name = environment.nameResolver.getString(p.name)
                    val reader = mapper.getPrimitive(p.returnType)

                    // Write the reading fragment, target the receiver
                    writeTrimmed("""
        |
        |           $name = source.$reader("$name")""")
                } else {
                    // Get property name and funneler
                    val name = environment.nameResolver.getString(p.name)
                    val funneler = mapper.funneler(p.returnType)

                    // Write the nested reading fragment, target the receiver
                    writeTrimmed("""
        |
        |           source.beginNested("$name")
        |           $name = $funneler.read(module, source)
        |           source.endNested()""")
                }


            writeTrimmed("""
        |
        |       }
        |   }
        |
        |   override fun write(module: Module, sink: SeqSink, item: $target) {""")


            ////////////////////////////////////////////////////////////////////////////////////////////////
            // Write to sequential sink ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////

            // Write the funnelers used by this method
            writeFunnelers()

            // Handle a primary constructor that contains properties
            environment.withPrimaryConstructor {

                for (p in it.valueParameterList)
                    if (mapper.isPrimitive(p.type)) {
                        // Get property name and writer
                        val name = environment.nameResolver.getString(p.name)
                        val writer = mapper.putPrimitive(p.type)

                        if (p.type.nullable)
                        // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       if(item.$name == null)
        |           sink.putNull(true)
        |       else {
        |           sink.putNull(false)
        |           sink.$writer(item.$name)
        |       }""")
                        else
                        // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       sink.$writer(item.$name)""")
                    } else {
                        // Get property name and funneler
                        val name = environment.nameResolver.getString(p.name)
                        val label = mapper.funneler(p.type)


                        if (p.type.nullable)
                        // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       if(item.$name == null)
        |           sink.putNull(true)
        |       else {
        |           sink.putNull(false)
        |           sink.beginNested()
        |           $label.write(module, sink, item.$name)
        |           sink.endNested()
        |       }""")
                        else
                        // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       sink.beginNested()
        |       $label.write(module, sink, item.$name)
        |       sink.endNested()""")
                    }

            }

            for (p in environment.postVariables)
                if (mapper.isPrimitive(p.returnType)) {
                    // Get property name and writer
                    val name = environment.nameResolver.getString(p.name)
                    val writer = mapper.putPrimitive(p.returnType)

                    if (p.returnType.nullable)
                    // Write the writing fragment for nullable
                        writeTrimmed("""
        |
        |       val capture_$name = item.$name
        |       if(capture_$name == null)
        |           sink.putNull(true)
        |       else {
        |           sink.putNull(false)
        |           sink.$writer(capture_$name)
        |       }""")
                    else
                    // Write the writing fragment for non-nullable
                        writeTrimmed("""
        |
        |       sink.$writer(item.$name)""")
                } else {
                    // Get property name and funneler
                    val name = environment.nameResolver.getString(p.name)
                    val label = mapper.funneler(p.returnType)

                    // Write the nested writing fragment
                    writeTrimmed("""
        |
        |       sink.beginNested()
        |       $label.write(module, sink, item.$name)
        |       sink.endNested()""")

                    if (p.returnType.nullable)
                    // Write the nested writing fragment for nullable
                        writeTrimmed("""
        |
        |       val capture_$name = item.$name
        |       if(capture_$name == null)
        |           sink.putNull(true)
        |       else {
        |           sink.putNull(false)
        |           sink.beginNested()
        |           $label.write(module, sink, capture_$name)
        |           sink.endNested()
        |       }""")
                    else
                    // Write the nested writing fragment for non-nullable
                        writeTrimmed("""
        |
        |       sink.beginNested()
        |       $label.write(module, sink, item.$name)
        |       sink.endNested()""")
                }


            writeTrimmed("""
        |
        |   }
        |
        |   override fun write(module: Module, sink: LabelSink, item: $target) {""")


            ////////////////////////////////////////////////////////////////////////////////////////////////
            // Write to labeled sink ///////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////

            // Write the funnelers used by this method
            writeFunnelers()

            // Handle a primary constructor that contains properties
            environment.withPrimaryConstructor {
                for (p in it.valueParameterList)
                    if (mapper.isPrimitive(p.type)) {
                        // Get property name and writer
                        val name = environment.nameResolver.getString(p.name)
                        val writer = mapper.putPrimitive(p.type)

                        if (p.type.nullable)
                        // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       if(item.$name == null)
        |           sink.putNull("$name", true)
        |       else {
        |           sink.putNull("$name", false)
        |           sink.$writer("$name", item.$name)
        |       }""")
                        else
                        // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       sink.$writer("$name", item.$name)""")
                    } else {
                        // Get property name and funneler
                        val name = environment.nameResolver.getString(p.name)
                        val label = mapper.funneler(p.type)


                        if (p.type.nullable)
                        // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       if(item.$name == null)
        |           sink.putNull("$name", true)
        |       else {
        |           sink.putNull("$name", false)
        |           sink.beginNested("$name")
        |           $label.write(module, sink, item.$name)
        |           sink.endNested()
        |       }""")
                        else
                        // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       sink.beginNested("$name")
        |       $label.write(module, sink, item.$name)
        |       sink.endNested()""")
                    }

            }

            for (p in environment.postVariables)
                if (mapper.isPrimitive(p.returnType)) {
                    // Get property name and writer
                    val name = environment.nameResolver.getString(p.name)
                    val writer = mapper.putPrimitive(p.returnType)

                    if (p.returnType.nullable)
                    // Write the writing fragment for nullable
                        writeTrimmed("""
        |
        |       val capture_$name = item.$name
        |       if(capture_$name == null)
        |           sink.putNull("$name", true)
        |       else {
        |           sink.putNull("$name", false)
        |           sink.$writer("$name", capture_$name)
        |       }""")
                    else
                    // Write the writing fragment for non-nullable
                        writeTrimmed("""
        |
        |       sink.$writer("$name", item.$name)""")
                } else {
                    // Get property name and funneler
                    val name = environment.nameResolver.getString(p.name)
                    val label = mapper.funneler(p.returnType)

                    // Write the nested writing fragment
                    writeTrimmed("""
        |
        |       sink.beginNested("$name")
        |       $label.write(module, sink, item.$name)
        |       sink.endNested()""")

                    if (p.returnType.nullable)
                    // Write the nested writing fragment for nullable
                        writeTrimmed("""
        |
        |       val capture_$name = item.$name
        |       if(capture_$name == null)
        |           sink.putNull("$name", true)
        |       else {
        |           sink.putNull("$name", false)
        |           sink.beginNested("$name")
        |           $label.write(module, sink, capture_$name)
        |           sink.endNested()
        |       }""")
                    else
                    // Write the nested writing fragment for non-nullable
                        writeTrimmed("""
        |
        |       sink.beginNested("$name")
        |       $label.write(module, sink, item.$name)
        |       sink.endNested()""")
                }


            writeTrimmed("""
        |
        |   }
        |}
        """)
        }
    }
}