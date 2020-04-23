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

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.member.JavaSourceConstructor;
import io.github.socraticphoenix.jource.ast.member.JavaSourceField;
import io.github.socraticphoenix.jource.ast.member.JavaSourceInitializer;
import io.github.socraticphoenix.jource.ast.member.JavaSourceMethod;
import io.github.socraticphoenix.jource.ast.member.JavaSourceStaticInitializer;
import io.github.socraticphoenix.jource.ast.modifier.JavaSourceModifier;
import io.github.socraticphoenix.jource.ast.type.JavaSourceGenerics;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.parse.Strings;

import java.util.Collection;

public class JavaSourceInterfaceDefinition extends AbstractJavaSourceDefinition<JavaSourceInterfaceDefinition> {

    public JavaSourceInterfaceDefinition(String name, JavaSourceGenerics generics) {
        super(name, generics);
    }

    @Override
    public Collection<JavaSourceMethod> methods() {
        return super.methods();
    }

    @Override
    public JavaSourceInterfaceDefinition addMethod(JavaSourceMethod method) {
        if(method.modifiers().contains(JavaSourceModifier.PRIVATE) || method.modifiers().contains(JavaSourceModifier.PROTECTED) || method.modifiers().contains(JavaSourceModifier.FINAL) || method.modifiers().contains(JavaSourceModifier.NATIVE)) {
            throw new IllegalArgumentException("Method contains illegal modifiers");
        } else if (!method.modifiers().contains(JavaSourceModifier.DEFAULT) && !method.modifiers().contains(JavaSourceModifier.ABSTRACT)) {
            method.addModifier(JavaSourceModifier.ABSTRACT);
        }
        return super.addMethod(method);
    }

    @Override
    public Collection<JavaSourceField> fields() {
        return super.fields();
    }

    @Override
    public JavaSourceInterfaceDefinition addField(JavaSourceField field) {
        if(!field.modifiers().contains(JavaSourceModifier.FINAL) || !field.modifiers().contains(JavaSourceModifier.STATIC) || !field.modifiers().contains(JavaSourceModifier.PUBLIC)) {
            throw new IllegalArgumentException("Interface fields must be public static final");
        }
        return super.addField(field);
    }

    @Override
    public Collection<JavaSourceConstructor> constructors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaSourceInterfaceDefinition addConstructor(JavaSourceConstructor constructor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<JavaSourceStaticInitializer> staticInitializers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaSourceInterfaceDefinition addStatInitializer(JavaSourceStaticInitializer staticInitializer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<JavaSourceInitializer> initializers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaSourceInterfaceDefinition addInitializer(JavaSourceInitializer initializer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaSourceNamespace.Filled superclass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaSourceInterfaceDefinition setSuperclass(JavaSourceNamespace.Filled superclass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        JavaSourceContext defContext = JavaSourceContext.of(context, JavaSourceDefinitionType.INTERFACE);

        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ind2 = Strings.indent(indent + 1);
        String ls = System.lineSeparator();

        this.annotations().forEach(annotation -> builder.append(annotation.write(indent + 1, defContext)).append(ls).append(ind));
        this.modifiers().forEach(modifier -> builder.append(modifier.getName()).append(" "));
        builder.append("interface ").append(this.name().getSimpleName()).append(" ");
        if(!this.interfaces().isEmpty()) {
            builder.append("extends ");
            int c = 0;
            for(JavaSourceNamespace face : this.interfaces()) {
                builder.append(face.write(indent + 1, defContext));
                if(c < this.interfaces().size() - 1) {
                    builder.append(", ");
                }
                c++;
            }
            builder.append(" ");
        }
        builder.append("{").append(ls);
        this.fields().forEach(field -> builder.append(ind2).append(field.write(indent + 1, defContext)).append(ls));
        builder.append(ls);
        this.methods().forEach(method -> builder.append(ind2).append(method.write(indent + 1, defContext)).append(ls).append(ls));
        this.inners().forEach(inner -> builder.append(ind2).append(inner.write(indent + 1, defContext)).append(ls).append(ls));
        builder.append(ind).append("}");
        return builder.toString();
    }

}
