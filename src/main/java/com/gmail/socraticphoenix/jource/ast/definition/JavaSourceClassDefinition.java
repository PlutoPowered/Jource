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
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceGenerics;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.parse.Strings;

public class JavaSourceClassDefinition extends AbstractJavaSourceDefinition<JavaSourceClassDefinition> {

    public JavaSourceClassDefinition(String name, JavaSourceGenerics generics) {
        super(name, generics);
    }

    public JavaSourceClassDefinition(JavaSourceNamespace name) {
        super(name);
    }

    public static JavaSourceClassDefinition of(JavaSourceNamespace name) {
        return new JavaSourceClassDefinition(name);
    }

    public static JavaSourceClassDefinition of(String name, JavaSourceGenerics generics) {
        return new JavaSourceClassDefinition(name, generics);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ind2 = Strings.indent(indent + 1);
        String ls = System.lineSeparator();

        this.annotations().forEach(annotation -> builder.append(annotation.write(indent + 1, context)).append(ls).append(ind));
        this.modifiers().forEach(modifier -> builder.append(modifier.getName()).append(" "));
        builder.append("class ").append(this.name().write(indent + 1, context)).append(" extends ").append(this.superclass().write(indent + 1, context)).append(" ");
        if(!this.interfaces().isEmpty()) {
            builder.append("implements ");
            int c = 0;
            for(JavaSourceNamespace face : this.interfaces()) {
                builder.append(face.write(indent + 1, context));
                if(c < this.interfaces().size() - 1) {
                    builder.append(", ");
                }
                c++;
            }
            builder.append(" ");
        }
        builder.append("{").append(ls);
        this.fields().forEach(field -> builder.append(ind2).append(field.write(indent + 1, context)).append(ls));
        builder.append(ls);
        this.staticInitializers().forEach(staticInitializer -> builder.append(ind2).append(staticInitializer.write(indent + 1, context)).append(ls));
        if(!this.staticInitializers().isEmpty()) {
            builder.append(ls);
        }
        this.initializers().forEach(initializer -> builder.append(ind2).append(initializer.write(indent + 1, context)).append(ls));
        if(!this.initializers().isEmpty()) {
            builder.append(ls);
        }
        this.constructors().forEach(constructor -> builder.append(ind2).append(constructor.write(indent + 1, context)).append(ls).append(ls));
        this.methods().forEach(method -> builder.append(ind2).append(method.write(indent + 1, context)).append(ls).append(ls));
        this.inners().forEach(inner -> builder.append(ind2).append(inner.write(indent + 1, context)).append(ls).append(ls));
        builder.append(ind).append("}");
        return builder.toString();
    }

}
