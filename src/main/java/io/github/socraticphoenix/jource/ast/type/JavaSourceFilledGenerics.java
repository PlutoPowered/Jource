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
package io.github.socraticphoenix.jource.ast.type;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.JavaSourceTyped;
import io.github.socraticphoenix.jource.ast.JavaSourceWritable;
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotatable;
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotation;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JavaSourceFilledGenerics implements JavaSourceWritable, JavaSourceTyped {
    private List<Element> filled;

    public JavaSourceFilledGenerics() {
        this.filled = new ArrayList<>();
    }

    public List<Element> getFilled() {
        return this.filled;
    }

    public JavaSourceFilledGenerics addElement(Element element) {
        this.filled.add(element);
        return this;
    }

    public static JavaSourceFilledGenerics empty() {
        return new JavaSourceFilledGenerics();
    }

    public static JavaSourceFilledGenerics of(ParameterizedType type) {
        return JavaSourceFilledGenerics.ofRec(type, new HashMap<>());
    }

    public static JavaSourceFilledGenerics ofRec(ParameterizedType type, Map<Class, JavaSourceGenerics> cache) {
        JavaSourceFilledGenerics generics = new JavaSourceFilledGenerics();
        Stream.of(type.getActualTypeArguments()).forEach(t -> generics.getFilled().add(Element.of(JavaSourceGenerics.of(t, cache))));
        return generics;
    }


    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        if(!this.filled.isEmpty()) {
            builder.append("<");
            for (int i = 0; i < this.filled.size(); i++) {
                Element element = this.filled.get(i);
                builder.append(element.write(indent + 1, context));
                if (i < this.filled.size() - 1) {
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

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        this.filled.forEach(element -> associated.addAll(element.associatedTypes()));
        return associated;
    }

    public static class Element implements JavaSourceAnnotatable, JavaSourceWritable, JavaSourceTyped {
        private JavaSourceNamespace.Filled type;
        private List<JavaSourceAnnotation> annotations;

        public static Element of(JavaSourceNamespace.Filled type) {
            return new Element(type);
        }

        public Element(JavaSourceNamespace.Filled type) {
            this.type = type;
            this.annotations = new ArrayList<>();
        }

        public JavaSourceNamespace.Filled type() {
            return this.type;
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
            builder.append(this.type.write(indent + 1, context));
            return builder.toString();
        }

        @Override
        public boolean usesIndents() {
            return false;
        }

        @Override
        public List<JavaSourceNamespace> associatedTypes() {
            List<JavaSourceNamespace> associated = new ArrayList<>();
            associated.add(this.type);
            this.annotations.forEach(annotation -> associated.addAll(annotation.associatedTypes()));
            return associated;
        }
    }
}
