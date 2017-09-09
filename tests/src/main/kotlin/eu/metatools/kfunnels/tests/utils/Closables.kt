package eu.metatools.kfunnels.tests.utils

/**
 * If the receiver is closable, it will be [AutoCloseable.use]d in the block, otherwise just passed to the block.
 */
inline fun <T> T.useIfClosable(block: (T) -> Unit) {
    val x = this as? AutoCloseable
    if (x != null) {
        try {
            block(this)
        } finally {
            x.close()
        }
    } else
        block(this)
}