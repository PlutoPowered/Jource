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

public class JavaSourceTernaryOperator implements JavaSourceValue {
    private JavaSourceValue bool;
    private JavaSourceValue trueCase;
    private JavaSourceValue falseCase;

    public JavaSourceTernaryOperator(JavaSourceValue bool, JavaSourceValue trueCase, JavaSourceValue falseCase) {
        this.bool = bool;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }

    public static JavaSourceTernaryOperator of(JavaSourceValue bool, JavaSourceValue trueCase, JavaSourceValue falseCase) {
        return new JavaSourceTernaryOperator(bool, trueCase, falseCase);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return "(" + this.bool.write(indent + 1, context) + " ? " + this.trueCase.write(indent + 1, context) + " : " + this.falseCase.write(indent + 1, context) + ")";
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.addAll(this.bool.associatedTypes());
        associated.addAll(this.trueCase.associatedTypes());
        associated.addAll(this.falseCase.associatedTypes());
        return associated;
    }

    @Override
    public JavaSourceNamespace type() {
        return null;
    }
}
