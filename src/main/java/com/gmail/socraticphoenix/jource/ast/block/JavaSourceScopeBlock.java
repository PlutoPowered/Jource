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
package com.gmail.socraticphoenix.jource.ast.block;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.statement.JavaSourceStatement;
import com.gmail.socraticphoenix.parse.Strings;

public class JavaSourceScopeBlock extends AbstractJavaSourceBlock<JavaSourceScopeBlock> {
    private boolean opensScope;

    public JavaSourceScopeBlock(boolean opensScope) {
        this.opensScope = opensScope;
    }

    public static JavaSourceScopeBlock of(boolean opensScope) {
        return new JavaSourceScopeBlock(opensScope);
    }

    public static JavaSourceScopeBlock of() {
        return JavaSourceScopeBlock.of(true);
    }

    public boolean opensScope() {
        return this.opensScope;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ind2 = Strings.indent(indent + 1);
        String ls = System.lineSeparator();
        if (!this.opensScope) {
            if(this.statements().size() > 0) {
                JavaSourceStatement initial = this.statements().get(0);
                builder.append(initial.write(indent + 1, context)).append(initial.requiresSemiColon() ? ";" : "").append(this.statements().size() > 1 ? ls : "");
                for (int i = 1; i < this.statements().size(); i++) {
                    JavaSourceStatement statement = this.statements().get(i);
                    builder.append(ind).append(statement.write(indent + 1, context)).append(statement.requiresSemiColon() ? ";" : "");
                    if(i < this.statements().size() - 1) {
                        builder.append(ls);
                    }
                }
            }
        } else {
            builder.append("{").append(ls);
            this.statements().forEach(statement -> builder.append(ind2).append(statement.write(indent + 1, context)).append(statement.requiresSemiColon() ? ";" : "").append(ls));
            builder.append(ind).append("}");
        }
        return builder.toString();
    }

}
