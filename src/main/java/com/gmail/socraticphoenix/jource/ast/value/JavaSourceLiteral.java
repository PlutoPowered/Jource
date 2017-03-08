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
package com.gmail.socraticphoenix.jource.ast.value;

import com.gmail.socraticphoenix.jource.ast.annotation.JavaSourceAnnotationValue;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.lang.reflect.Array;

public interface JavaSourceLiteral<T> extends JavaSourceValue, JavaSourceAnnotationValue {

    static JavaSourceLiteral of(Object value) {
        if (value instanceof String) {
            return new JavaSourceString((String) value);
        } else if (value instanceof Byte) {
            return new JavaSourceByte((Byte) value);
        } else if (value instanceof Character) {
            return new JavaSourceChar((Character) value);
        } else if (value instanceof Short) {
            return new JavaSourceShort((Short) value);
        } else if (value instanceof Integer) {
            return new JavaSourceInt((Integer) value);
        } else if (value instanceof Long) {
            return new JavaSourceLong((Long) value);
        } else if (value instanceof Float) {
            return new JavaSourceFloat((Float) value);
        } else if (value instanceof Double) {
            return new JavaSourceDouble((Double) value);
        } else if (value instanceof Boolean) {
            return new JavaSourceBoolean((Boolean) value);
        } else if (value instanceof Class) {
            return new JavaSourceClassReference(JavaSourceNamespace.of((Class) value));
        } else if (value.getClass().isArray()) {
            JavaSourceLiteralArray array = new JavaSourceLiteralArray(JavaSourceNamespace.of(value.getClass().getComponentType()));
            for (int i = 0; i < Array.getLength(value); i++) {
                array.addElement(JavaSourceLiteral.of(Array.get(value, i)));
            }
            return array;
        } else {
            throw new IllegalArgumentException("Unknown type: " + value.getClass().getName());
        }
    }

    static JavaSourceLiteral of(String... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(Class... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(byte... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(char... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(short... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(int... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(long... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(float... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(double... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    static JavaSourceLiteral of(boolean... array) {
        return JavaSourceLiteral.of((Object) array);
    }

    T value();

}
