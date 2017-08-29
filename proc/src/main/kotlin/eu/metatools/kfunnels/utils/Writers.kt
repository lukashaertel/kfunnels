package eu.metatools.kfunnels.utils

import java.io.Writer

/**
 * Writes with [String.trimMargin].
 */
fun Writer.writeTrimmed(string: String) =
        write(string.trimMargin())