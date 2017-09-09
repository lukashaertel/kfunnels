package eu.metatools.kfunnels.tools.android

import android.content.Intent
import android.os.Bundle
import eu.metatools.kfunnels.*

class BundleSink(val bundle: Bundle) : Sink {
    override fun begin(type: Type, value: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun end(type: Type, value: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putBoolean(label: String, value: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putByte(label: String, value: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putShort(label: String, value: Short) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putInt(label: String, value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putLong(label: String, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putFloat(label: String, value: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putDouble(label: String, value: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putChar(label: String, value: Char) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putNull(label: String, isNull: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putUnit(label: String, value: Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putString(label: String, value: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class BundleSource(val bundle: Bundle) : Source {
    override fun begin(type: Type): Begin {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEnd(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun end(type: Type) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beginNested(label: String, type: Type): Nested {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endNested(label: String, type: Type) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(label: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByte(label: String): Byte {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShort(label: String): Short {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(label: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(label: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(label: String): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDouble(label: String): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChar(label: String): Char {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNull(label: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUnit(label: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(label: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}