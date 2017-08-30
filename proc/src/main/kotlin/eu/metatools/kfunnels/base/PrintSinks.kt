package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.LabelSink
import eu.metatools.kfunnels.SeqSink

object PrintSeqSink : SeqSink {
    override fun putBoolean(b: Boolean) {
        println("Boolean $b")
    }

    override fun putByte(b: Byte) {
        println("Byte $b")
    }

    override fun putShort(s: Short) {
        println("Short $s")
    }

    override fun putInt(i: Int) {
        println("Int $i")
    }

    override fun putLong(l: Long) {
        println("Long $l")
    }

    override fun putFloat(f: Float) {
        println("Float $f")
    }

    override fun putDouble(d: Double) {
        println("Double $d")
    }

    override fun putChar(c: Char) {
        println("Char $c")
    }

    override fun putNull(isNull: Boolean) {
        println("Null $isNull")
    }

    override fun putUnit(unit: Unit) {
        println("Unit")
    }

    override fun putString(string: String) {
        println("String $string")
    }

    override fun beginNested() {
        println("Begin nested")
    }

    override fun endNested() {
        println("End nested")
    }
}

object PrintLabelSink : LabelSink {
    override fun putBoolean(label: String, b: Boolean) {
        println("Boolean $label=$b")
    }

    override fun putByte(label: String, b: Byte) {
        println("Byte $label=$b")
    }

    override fun putShort(label: String, s: Short) {
        println("Short $label=$s")
    }

    override fun putInt(label: String, i: Int) {
        println("Int $label=$i")
    }

    override fun putLong(label: String, l: Long) {
        println("Long $label=$l")
    }

    override fun putFloat(label: String, f: Float) {
        println("Float $label=$f")
    }

    override fun putDouble(label: String, d: Double) {
        println("Double $label=$d")
    }

    override fun putChar(label: String, c: Char) {
        println("Char $label=$c")
    }

    override fun putNull(label: String, isNull: Boolean) {
        println("Null $label: $isNull")
    }

    override fun putUnit(label: String, unit: Unit) {
        println("Unit $label")
    }

    override fun putString(label: String, string: String) {
        println("Boolean $label=$string")
    }

    override fun beginNested(label: String) {
        println("Begin nested $label")
    }

    override fun endNested(label: String) {
        println("End nested $label")
    }
}