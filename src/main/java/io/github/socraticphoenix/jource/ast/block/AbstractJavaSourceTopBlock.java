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
package io.github.socraticphoenix.jource.ast.block;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.statement.JavaSourceStatement;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.parse.Strings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJavaSourceTopBlock<T extends AbstractJavaSourceTopBlock<T>> implements JavaSourceTopBlock<T> {
    private List<JavaSourceStatement> statements;

    public AbstractJavaSourceTopBlock() {
        this.statements = new ArrayList<>();
    }

    @Override
    public final boolean usesIndents() {
        return true;
    }

    @Override
    public List<JavaSourceStatement> statements() {
        return this.statements;
    }

    @Override
    public T addStatement(JavaSourceStatement statement) {
        this.statements.add(statement);
        return (T) this;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        this.statements.forEach(statement -> associated.addAll(statement.associatedTypes()));
        return associated;
    }

    public final String writeStatements(int indent, JavaSourceContext context, String header, String footer) {
        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ind2 = Strings.indent(indent + 1);
        String ls = System.lineSeparator();
        builder.append(header).append(!header.equals("") ? " " : "").append("{").append(ls);
        this.statements.forEach(statement -> builder.append(ind2).append(statement.write(indent + 1, context)).append(statement.requiresSemiColon() ? ";" : "").append(ls));
        builder.append(ind).append("}").append(!footer.equals("") ? " " : "").append(footer);
        return builder.toString();
    }

}
