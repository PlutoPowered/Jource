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
import io.github.socraticphoenix.jource.ast.value.JavaSourceValue;

import java.util.Arrays;
import java.util.List;

public class JavaSourceForLoop extends AbstractJavaSourceBlock<JavaSourceForLoop> {
    private JavaSourceStatement initializer;
    private JavaSourceValue bool;
    private List<JavaSourceStatement> repeated;

    public JavaSourceForLoop(JavaSourceStatement initializer, JavaSourceValue bool, List<JavaSourceStatement> repeated) {
        this.initializer = initializer;
        this.bool = bool;
        this.repeated = repeated;
    }

    public static JavaSourceForLoop of(JavaSourceStatement initializer, JavaSourceValue bool, JavaSourceStatement... repeated) {
        return new JavaSourceForLoop(initializer, bool, Arrays.asList(repeated));
    }


    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder header = new StringBuilder();
        header.append("for (").append(this.initializer == null ? "" : this.initializer.write(indent + 1, context)).append("; ").append(this.bool == null ? "" : this.bool.write(indent + 1, context)).append("; ");
        if(this.repeated != null) {
            for (int i = 0; i < this.repeated.size(); i++) {
                header.append(this.repeated.get(i).write(indent + 1, context));
                if(i < this.repeated.size() - 1) {
                    header.append(", ");
                }
            }
        }
        return this.writeStatements(indent, context, header.toString(), "");
    }

}
