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
package com.gmail.socraticphoenix.jource.ast.type;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.JavaSourceTyped;
import com.gmail.socraticphoenix.jource.ast.JavaSourceWritable;
import com.gmail.socraticphoenix.jource.ast.annotation.JavaSourceAnnotatable;
import com.gmail.socraticphoenix.jource.ast.annotation.JavaSourceAnnotation;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceLiteral;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaSourceGenerics implements JavaSourceWritable, JavaSourceTyped {
    private Map<String, Element> mappings;

    public JavaSourceGenerics() {
        this.mappings = new LinkedHashMap<>();
    }

    public static JavaSourceGenerics empty() {
        return new JavaSourceGenerics();
    }

    public static JavaSourceGenerics of(Class clazz) {
        return JavaSourceGenerics.ofRec(clazz, new HashMap<>());
    }

    public static JavaSourceGenerics ofRec(Class clazz, Map<Class, JavaSourceGenerics> cache) {
        if (cache.containsKey(clazz)) {
            return cache.get(clazz);
        } else {
            JavaSourceGenerics generics = new JavaSourceGenerics();
            cache.put(clazz, generics);
            Stream.of(clazz.getTypeParameters()).forEach(var -> {
                String name = var.getName();
                List<JavaSourceNamespace.Filled> bounds = Stream.of(var.getBounds()).map(t -> JavaSourceGenerics.of(t, cache)).collect(Collectors.toList());
                generics.set(name, bounds);
                Element element = generics.get(name).get();
                for(Annotation annotation : var.getAnnotations()) {
                    JavaSourceAnnotation jsAnnotation = new JavaSourceAnnotation(annotation.annotationType());
                    Method[] toFill = annotation.annotationType().getDeclaredMethods();
                    for(Method m : toFill) {
                        try {
                            jsAnnotation.set(m.getName(), JavaSourceLiteral.of(m.invoke(annotation)));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                    element.addAnnotation(jsAnnotation);
                }
            });
            return generics;
        }
    }

    public static JavaSourceNamespace.Filled of(Type type, Map<Class, JavaSourceGenerics> cache) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Type raw = ptype.getRawType();
            if (raw instanceof Class) {
                return JavaSourceNamespace.of((Class) raw, cache).fill(JavaSourceFilledGenerics.ofRec(ptype, cache));
            } else {
                throw new IllegalArgumentException("Unknown raw type: " + raw.getClass());
            }
        } else if (type instanceof Class) {
            return JavaSourceNamespace.of((Class) type, cache).fill(JavaSourceFilledGenerics.empty());
        } else if (type instanceof WildcardType) {
            WildcardType wtype = (WildcardType) type;
            List<JavaSourceNamespace.Filled> upper = Stream.of(wtype.getUpperBounds()).map(t -> JavaSourceGenerics.of(t, cache)).collect(Collectors.toList());
            List<JavaSourceNamespace.Filled> lower = Stream.of(wtype.getLowerBounds()).map(t -> JavaSourceGenerics.of(t, cache)).collect(Collectors.toList());
            return new JavaSourceNamespace.Wildcard(upper, lower);
        } else if (type instanceof TypeVariable) {
            TypeVariable variable = (TypeVariable) type;
            return JavaSourceNamespace.of(variable.getName(), JavaSourceGenerics.empty(), false).fill();
        } else {
            throw new IllegalArgumentException("Unknown type: " + type.getClass());
        }
    }

    public JavaSourceGenerics set(String varName, List<JavaSourceNamespace.Filled> bounds) {
        bounds = bounds == null || bounds.isEmpty() ? Collections.singletonList(JavaSourceNamespace.of(Object.class).fill()) : bounds;
        this.mappings.put(varName, Element.of(varName, bounds));
        return this;
    }

    public Optional<Element> get(String name) {
        return Optional.ofNullable(this.mappings.get(name));
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        if (!this.mappings.isEmpty()) {
            builder.append("<");
            List<Map.Entry<String, Element>> values = this.mappings.entrySet().stream().collect(Collectors.toList());
            for (int i = 0; i < values.size(); i++) {
                Map.Entry<String, Element> entry = values.get(i);
                builder.append(entry.getValue().write(indent + 1, context));
                if (i < values.size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append(">");
        }
        return builder.toString();
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

    public boolean isEmpty() {
        return this.mappings.isEmpty();
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return null;
    }

    private static class Element implements JavaSourceAnnotatable, JavaSourceWritable, JavaSourceTyped {
        private List<JavaSourceNamespace.Filled> bounds;
        private List<JavaSourceAnnotation> annotations;
        private String name;

        public Element(String name, List<JavaSourceNamespace.Filled> bounds) {
            this.bounds = bounds.stream().collect(Collectors.toList());
            this.annotations = new ArrayList<>();
            this.name = name;
        }

        public static Element of(String name, List<JavaSourceNamespace.Filled> bounds) {
            return new Element(name, bounds);
        }

        @Override
        public Collection<JavaSourceAnnotation> annotations() {
            return this.annotations;
        }

        @Override
        public Element addAnnotation(JavaSourceAnnotation annotation) {
            this.annotations.add(annotation);
            return this;
        }

        @Override
        public boolean hasAnnotation(JavaSourceAnnotation annotation) {
            return this.annotations.contains(annotation);
        }

        @Override
        public String write(int indent, JavaSourceContext context) {
            StringBuilder builder = new StringBuilder();
            this.annotations.forEach(annotation -> builder.append(annotation.write(indent + 1, context)).append(" "));
            builder.append(this.name);
            if (!this.bounds.isEmpty() && (this.bounds.size() != 1 || this.bounds.get(0).getRepresented().orElse(Class.class) != Object.class)) {
                builder.append(" extends ");
                for (int i = 0; i < this.bounds.size(); i++) {
                    builder.append(this.bounds.get(i).write(indent + 1, context));
                    if (i < this.bounds.size() - 1) {
                        builder.append(" & ");
                    }
                }
            }
            return builder.toString();
        }

        @Override
        public boolean usesIndents() {
            return false;
        }

        @Override
        public List<JavaSourceNamespace> associatedTypes() {
            return null;
        }
    }
}
