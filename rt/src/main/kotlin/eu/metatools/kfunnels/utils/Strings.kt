package eu.metatools.kfunnels.utils

/**
 * Returns the string with the first letter uppercased.
 */
fun String.toFirstUpper() =
        if (isEmpty()) this else get(0).toUpperCase() + substring(1)

/**
 * Returns the string with the first letter lowercased.
 */
fun String.toFirstLower() =
        if (isEmpty()) this else get(0).toLowerCase() + substring(1)

/**
 * Returns null if this is true, otherwise empty string.
 */
fun Boolean.tn() = if (this) null else ""

/**
 * Returns null if this is false, otherwise empty string.
 */
fun Boolean.fn() = if (this) "" else null