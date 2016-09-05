/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.jource.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class Utils {
    private static Map<String, Class> primitives;
    private static Map<Class, Class> wrappers;

    static {
        primitives = new HashMap<>();
        primitives.put("int", int.class);
        primitives.put("long", long.class);
        primitives.put("byte", byte.class);
        primitives.put("short", short.class);

        primitives.put("double", double.class);
        primitives.put("float", float.class);

        primitives.put("boolean", boolean.class);
        primitives.put("char", char.class);
        primitives.put("void", void.class);

        wrappers = new HashMap<>();
        wrappers.put(int.class, Integer.class);
        wrappers.put(long.class, Long.class);
        wrappers.put(byte.class, Byte.class);
        wrappers.put(short.class, Short.class);

        wrappers.put(double.class, Double.class);
        wrappers.put(float.class, Float.class);

        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(char.class, Character.class);
        wrappers.put(void.class, Void.class);
    }

    public static Optional<Class> resolveClass(String name) {
        if(Utils.primitives.containsKey(name)) {
            return Optional.of(Utils.primitives.get(name));
        } else {
            try {
                return Optional.of(Class.forName(name));
            } catch (ClassNotFoundException e) {
                return Optional.empty();
            }
        }
    }

    public static Class getPromoted(Class clazz) {
        if(wrappers.containsKey(clazz)) {
            return wrappers.get(clazz);
        } else {
            return clazz;
        }
    }

    public static String indent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }

    public static String escape(String string) {
        if (string == null) {
            return "";
        }

        return string.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll(Pattern.quote(String.valueOf('"')), "\\\\\"")
                .replaceAll(Pattern.quote(String.valueOf('\'')), "\\\\\'")
                .replaceAll(Pattern.quote("\0"), "\\\\0")
                .replaceAll(Pattern.quote("\b"), "\\\\b")
                .replaceAll(Pattern.quote("\f"), "\\\\f")
                .replaceAll(Pattern.quote("\n"), "\\\\n")
                .replaceAll(Pattern.quote("\r"), "\\\\r")
                .replaceAll(Pattern.quote("\t"), "\\\\t");
    }

}
