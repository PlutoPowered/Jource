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
package com.gmail.socraticphoenix.jource.ast.value;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.statement.JavaSourceStatement;
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import com.gmail.socraticphoenix.jource.ast.value.operator.JavaSourceOperator;

import java.util.List;

public class JavaSourceVariableAssignment implements JavaSourceValue, JavaSourceStatement {
    private JavaSourceOperator operator;
    private String name;
    private JavaSourceValue value;

    public JavaSourceVariableAssignment(String name, JavaSourceOperator operator, JavaSourceValue value) {
        this.name = name;
        this.value = value;
        if(operator.isLogical()) {
            throw new IllegalArgumentException("Operator cannot be logical");
        } else if (operator != JavaSourceOperator.EMPTY && operator.getOperands() != 2) {
            throw new IllegalArgumentException("Operator does not accept two operands");
        }
    }

    public static JavaSourceVariableAssignment of(String name, JavaSourceValue value) {
        return new JavaSourceVariableAssignment(name, JavaSourceOperator.EMPTY, value);
    }

    public static JavaSourceVariableAssignment of(String name, JavaSourceOperator operator, JavaSourceValue value) {
        return new JavaSourceVariableAssignment(name, operator, value);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return this.name + " " + this.operator.getRep() + "= " + this.value.write(indent + 1, context);
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return this.value.associatedTypes();
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.UNKNOWN;
    }

    @Override
    public boolean requiresSemiColon() {
        return true;
    }
}
