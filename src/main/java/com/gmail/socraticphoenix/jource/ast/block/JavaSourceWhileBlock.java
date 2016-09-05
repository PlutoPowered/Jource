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
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceValue;
import com.gmail.socraticphoenix.jource.util.Utils;

public class JavaSourceWhileBlock extends AbstractJavaSourceBlock<JavaSourceWhileBlock> {
    private String name;
    private JavaSourceValue bool;
    private boolean isDo;

    public JavaSourceWhileBlock(String name, JavaSourceValue bool, boolean isDo) {
        this.name = name;
        this.bool = bool;
        this.isDo = isDo;
    }

    public static JavaSourceWhileBlock whileBlock(String name, JavaSourceValue bool) {
        return new JavaSourceWhileBlock(name, bool, false);
    }

    public static JavaSourceWhileBlock whileBlock(JavaSourceValue bool) {
        return JavaSourceWhileBlock.whileBlock("", bool);
    }

    public static JavaSourceWhileBlock doWhileBlock(String name, JavaSourceValue bool) {
        return new JavaSourceWhileBlock(name, bool, true);
    }

    public static JavaSourceWhileBlock doWhileBlock(JavaSourceValue bool) {
        return JavaSourceWhileBlock.doWhileBlock("", bool);
    }

    @Override
    public boolean requiresSemiColon() {
        return this.isDo;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder header = new StringBuilder();
        if(!this.name.equals("")) {
            header.append(this.name).append(":").append(System.lineSeparator()).append(Utils.indent(indent));
        }

        if(this.isDo) {
            return this.writeStatements(indent, context, header.append("do").toString(), "while(" + this.bool.write(indent + 1, context) + ")");
        } else {
            return this.writeStatements(indent, context, header.append("while(").append(this.bool.write(indent + 1, context)).append(")").toString(), "");
        }
    }
}
