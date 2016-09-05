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
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.List;

public class JavaSourceConstructorCall implements JavaSourceValue {
    private JavaSourceNamespace.Filled target;
    private List<JavaSourceValue> parameters;

    public JavaSourceConstructorCall(JavaSourceNamespace.Filled target) {
        this.target = target;
        this.parameters = new ArrayList<>();
    }

    public static JavaSourceConstructorCall of(JavaSourceNamespace.Filled target) {
        return new JavaSourceConstructorCall(target);
    }

    public JavaSourceConstructorCall addParameter(JavaSourceValue param) {
        this.parameters.add(param);
        return this;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("new ").append(this.target.write(indent + 1, context)).append("(");
        for (int i = 0; i < this.parameters.size(); i++) {
            builder.append(this.parameters.get(i).write(indent + 1, context));
            if(i < this.parameters.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.add(this.target);
        this.parameters.forEach(param -> associated.addAll(param.associatedTypes()));
        return associated;
    }

    @Override
    public JavaSourceNamespace type() {
        return this.target;
    }
}
