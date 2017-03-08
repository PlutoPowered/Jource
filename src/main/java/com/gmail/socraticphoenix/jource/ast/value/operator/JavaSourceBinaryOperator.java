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
package com.gmail.socraticphoenix.jource.ast.value.operator;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceStaticReference;
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceValue;

import java.util.List;

public class JavaSourceBinaryOperator implements JavaSourceValue {
    private JavaSourceValue left;
    private JavaSourceOperator operator;
    private JavaSourceValue right;

    public JavaSourceBinaryOperator(JavaSourceValue left, JavaSourceOperator operator, JavaSourceValue right) {
        if(operator != JavaSourceOperator.EMPTY && operator.getOperands() != 2) {
            throw new IllegalArgumentException("Operator does not accept two operands");
        } else if (operator == JavaSourceOperator.INSTANCEOF && !(right instanceof JavaSourceStaticReference)) {
            throw new IllegalArgumentException("The right value of an instanceof must be a static reference");
        }
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public static JavaSourceBinaryOperator of(JavaSourceValue left, JavaSourceOperator operator, JavaSourceValue right) {
        return new JavaSourceBinaryOperator(left, operator, right);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return "(" + this.left.write(indent + 1, context) + " " + this.operator.getRep() + " " + this.right.write(indent + 1, context) + ")";
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = this.left.associatedTypes();
        associated.addAll(this.right.associatedTypes());
        return associated;
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.UNKNOWN;
    }
}
