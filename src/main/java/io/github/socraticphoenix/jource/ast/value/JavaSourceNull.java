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
package io.github.socraticphoenix.jource.ast.value;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.type.JavaSourceGenerics;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.Collections;
import java.util.List;

public class JavaSourceNull implements JavaSourceLiteral<Object> {

    public static JavaSourceLiteral<?> getNullOf(JavaSourceNamespace namespace) {
        if(namespace.getRepresented().isPresent()) {
            return JavaSourceNull.getNullOf(namespace.getRepresented().get());
        } else {
            return new JavaSourceNull();
        }
    }

    public static JavaSourceLiteral<?> getNullOf(Class clazz) {
        if(clazz.isPrimitive()) {
            if(clazz == byte.class) {
                return new JavaSourceByte((byte) 0);
            } else if (clazz == char.class) {
                return new JavaSourceChar('\0');
            } else if (clazz == short.class) {
                return new JavaSourceShort((short) 0);
            } else if (clazz == int.class) {
                return new JavaSourceInt(0);
            } else if (clazz == long.class) {
                return new JavaSourceLong(0L);
            } else if (clazz == float.class) {
                return new JavaSourceFloat(0F);
            } else if (clazz == double.class) {
                return new JavaSourceDouble(0D);
            } else if (clazz == boolean.class) {
                return new JavaSourceBoolean(false);
            } else {
                throw new IllegalArgumentException("Unrecognized primitive: " + clazz);
            }
        } else {
            return new JavaSourceNull();
        }
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return String.valueOf("null");
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.of("null", JavaSourceGenerics.empty());
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return Collections.emptyList();
    }

    @Override
    public Object value() {
        return null;
    }

}
