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
package com.gmail.socraticphoenix.jource.ast.definition;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.member.JavaSourceField;
import com.gmail.socraticphoenix.jource.ast.member.JavaSourceMethod;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.jource.util.Utils;
import com.gmail.socraticphoenix.jource.ast.member.JavaSourceInitializer;
import com.gmail.socraticphoenix.jource.ast.member.JavaSourceParameter;
import com.gmail.socraticphoenix.jource.ast.member.JavaSourceStaticInitializer;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JavaSourceAnonymousClass implements JavaSourceValue {
    private List<JavaSourceDefinition> innerClasses;
    private List<JavaSourceStaticInitializer> staticInitializers;
    private List<JavaSourceInitializer> initializers;
    private List<JavaSourceMethod> methods;
    private List<JavaSourceField> fields;

    private List<JavaSourceParameter> parameters;

    private JavaSourceNamespace superclass;

    public JavaSourceAnonymousClass(JavaSourceNamespace superclass) {
        this.superclass = superclass;
        this.innerClasses = new ArrayList<>();
        this.staticInitializers = new ArrayList<>();
        this.initializers = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public Collection<JavaSourceDefinition> inners() {
        return this.innerClasses;
    }

    public JavaSourceAnonymousClass addInner(JavaSourceDefinition definition) {
        this.innerClasses.add(definition);
        return this;
    }

    public Collection<JavaSourceMethod> methods() {
        return this.methods;
    }

    public JavaSourceAnonymousClass addMethod(JavaSourceMethod method) {
        this.methods.add(method);
        return this;
    }

    public Collection<JavaSourceField> fields() {
        return this.fields;
    }

    public JavaSourceAnonymousClass addField(JavaSourceField field) {
        this.fields.add(field);
        return this;
    }


    public Collection<JavaSourceParameter> parameters() {
        return this.parameters;
    }


    public JavaSourceAnonymousClass addParameter(JavaSourceParameter parameter) {
        this.parameters.add(parameter);
        return this;
    }


    public Collection<JavaSourceStaticInitializer> staticInitializers() {
        return this.staticInitializers;
    }


    public JavaSourceAnonymousClass addStatInitializer(JavaSourceStaticInitializer staticInitializer) {
        this.staticInitializers.add(staticInitializer);
        return this;
    }


    public Collection<JavaSourceInitializer> initializers() {
        return this.initializers;
    }


    public JavaSourceAnonymousClass addInitializer(JavaSourceInitializer initializer) {
        this.initializers.add(initializer);
        return this;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        String ind = Utils.indent(indent);
        String ind2 = Utils.indent(indent + 1);
        String ls = System.lineSeparator();

        builder.append("new ").append(this.superclass.write(indent + 1, context)).append("(");
        for (int i = 0; i < this.parameters.size(); i++) {
            builder.append(this.parameters.get(i).write(indent + 1, context));
            if(i < this.parameters.size() - 1) {
                builder.append(", ");
            }
        }


        builder.append("){").append(ls);
        this.fields().forEach(field -> builder.append(ind2).append(field.write(indent + 1, context)).append(ls));
        builder.append(ls);
        this.staticInitializers().forEach(staticInitializer -> builder.append(ind2).append(staticInitializer.write(indent + 1, context)).append(ls));
        if (!this.staticInitializers().isEmpty()) {
            builder.append(ls);
        }
        this.initializers().forEach(initializer -> builder.append(ind2).append(initializer.write(indent + 1, context)).append(ls));
        if (!this.initializers().isEmpty()) {
            builder.append(ls);
        }
        this.methods().forEach(method -> builder.append(ind2).append(method.write(indent + 1, context)).append(ls).append(ls));
        this.inners().forEach(inner -> builder.append(ind2).append(inner.write(indent + 1, context)).append(ls).append(ls));
        builder.append(ind).append("}");
        return builder.toString();
    }

    @Override
    public JavaSourceNamespace type() {
        return null;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return null;
    }

}
