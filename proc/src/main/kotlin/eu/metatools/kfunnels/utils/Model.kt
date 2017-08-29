package eu.metatools.kfunnels.utils

import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror

/**
 * Gets the entry in the map that has the matching simple name.
 */
fun <K : Element, V> Map<K, V>.getByName(name: String) =
        entries.find { (k, _) -> k.simpleName.contentEquals(name) }?.value

/**
 * Base class that can be derrived to achieve visitor based class casting.
 */
private abstract class ClassCastVisitor<T> : AnnotationValueVisitor<T, Unit> {
    override fun visitInt(p0: Int, p1: Unit): T = throw ClassCastException()

    override fun visitChar(p0: Char, p1: Unit): T = throw ClassCastException()

    override fun visitString(p0: String, p1: Unit): T = throw ClassCastException()

    override fun visitBoolean(p0: Boolean, p1: Unit): T = throw ClassCastException()

    override fun visit(p0: AnnotationValue, p1: Unit): T = throw ClassCastException()

    override fun visit(p0: AnnotationValue): T = throw ClassCastException()

    override fun visitUnknown(p0: AnnotationValue, p1: Unit): T = throw ClassCastException()

    override fun visitDouble(p0: Double, p1: Unit): T = throw ClassCastException()

    override fun visitByte(p0: Byte, p1: Unit): T = throw ClassCastException()

    override fun visitArray(p0: MutableList<out AnnotationValue>, p1: Unit): T = throw ClassCastException()

    override fun visitShort(p0: Short, p1: Unit): T = throw ClassCastException()

    override fun visitLong(p0: Long, p1: Unit): T = throw ClassCastException()

    override fun visitFloat(p0: Float, p1: Unit): T = throw ClassCastException()

    override fun visitEnumConstant(p0: VariableElement, p1: Unit): T = throw ClassCastException()

    override fun visitAnnotation(p0: AnnotationMirror, p1: Unit): T = throw ClassCastException()

    override fun visitType(p0: TypeMirror, p1: Unit): T = throw ClassCastException()
}

/**
 * Accepts a unit visitor.
 */
fun <R> AnnotationValue.accept(visitor: AnnotationValueVisitor<R, Unit>) =
        accept(visitor, Unit)

/**
 * Casts the value as a list.
 */
fun AnnotationValue.asList() =
        accept(object : ClassCastVisitor<List<AnnotationValue>>() {
            override fun visitArray(p0: MutableList<out AnnotationValue>, p1: Unit) = p0.toList()
        })

/**
 * Casts the value as a string.
 *
 */
fun AnnotationValue.asString() =
        accept(object : ClassCastVisitor<String>() {
            override fun visitString(p0: String, p1: Unit) = p0
        })