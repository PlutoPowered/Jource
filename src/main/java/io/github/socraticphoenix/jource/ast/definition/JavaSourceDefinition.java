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


import io.github.socraticphoenix.jource.ast.JavaSourceTyped;
import io.github.socraticphoenix.jource.ast.JavaSourceWritable;
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotatable;
import io.github.socraticphoenix.jource.ast.member.JavaSourceConstructor;
import io.github.socraticphoenix.jource.ast.member.JavaSourceField;
import io.github.socraticphoenix.jource.ast.member.JavaSourceInitializer;
import io.github.socraticphoenix.jource.ast.member.JavaSourceMethod;
import io.github.socraticphoenix.jource.ast.member.JavaSourceStaticInitializer;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifiable;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.Collection;

public interface JavaSourceDefinition<T extends JavaSourceDefinition<T>> extends JavaSourceWritable, JavaSourceAnnotatable, JavaSourceTyped, JavaSourceModifiable<T> {

    Collection<JavaSourceDefinition> inners();

    T addInner(JavaSourceDefinition definition);

    Collection<JavaSourceMethod> methods();

    T addMethod(JavaSourceMethod method);

    Collection<JavaSourceField> fields();

    T addField(JavaSourceField field);

    Collection<JavaSourceConstructor> constructors();

    T addConstructor(JavaSourceConstructor constructor);

    Collection<JavaSourceStaticInitializer> staticInitializers();

    T addStatInitializer(JavaSourceStaticInitializer staticInitializer);

    Collection<JavaSourceInitializer> initializers();

    T addInitializer(JavaSourceInitializer initializer);

    Collection<JavaSourceNamespace.Filled> interfaces();

    T addInterface(JavaSourceNamespace.Filled face);

    JavaSourceNamespace.Filled superclass();

    T setSuperclass(JavaSourceNamespace.Filled superclass);

    JavaSourceNamespace name();

}
