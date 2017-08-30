package eu.metatools.kfunnels.utils

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import javax.lang.model.element.Element

/**
 * A processing step for a single annotation.
 * @param T The type of the annotation
 * @param block The processing method
 */
inline fun <reified T : Annotation>
        processingStep(crossinline block: (Set<Element>) -> Set<Element>) =
        object : BasicAnnotationProcessor.ProcessingStep {
            override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>) =
                    block(elementsByAnnotation.get(T::class.java))

            override fun annotations() =
                    setOf(T::class.java)
        }

/**
 * A processing step for two annotations.
 * @param T The type of the first annotation
 * @param U The type of the second annotation
 * @param block The processing method
 */
inline fun <reified T : Annotation, reified U : Annotation>
        processingStep(crossinline block: (Set<Element>, Set<Element>) -> Set<Element>) =
        object : BasicAnnotationProcessor.ProcessingStep {
            override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>) =
                    block(elementsByAnnotation.get(T::class.java),
                            elementsByAnnotation.get(U::class.java))

            override fun annotations() =
                    setOf(T::class.java, U::class.java)
        }

/**
 * A processing step for three annotations.
 * @param T The type of the first annotation
 * @param U The type of the second annotation
 * @param V The type of the third annotation
 * @param block The processing method
 */
inline fun <reified T : Annotation, reified U : Annotation, reified V : Annotation>
        processingStep(crossinline block: (Set<Element>, Set<Element>, Set<Element>) -> Set<Element>) =
        object : BasicAnnotationProcessor.ProcessingStep {
            override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>) =
                    block(elementsByAnnotation.get(T::class.java),
                            elementsByAnnotation.get(U::class.java),
                            elementsByAnnotation.get(V::class.java))

            override fun annotations() =
                    setOf(T::class.java, U::class.java, V::class.java)
        }