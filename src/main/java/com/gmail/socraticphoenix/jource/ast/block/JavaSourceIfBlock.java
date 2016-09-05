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

public class JavaSourceIfBlock extends AbstractJavaSourceBlock<JavaSourceIfBlock> {
    private JavaSourceValue bool;
    private boolean isElseIf;

    public JavaSourceIfBlock(JavaSourceValue bool, boolean isElseIf) {
        this.bool = bool;
        this.isElseIf = isElseIf;
    }

    public static JavaSourceIfBlock ifBlock(JavaSourceValue bool) {
        return new JavaSourceIfBlock(bool, false);
    }

    public static JavaSourceIfBlock elseIfBlock(JavaSourceValue bool) {
        return new JavaSourceIfBlock(bool, true);
    }

    public static JavaSourceIfBlock elseBlock() {
        return new JavaSourceIfBlock(null, true);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        if(this.bool == null) {
            return this.writeStatements(indent, context, "else", "");
        } else if (this.isElseIf) {
            return this.writeStatements(indent, context, "else if (" + this.bool.write(indent + 1, context) + ")", "");
        } else {
            return this.writeStatements(indent, context, "if (" + this.bool.write(indent + 1, context) + ")", "");
        }
    }

}
