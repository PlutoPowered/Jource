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

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.JavaSourceTyped;
import com.gmail.socraticphoenix.jource.ast.JavaSourceWritable;
import com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifiable;
import com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifier;
import com.gmail.socraticphoenix.jource.ast.statement.JavaSourceStatement;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JavaSourceVariableDeclaration implements JavaSourceTyped, JavaSourceStatement, JavaSourceWritable, JavaSourceModifiable<JavaSourceVariableDeclaration> {
    private JavaSourceNamespace.Filled type;
    private String name;
    private JavaSourceValue value;

    private Set<JavaSourceModifier> modifiers;

    public JavaSourceVariableDeclaration(JavaSourceNamespace.Filled type, String name, JavaSourceValue value) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.modifiers = new LinkedHashSet<>();
    }

    public static JavaSourceVariableDeclaration of(JavaSourceNamespace.Filled type, String name, JavaSourceValue value) {
        return new JavaSourceVariableDeclaration(type, name, value);
    }

    public static JavaSourceVariableDeclaration of(String name, JavaSourceValue value) {
        return JavaSourceVariableDeclaration.of(value.type().fill(), name, value);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        this.modifiers.forEach(mod -> builder.append(mod.getName()).append(" "));
        return builder.append(this.type.write(indent + 1, context)).append(" ").append(this.name).append(" = ").append(this.value.write(indent + 1, context)).toString();
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.add(this.type);
        associated.addAll(this.value.associatedTypes());
        return associated;
    }

    @Override
    public Collection<JavaSourceModifier> modifiers() {
        return this.modifiers;
    }

    @Override
    public JavaSourceVariableDeclaration addModifierImpl(JavaSourceModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    @Override
    public boolean requiresSemiColon() {
        return true;
    }
}

