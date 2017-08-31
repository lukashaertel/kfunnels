package eu.metatools.kfunnels.utils;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;

public final class RawEnums {
    private enum Placeholder {}

    @SuppressWarnings("unchecked")
    public static Object valueOf(KClass<?> in, String value) {
        Class<Placeholder> c = (Class<Placeholder>) JvmClassMappingKt.getJavaClass(in);
        return Enum.valueOf(c, value);
    }
}
