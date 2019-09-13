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
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotatable;
import io.github.socraticphoenix.jource.ast.annotation.JavaSourceAnnotation;
import io.github.socraticphoenix.jource.ast.block.AbstractJavaSourceTopBlock;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifiable;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifier;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.parse.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JavaSourceConstructor  extends AbstractJavaSourceTopBlock<JavaSourceConstructor> implements JavaSourceTyped, JavaSourceModifiable<JavaSourceConstructor>, JavaSourceAnnotatable<JavaSourceConstructor> {
    private List<JavaSourceAnnotation> annotations;
    private Set<JavaSourceModifier> modifiers;
    private JavaSourceNamespace type;
    private List<JavaSourceParameter> parameters;

    public JavaSourceConstructor(JavaSourceNamespace type) {
        this.type = type;
        this.modifiers = new LinkedHashSet<>();
        this.parameters = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public Collection<JavaSourceParameter> parameters() {
        return this.parameters;
    }

    public JavaSourceConstructor addParameter(JavaSourceParameter param) {
        this.parameters.add(param);
        return this;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ls = System.lineSeparator();
        this.annotations.forEach(annotation -> builder.append(annotation.write(indent + 1, context)).append(ls).append(ind));
        this.modifiers.forEach(modifier -> builder.append(modifier.getName()).append(" "));
        builder.append(context.nameOf(this.type)).append("(");
        for (int i = 0; i < this.parameters.size(); i++) {
            builder.append(this.parameters.get(i).write(indent + 1, context));
            if(i < this.parameters.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return this.writeStatements(indent, context, builder.toString(), "");
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.add(this.type);
        this.parameters.forEach(param -> associated.addAll(param.associatedTypes()));
        this.annotations.forEach(annotation -> associated.addAll(annotation.associatedTypes()));
        associated.addAll(super.associatedTypes());
        return associated;
    }

    @Override
    public Collection<JavaSourceModifier> modifiers() {
        return this.modifiers;
    }

    @Override
    public JavaSourceConstructor addModifierImpl(JavaSourceModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    @Override
    public Collection<JavaSourceAnnotation> annotations() {
        return this.annotations;
    }

    @Override
    public JavaSourceConstructor addAnnotation(JavaSourceAnnotation annotation) {
        this.annotations.add(annotation);
        return this;
    }
}