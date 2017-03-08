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
package com.gmail.socraticphoenix.jource.ast.annotation;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.JavaSourceTyped;
import com.gmail.socraticphoenix.jource.ast.JavaSourceWritable;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceGenerics;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceNull;
import com.gmail.socraticphoenix.mirror.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaSourceAnnotation implements JavaSourceTyped, JavaSourceWritable {
    private Class<? extends Annotation> bounds;
    private JavaSourceNamespace type;
    private Map<String, JavaSourceAnnotationValue> values;

    public JavaSourceAnnotation(String name) {
        Class<?> c = Reflections.resolveClass(name).orElse(null);
        if (c != null && !Annotation.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException(name + " is not an Annotation");
        }
        this.bounds = (Class<? extends Annotation>) c;
        this.values = new LinkedHashMap<>();
        this.type = JavaSourceNamespace.of(name, JavaSourceGenerics.empty());
    }

    public JavaSourceAnnotation(Class<? extends Annotation> clazz) {
        this.bounds = clazz;
        this.values = new LinkedHashMap<>();
        this.type = JavaSourceNamespace.of(clazz);
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.add(this.type);
        this.values.values().stream().map(JavaSourceAnnotationValue::type).forEach(associated::add);
        return associated;
    }

    public JavaSourceAnnotation set(String name, JavaSourceAnnotationValue value) {
        if (value.type().equals(this.type)) {
            throw new IllegalArgumentException("Cannot set of annotation to that annotation");
        } else if (this.bounds != null) {
            try {
                Method method = this.bounds.getMethod(name);
                JavaSourceNamespace type = JavaSourceNamespace.of(method.getReturnType());
                if (!type.equals(value.type())) {
                    throw new IllegalArgumentException("Bounds are non-null and require: " + type.getQualifiedName() + " for value: " + name + ", not: " + value.type().getQualifiedName());
                }
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Bounds are non-null and do not have a method called: " + name);
            }
        }

        if (value instanceof JavaSourceNull) {
            throw new IllegalArgumentException("null values are disallowed in annotations");
        }

        this.values.put(name, value);
        return this;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("@").append(context.nameOf(this.type));
        if (!this.values.isEmpty()) {
            builder.append("(");
            List<Map.Entry<String, JavaSourceAnnotationValue>> toWrite = this.values.entrySet().stream().collect(Collectors.toList());
            for (int i = 0; i < toWrite.size(); i++) {
                Map.Entry<String, JavaSourceAnnotationValue> entry = toWrite.get(i);
                builder.append(entry.getKey()).append(" = ").append(entry.getValue().writeConstant(context));
                if (i < toWrite.size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

    @Override
    public boolean usesIndents() {
        return false;
    }
}
