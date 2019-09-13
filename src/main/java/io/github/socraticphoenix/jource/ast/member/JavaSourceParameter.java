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
package io.github.socraticphoenix.jource.ast.member;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.JavaSourceTyped;
import io.github.socraticphoenix.jource.ast.JavaSourceWritable;
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotatable;
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotation;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifiable;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifier;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JavaSourceParameter implements JavaSourceAnnotatable<JavaSourceParameter>, JavaSourceWritable, JavaSourceTyped, JavaSourceModifiable<JavaSourceParameter> {
    private List<JavaSourceAnnotation> annotations;
    private Set<JavaSourceModifier> modifiers;
    private JavaSourceNamespace type;
    private String name;
    private boolean isVarArgs;

    public JavaSourceParameter(JavaSourceNamespace type, String name, boolean isVarArgs) {
        this.type = type;
        this.name = name;
        this.annotations = new ArrayList<>();
        this.modifiers = new LinkedHashSet<>();
        this.isVarArgs = isVarArgs;

        if (type.getArray() == 0 && isVarArgs) {
            throw new IllegalArgumentException("Variable argument parameter must have array type");
        }
    }

    public static JavaSourceParameter of(JavaSourceNamespace type, String name, boolean isVarArgs) {
        return new JavaSourceParameter(type, name, isVarArgs);
    }

    public static JavaSourceParameter of(JavaSourceNamespace type, String name) {
        return of(type, name, false);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        this.annotations().forEach(annotation -> builder.append(annotation.write(indent + 1, context)).append(" "));
        this.modifiers.forEach(modifier -> builder.append(modifier.getName()).append(" "));
        builder.append(this.isVarArgs? this.type.array(this.type.getArray() - 1).write(indent + 1, context) : this.type.write(indent + 1, context)).append(this.isVarArgs ? "..." : "").append(" ").append(this.name);
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

    @Override
    public Collection<JavaSourceAnnotation> annotations() {
        return this.annotations;
    }

    @Override
    public JavaSourceParameter addAnnotation(JavaSourceAnnotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    @Override
    public Collection<JavaSourceModifier> modifiers() {
        return this.modifiers;
    }

    @Override
    public JavaSourceParameter addModifierImpl(JavaSourceModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }
}
