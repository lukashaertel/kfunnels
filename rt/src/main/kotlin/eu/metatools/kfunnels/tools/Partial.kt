package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.*

/**
 * A source where all methods are implemented as an error by default.
 */
abstract class PartialSource : Source {
    protected open fun partialError(): Nothing {
        error("Method not defined, source is partial.")
    }

    override fun begin(type: Type): Begin {
        partialError()
    }

    override fun isEnd(): Boolean {
        partialError()
    }

    override fun end(type: Type) {
        partialError()
    }

    override fun beginNested(label: String, type: Type): Nested {
        partialError()
    }

    override fun endNested(label: String, type: Type) {
        partialError()
    }

    override fun getBoolean(label: String): Boolean {
        partialError()
    }

    override fun getByte(label: String): Byte {
        partialError()
    }

    override fun getShort(label: String): Short {
        partialError()
    }

    override fun getInt(label: String): Int {
        partialError()
    }

    override fun getLong(label: String): Long {
        partialError()
    }

    override fun getFloat(label: String): Float {
        partialError()
    }

    override fun getDouble(label: String): Double {
        partialError()
    }

    override fun getChar(label: String): Char {
        partialError()
    }

    override fun isNull(label: String): Boolean {
        partialError()
    }

    override fun getUnit(label: String) {
        partialError()
    }

    override fun getString(label: String): String {
        partialError()
    }
}


/**
 * A source where all methods are implemented as an error by default.
 */
abstract class PartialSuspendSource : SuspendSource {
    protected open fun partialError(): Nothing {
        error("Method not defined, source is partial.")
    }

    override suspend fun begin(type: Type): Begin {
        partialError()
    }

    override suspend fun isEnd(): Boolean {
        partialError()
    }

    override suspend fun end(type: Type) {
        partialError()
    }

    override suspend fun beginNested(label: String, type: Type): Nested {
        partialError()
    }

    override suspend fun endNested(label: String, type: Type) {
        partialError()
    }

    override suspend fun getBoolean(label: String): Boolean {
        partialError()
    }

    override suspend fun getByte(label: String): Byte {
        partialError()
    }

    override suspend fun getShort(label: String): Short {
        partialError()
    }

    override suspend fun getInt(label: String): Int {
        partialError()
    }

    override suspend fun getLong(label: String): Long {
        partialError()
    }

    override suspend fun getFloat(label: String): Float {
        partialError()
    }

    override suspend fun getDouble(label: String): Double {
        partialError()
    }

    override suspend fun getChar(label: String): Char {
        partialError()
    }

    override suspend fun isNull(label: String): Boolean {
        partialError()
    }

    override suspend fun getUnit(label: String) {
        partialError()
    }

    override suspend fun getString(label: String): String {
        partialError()
    }
}

/**
 * A sink where all methods are implemented as an error by default.
 */
abstract class PartialSink : Sink {
    protected open fun partialError(): Nothing {
        error("Method not defined, sink is partial.")
    }

    override fun begin(type: Type, value: Any?): Boolean {
        partialError()
    }

    override fun end(type: Type, value: Any?) {
        partialError()
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        partialError()
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        partialError()
    }

    override fun putBoolean(label: String, value: Boolean) {
        partialError()
    }

    override fun putByte(label: String, value: Byte) {
        partialError()
    }

    override fun putShort(label: String, value: Short) {
        partialError()
    }

    override fun putInt(label: String, value: Int) {
        partialError()
    }

    override fun putLong(label: String, value: Long) {
        partialError()
    }

    override fun putFloat(label: String, value: Float) {
        partialError()
    }

    override fun putDouble(label: String, value: Double) {
        partialError()
    }

    override fun putChar(label: String, value: Char) {
        partialError()
    }

    override fun putNull(label: String, isNull: Boolean) {
        partialError()
    }

    override fun putUnit(label: String, value: Unit) {
        partialError()
    }

    override fun putString(label: String, value: String) {
        partialError()
    }
}

/**
 * A sink where all methods are implemented as an error by default.
 */
abstract class PartialSuspendSink : SuspendSink {
    protected open fun partialError(): Nothing {
        error("Method not defined, sink is partial.")
    }

    override suspend fun begin(type: Type, value: Any?): Boolean {
        partialError()
    }

    override suspend fun end(type: Type, value: Any?) {
        partialError()
    }

    override suspend fun beginNested(label: String, type: Type, value: Any?): Boolean {
        partialError()
    }

    override suspend fun endNested(label: String, type: Type, value: Any?) {
        partialError()
    }

    override suspend fun putBoolean(label: String, value: Boolean) {
        partialError()
    }

    override suspend fun putByte(label: String, value: Byte) {
        partialError()
    }

    override suspend fun putShort(label: String, value: Short) {
        partialError()
    }

    override suspend fun putInt(label: String, value: Int) {
        partialError()
    }

    override suspend fun putLong(label: String, value: Long) {
        partialError()
    }

    override suspend fun putFloat(label: String, value: Float) {
        partialError()
    }

    override suspend fun putDouble(label: String, value: Double) {
        partialError()
    }

    override suspend fun putChar(label: String, value: Char) {
        partialError()
    }

    override suspend fun putNull(label: String, isNull: Boolean) {
        partialError()
    }

    override suspend fun putUnit(label: String, value: Unit) {
        partialError()
    }

    override suspend fun putString(label: String, value: String) {
        partialError()
    }
}