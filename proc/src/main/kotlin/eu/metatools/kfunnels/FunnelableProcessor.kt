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
        | * Holds and resolves the funnelers:
        |${funnelers.joinToString("") { " * * [${it.joined}]\r\n" }}
        | * Use [std] to support primitive types and collections.
        | */
        |object ${moduleName.className} : GeneratedModule {
        |
        |   override val types get() = listOf(${funnelers.joinToString(", ") { it.joined }})
        |
        |   override fun <T> resolve(type: Type): Funneler<T> {""")
                    for (funneler in funnelers)
                        writeTrimmed("""
        |
        |       @Suppress("unchecked_cast")
        |       if(${funneler.joined}.type == type.kClass)
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
    private inner class TypeEnvironment(val element: TypeElement) {
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
         * True if the type is a class
         */
        val isClass by lazy {
            Flags.CLASS_KIND[data.flags] == ProtoBuf.Class.Kind.CLASS
        }

        /**
         * True if this class is not abstract
         */
        val isInstanitable by lazy {
            Flags.MODALITY[data.flags] != ProtoBuf.Modality.ABSTRACT
        }

        /**
         * The type arguments of the described class.
         */
        val typeArguments by lazy {
            data.typeParameterList
        }

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
    private inner class FunnelerMapper(val typeEnvironment: TypeEnvironment) {

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
            // If type parameter, get from type parameter list
            if (type.hasTypeParameter()) {
                // Compute nulling operator and index
                // TODO Is reset here necesseary or just symmetric
                val nullable = if (type.nullable && !resetNullity) "-" else "+"
                val index = type.typeParameter

                // Compose
                return "${nullable}type.args[$index]"
            }

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
            // If type parameter, get from type parameter list
            if (type.hasTypeParameter()) {
                // Compute type argument name and nullable suffix
                val name = typeEnvironment.nameResolver.getString(
                        typeEnvironment.typeArguments[type.typeParameter].name)
                val nullable = if (type.nullable) "?" else ""

                // Compose
                return "$name$nullable"
            }

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

        /**
         * Returns the declaration to fill in for a type parameter. This is the single upper bound (where clauses not
         * yet/ever supported) or the universally satisfiable type Any?.
         */
        fun computeBound(type: ProtoBuf.Type): String {
            // Assert that the call is at the correct site
            check(type.hasTypeParameter())

            // Get the corresponding argument from the class head
            val arg = typeEnvironment.typeArguments[type.typeParameter]

            // Find the declaration for the single upper bound, or return "Any?"
            return arg.upperBoundList.map { computeDeclaration(it) }.singleOrNull() ?: "Any?"
        }

        /**
         * True if the type has not substitute, e.g. is final, an object or enum or enum constant.
         */
        fun isTerminal(type: ProtoBuf.Type): Boolean {
            if (type.hasTypeParameter())
                return false

            // TODO: Replace computeDeclaration with something more sensible (computeCanonicalName)
            val canonicalName = computeDeclaration(type).substringBefore('<')

            // TODO: Preliminary fix
            when (canonicalName) {
                "kotlin.collections.MutableCollection",
                "kotlin.collections.MutableList",
                "kotlin.collections.ArrayList",
                "kotlin.collections.Collection",
                "kotlin.collections.List",
                "kotlin.collections.MutableSet",
                "kotlin.collections.HashSet",
                "kotlin.collections.Set" -> return true
            }

            val typeElement = processingEnv.elementUtils.getTypeElement(canonicalName)

            return when (typeElement.kind) {
                ElementKind.CLASS ->
                    typeElement.modifiers.contains(Modifier.FINAL)
                ElementKind.ENUM -> true
                ElementKind.INTERFACE -> false
                else -> error { "Cannot determine if $canonicalName is terminal." }
            }
        }

        fun isTerminal2(type: ProtoBuf.Type): Boolean {
            // TODO: This information is not present in the metadata
            if (type.hasTypeParameter())
                return false

            return when (Flags.CLASS_KIND[type.flags]) {
                ProtoBuf.Class.Kind.CLASS ->
                    when (Flags.MODALITY[type.flags]) {
                        null,
                        ProtoBuf.Modality.ABSTRACT,
                        ProtoBuf.Modality.OPEN,
                        ProtoBuf.Modality.SEALED -> false

                        ProtoBuf.Modality.FINAL -> true
                    }

                null,
                ProtoBuf.Class.Kind.INTERFACE -> false

                ProtoBuf.Class.Kind.ENUM_CLASS,
                ProtoBuf.Class.Kind.ENUM_ENTRY,
                ProtoBuf.Class.Kind.ANNOTATION_CLASS,
                ProtoBuf.Class.Kind.OBJECT,
                ProtoBuf.Class.Kind.COMPANION_OBJECT -> true
            }
        }

        fun funneler(type: ProtoBuf.Type) =
                computeDeclaration(type).replace(Regex("\\W"), "_")

        fun getter(type: ProtoBuf.Type): String {
            val infix = if (type.nullable) "Nullable" else ""
            return when (typeEnvironment.nameResolver.getString(type.className)) {
                "kotlin/Boolean" -> "get${infix}Boolean"
                "kotlin/Byte" -> "get${infix}Byte"
                "kotlin/Short" -> "get${infix}Short"
                "kotlin/Int" -> "get${infix}Int"
                "kotlin/Long" -> "get${infix}Long"
                "kotlin/Float" -> "get${infix}Float"
                "kotlin/Double" -> "get${infix}Double"
                "kotlin/Char" -> "get${infix}Char"
                "kotlin/Unit" -> "get${infix}Unit"
                "kotlin/String" -> "get${infix}String"
                else -> if (isTerminal(type))
                    "get${infix}TerminalNested"
                else
                    "get${infix}DynamicNested"
            }
        }

        fun putter(type: ProtoBuf.Type): String {
            val infix = if (type.nullable) "Nullable" else ""
            return when (typeEnvironment.nameResolver.getString(type.className)) {
                "kotlin/Boolean" -> "put${infix}Boolean"
                "kotlin/Byte" -> "put${infix}Byte"
                "kotlin/Short" -> "put${infix}Short"
                "kotlin/Int" -> "put${infix}Int"
                "kotlin/Long" -> "put${infix}Long"
                "kotlin/Float" -> "put${infix}Float"
                "kotlin/Double" -> "put${infix}Double"
                "kotlin/Char" -> "put${infix}Char"
                "kotlin/Unit" -> "put${infix}Unit"
                "kotlin/String" -> "put${infix}String"
                else -> if (isTerminal(type))
                    "put${infix}TerminalNested"
                else
                    "put${infix}DynamicNested"
            }
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

        // Check if element is a class TODO: Also support enums
        check(environment.isClass, { "Funnelable element must be a class" })

        // Create file content
        src(nameGenerated, "kt") {
            /**
             * Generates the funnelers that may be loaded statically at the start of funneling
             */
            fun generateFunnelers(all: Boolean) {
                // Write funnelers for all non-primitive mappers
                for (t in environment.requiredTypes.distinctBy { mapper.computeType(it) }) {
                    // Do not generate funnelers for primitive types
                    if (mapper.isPrimitive(t))
                        continue

                    if (!all && !mapper.isTerminal(t))
                        continue

                    // Compute the declaration and the type
                    val label = mapper.funneler(t)
                    val decl = if (t.hasTypeParameter()) mapper.computeBound(t) else mapper.computeDeclaration(t, true)
                    val type = mapper.computeType(t, true)

                    // Write a value with the funneler for that declaration
                    writeTrimmed("""
        |
        |       val ${label}_type = $type
        |       val $label = module.resolve<$decl>(${label}_type)""")
                }
            }

            // Compute the star projection type argument list
            val liftedTypeArgs = if (environment.typeArguments.isEmpty())
                ""
            else
                "<" + environment.typeArguments.joinToString(", ") { "*" } + ">"

            // First segment before sequential read, contains imports and declaration of the class
            writeTrimmed("""
        |${nameGenerated.kotlinPackage}
        |
        |import eu.metatools.kfunnels.*
        |
        |/**
        | * Funnels and unfunnels $target.
        | */
        |object ${nameGenerated.className} : GeneratedFunneler<$target$liftedTypeArgs> {
        |    override val module = ${moduleName.joined}
        |
        |    override val type = $target::class""")

            /**
             * Generates the read method for either suspended or unsuspended sources.
             */
            fun generateRead(isSuspend: Boolean) {
                // Compute prefixes for suspension
                val funSuspend = if (isSuspend) "suspend " else ""
                val pivotSuspend = if (isSuspend) "Suspend" else ""

                writeTrimmed("""
        |
        |    override $funSuspend fun read(module: Module, type: Type, source: ${pivotSuspend}Source)
        |           : $target$liftedTypeArgs = source.markAround(type) {""")
                if (environment.isInstanitable) {
                    // Write the funnelers used by this method
                    generateFunnelers(true)

                    // Handle a primary constructor that assigns properties
                    environment.withPrimaryConstructor {
                        for ((i, p) in it.valueParameterList.withIndex())
                            if (mapper.isPrimitive(p.type)) {
                                // Get property name and getter
                                val name = environment.nameResolver.getString(p.name)
                                val getter = mapper.getter(p.type)

                                writeTrimmed("""
        |
        |       val entry$i = source.$getter("$name")""")
                            } else if (mapper.isTerminal(p.type)) {
                                // Get property name and funneler
                                val name = environment.nameResolver.getString(p.name)
                                val label = mapper.funneler(p.type)
                                val getter = mapper.getter(p.type)

                                // Write the nested reading fragment
                                writeTrimmed("""
        |
        |       val entry$i = source.$getter(module, $label, "$name", ${label}_type)""")
                            } else {
                                // Get property name and funneler
                                val name = environment.nameResolver.getString(p.name)
                                val label = mapper.funneler(p.type)
                                val type = mapper.computeType(p.type, true)
                                val getter = mapper.getter(p.type)

                                // Write the nested reading fragment
                                writeTrimmed("""
        |
        |       val entry$i = source.$getter(module, $label, "$name", $type)""")
                            }

                        val arguments = (0 until it.valueParameterCount).joinToString(", ") { "entry$it" }

                        writeTrimmed("""
        |
        |       val result = $target($arguments)""")

                    }

                    // Handle an empty primary constructor
                    if (environment.primaryConstructor == null) {
                        writeTrimmed("""
        |
        |       val result = $target()""")
                    }

                    for (p in environment.postVariables)
                        if (mapper.isPrimitive(p.returnType)) {
                            // Get property name and getter
                            val name = environment.nameResolver.getString(p.name)
                            val getter = mapper.getter(p.returnType)

                            writeTrimmed("""
        |
        |       result.$name = source.$getter("$name")""")
                        } else if (mapper.isTerminal(p.returnType)) {
                            // Get property name and funneler
                            val name = environment.nameResolver.getString(p.name)
                            val label = mapper.funneler(p.returnType)
                            val getter = mapper.getter(p.returnType)

                            // Write the nested reading fragment
                            writeTrimmed("""
        |
        |       result.$name = source.$getter(module, $label, "$name", ${label}_type)""")
                        } else {
                            // Get property name and funneler
                            val name = environment.nameResolver.getString(p.name)
                            val label = mapper.funneler(p.returnType)
                            val type = mapper.computeType(p.returnType, true)
                            val getter = mapper.getter(p.returnType)
                            val declaration = if (p.returnType.hasTypeParameter())
                                mapper.computeBound(p.returnType)
                            else
                                mapper.computeDeclaration(p.returnType, true)

                            // Write the nested reading fragment
                            writeTrimmed("""
        |
        |       result.$name = source.$getter<$declaration>(module, $label, "$name", $type)""")
                        }

                    writeTrimmed("""
        |
        |       result
        |   }
                """)
                } else {
                    writeTrimmed("""
        |
        |       throw UnsupportedOperationException("Class is abstract")
                """)
                }
            }

            // Generate unsuspended and suspended variant
            generateRead(false)
            generateRead(true)

            /**
             * Generates the write method for either suspended or unsuspended sinks.
             */
            fun generateWrite(isSuspend: Boolean) {
                // Compute prefixes for suspension
                val funSuspend = if (isSuspend) "suspend " else ""
                val pivotSuspend = if (isSuspend) "Suspend" else ""

                writeTrimmed("""
        |
        |   override $funSuspend fun write(module: Module, type: Type, sink: ${pivotSuspend}Sink, item: $target$liftedTypeArgs)
        |           = sink.markAround(type) {""")

                // Write the funnelers used by this method, no dynamics
                generateFunnelers(false)

                // Handle a primary constructor that contains properties
                environment.withPrimaryConstructor {
                    for (p in it.valueParameterList)
                        if (mapper.isPrimitive(p.type)) {
                            // Get property name and putter
                            val name = environment.nameResolver.getString(p.name)
                            val putter = mapper.putter(p.type)

                            // Write the reading fragment for nullable
                            writeTrimmed("""
        |
        |       sink.$putter("$name", item.$name)""")
                        } else if (mapper.isTerminal(p.type)) {
                            // Get property name and funneler
                            val name = environment.nameResolver.getString(p.name)
                            val label = mapper.funneler(p.type)
                            val putter = mapper.putter(p.type)

                            // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       sink.$putter(module, ${label}, "$name", ${label}_type, item.$name)""")
                        } else {
                            // Get property name and funneler
                            val name = environment.nameResolver.getString(p.name)
                            val type = mapper.computeType(p.type, true)
                            val putter = mapper.putter(p.type)

                            // Write the reading fragment for non-nullable
                            writeTrimmed("""
        |
        |       sink.$putter(module, "$name", $type, item.$name)""")
                        }

                }

                for (p in environment.postVariables)
                    if (mapper.isPrimitive(p.returnType)) {
                        // Get property name and putter
                        val name = environment.nameResolver.getString(p.name)
                        val putter = mapper.putter(p.returnType)

                        // Write the reading fragment for nullable
                        writeTrimmed("""
        |
        |       sink.$putter("$name", item.$name)""")
                    } else if (mapper.isTerminal(p.returnType)) {
                        // Get property name and funneler
                        val name = environment.nameResolver.getString(p.name)
                        val label = mapper.funneler(p.returnType)
                        val putter = mapper.putter(p.returnType)

                        // Write the reading fragment for non-nullable
                        writeTrimmed("""
        |
        |       sink.$putter(module, ${label}, "$name", ${label}_type, item.$name)""")
                    } else {
                        // Get property name and funneler
                        val name = environment.nameResolver.getString(p.name)
                        val type = mapper.computeType(p.returnType, true)
                        val putter = mapper.putter(p.returnType)

                        // Write the reading fragment for non-nullable
                        writeTrimmed("""
        |
        |       sink.$putter(module, "$name", $type, item.$name)""")
                    }


                writeTrimmed("""
        |
        |   }""")
            }

            generateWrite(false)
            generateWrite(true)

            writeTrimmed("""
        |
        |}
        """)
        }
    }

    private fun note(message: () -> Any?) =
            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, message()?.toString())

    private fun error(message: () -> Any?): Nothing {
        val msg = message()?.toString() ?: ""
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg)
        kotlin.error(msg)
    }

    private fun warn(message: () -> Any?) =
            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, message()?.toString())

    private fun mandatoryWarn(message: () -> Any?) =
            processingEnv.messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, message()?.toString())

    private fun other(message: () -> Any?) =
            processingEnv.messager.printMessage(Diagnostic.Kind.OTHER, message()?.toString())
}