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
package io.github.socraticphoenix.jource.ast.definition;

import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotation;
import io.github.socraticphoenix.jource.ast.member.JavaSourceConstructor;
import io.github.socraticphoenix.jource.ast.member.JavaSourceField;
import io.github.socraticphoenix.jource.ast.member.JavaSourceInitializer;
import io.github.socraticphoenix.jource.ast.member.JavaSourceMethod;
import io.github.socraticphoenix.jource.ast.member.JavaSourceStaticInitializer;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifier;
import io.github.socraticphoenix.jource.ast.type.JavaSourceGenerics;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractJavaSourceDefinition<T extends AbstractJavaSourceDefinition<T>> implements JavaSourceDefinition<T> {
    private List<JavaSourceAnnotation> annotations;
    private Set<JavaSourceNamespace.Filled> interfaces;
    private JavaSourceNamespace.Filled superclass;
    private List<JavaSourceDefinition> innerClasses;
    private List<JavaSourceStaticInitializer> staticInitializers;
    private List<JavaSourceInitializer> initializers;
    private List<JavaSourceConstructor> constructors;
    private List<JavaSourceMethod> methods;
    private List<JavaSourceField> fields;
    private Set<JavaSourceModifier> modifiers;
    private JavaSourceNamespace name;

    public AbstractJavaSourceDefinition(String name, JavaSourceGenerics generics) {
        this(JavaSourceNamespace.of(name, generics));
    }

    public AbstractJavaSourceDefinition(JavaSourceNamespace name) {
        this.name = name;
        this.annotations = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.staticInitializers = new ArrayList<>();
        this.initializers = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.interfaces = new LinkedHashSet<>();
        this.modifiers = new LinkedHashSet<>();
        this.superclass = JavaSourceNamespace.of(Object.class).fill();
    }

    @Override
    public boolean usesIndents() {
        return true;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.add(this.superclass);
        associated.addAll(this.interfaces);
        this.initializers.forEach(v -> associated.addAll(v.associatedTypes()));
        this.constructors.forEach(v -> associated.addAll(v.associatedTypes()));
        this.annotations.forEach(v -> associated.addAll(v.associatedTypes()));
        this.innerClasses.forEach(v -> associated.addAll(v.associatedTypes()));
        this.methods.forEach(v -> associated.addAll(v.associatedTypes()));
        this.fields.forEach(v -> associated.addAll(v.associatedTypes()));
        return associated;
    }

    @Override
    public Collection<JavaSourceAnnotation> annotations() {
        return this.annotations;
    }

    @Override
    public T addAnnotation(JavaSourceAnnotation annotation) {
        return (T) this;
    }

    @Override
    public Collection<JavaSourceDefinition> inners() {
        return this.innerClasses;
    }

    @Override
    public T addInner(JavaSourceDefinition definition) {
        this.innerClasses.add(definition);
        return (T) this;
    }

    @Override
    public Collection<JavaSourceMethod> methods() {
        return this.methods;
    }

    @Override
    public T addMethod(JavaSourceMethod method) {
        this.methods.add(method);
        return (T) this;
    }

    @Override
    public Collection<JavaSourceField> fields() {
        return this.fields;
    }

    @Override
    public T addField(JavaSourceField field) {
        this.fields.add(field);
        return (T) this;
    }

    @Override
    public Collection<JavaSourceConstructor> constructors() {
        return this.constructors;
    }

    @Override
    public T addConstructor(JavaSourceConstructor constructor) {
        this.constructors.add(constructor);
        return (T) this;
    }

    @Override
    public Collection<JavaSourceStaticInitializer> staticInitializers() {
        return this.staticInitializers;
    }

    @Override
    public T addStatInitializer(JavaSourceStaticInitializer staticInitializer) {
        this.staticInitializers.add(staticInitializer);
        return (T) this;
    }

    @Override
    public Collection<JavaSourceInitializer> initializers() {
        return this.initializers;
    }

    @Override
    public T addInitializer(JavaSourceInitializer initializer) {
        this.initializers.add(initializer);
        return (T) this;
    }

    @Override
    public Collection<JavaSourceNamespace.Filled> interfaces() {
        return this.interfaces;
    }

    @Override
    public T addInterface(JavaSourceNamespace.Filled face) {
        this.interfaces.add(face);
        return (T) this;
    }

    @Override
    public JavaSourceNamespace.Filled superclass() {
        return this.superclass;
    }

    @Override
    public T setSuperclass(JavaSourceNamespace.Filled superclass) {
        this.superclass = superclass;
        return (T) this;
    }

    @Override
    public JavaSourceNamespace name() {
        return this.name;
    }

    @Override
    public Collection<JavaSourceModifier> modifiers() {
        return this.modifiers;
    }

    @Override
    public T addModifierImpl(JavaSourceModifier modifier) {
        this.modifiers.add(modifier);
        return (T) this;
    }
}
