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
import com.gmail.socraticphoenix.jource.ast.value.JavaSourceValue;

import java.util.ArrayList;
import java.util.List;

public class JavaSourceOperatorList implements JavaSourceValue {
    private List<JavaSourceValue> values;
    private List<JavaSourceOperator> operators;

    public JavaSourceOperatorList() {
        this.values = new ArrayList<>();
        this.operators = new ArrayList<>();
    }

    public static JavaSourceOperatorList of() {
        return new JavaSourceOperatorList();
    }

    public static JavaSourceOperatorList glued(JavaSourceOperator glue, JavaSourceValue... values) {
        if(glue.getOperands() != 2) {
            throw new IllegalArgumentException("Operator does not accept two operands");
        }

        JavaSourceOperatorList list = new JavaSourceOperatorList();
        for (int i = 0; i < values.length; i++) {
            list.values.add(values[i]);
            if(i < values.length - 1) {
                list.operators.add(glue);
            }
        }
        return list;
    }

    public JavaSourceOperatorList start(JavaSourceValue left, JavaSourceOperator operator, JavaSourceValue right) {
        if(operator.getOperands() != 2) {
            throw new IllegalArgumentException("Operator does not accept two operands");
        }

        if(this.values.size() != 0 || this.operators.size() != 0) {
            throw new IllegalStateException("Cannot start more than once");
        }

        this.values.add(left);
        this.values.add(right);
        this.operators.add(operator);
        return this;
    }

    public JavaSourceOperatorList next(JavaSourceOperator operator, JavaSourceValue next) {
        if(operator.getOperands() != 2) {
            throw new IllegalArgumentException("Operator does not accept two operands");
        }

        this.values.add(next);
        this.operators.add(operator);
        return this;
    }

    public JavaSourceOperatorList n(JavaSourceOperator operator, JavaSourceValue next) {
        return this.next(operator, next);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        if(this.values.size() > 0) {
            builder.append("(");
            builder.append(this.values.get(0).write(indent + 1, context));
            for (int i = 1; i < this.values.size(); i++) {
                builder.append(" ").append(this.operators.get(i - 1).getRep()).append(" ").append(this.values.get(i).write(indent + 1, context));
            }
            builder.append(")");
        }
        return builder.toString();
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        this.values.forEach(value -> associated.addAll(value.associatedTypes()));
        return associated;
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.UNKNOWN;
    }

}
