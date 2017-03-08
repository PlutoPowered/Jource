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
package com.gmail.socraticphoenix.jource.ast.member;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.JavaSourceTyped;
import com.gmail.socraticphoenix.jource.ast.JavaSourceWritable;
import com.gmail.socraticphoenix.jource.ast.annotation.JavaSourceAnnotatable;
import com.gmail.socraticphoenix.jource.ast.annotation.JavaSourceAnnotation;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceValue;
import com.gmail.socraticphoenix.parse.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JavaSourceEnumConstant implements JavaSourceTyped, JavaSourceWritable, JavaSourceAnnotatable<JavaSourceEnumConstant> {
    private String name;
    private List<JavaSourceValue> parameters;
    private List<JavaSourceAnnotation> annotations;

    public JavaSourceEnumConstant(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public static JavaSourceEnumConstant of(String name) {
        return new JavaSourceEnumConstant(name);
    }

    public JavaSourceEnumConstant addParameter(JavaSourceValue param) {
        this.parameters.add(param);
        return this;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ls = System.lineSeparator();
        this.annotations.forEach(annotation -> builder.append(annotation.write(indent + 1, context)).append(ls).append(ind));
        builder.append(this.name).append("(");
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
    public boolean usesIndents() {
        return true;
    }

    @Override
    public Collection<JavaSourceAnnotation> annotations() {
        return this.annotations;
    }

    @Override
    public JavaSourceEnumConstant addAnnotation(JavaSourceAnnotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        this.parameters.forEach(param -> associated.addAll(param.associatedTypes()));
        this.annotations.forEach(annotation -> associated.addAll(annotation.associatedTypes()));
        return associated;
    }
}
