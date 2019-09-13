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
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaSourceLiteralArray<T extends JavaSourceLiteral> implements JavaSourceLiteral<Object[]> {
    private List<T> literals;
    private JavaSourceNamespace type;

    public JavaSourceLiteralArray(JavaSourceNamespace component) {
        this.literals = new ArrayList<>();
        this.type = component.array(component.getArray() + 1);
    }

    public JavaSourceLiteralArray addElement(T element) {
        this.literals.add(element);
        return this;
    }

    public List<T> getLiterals() {
        return this.literals;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("new ").append(this.type.write(indent + 1, context)).append("{");
        for (int i = 0; i < this.literals.size(); i++) {
            builder.append(this.literals.get(i).write(indent + 1, context));
            if(i < this.literals.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String writeConstant(JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int i = 0; i < this.literals.size(); i++) {
            builder.append(this.literals.get(i).write(context));
            if(i < this.literals.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

    @Override
    public JavaSourceNamespace type() {
        return this.type;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return Collections.singletonList(this.type());
    }

    @Override
    public Object[] value() {
        return this.literals.toArray();
    }

}
