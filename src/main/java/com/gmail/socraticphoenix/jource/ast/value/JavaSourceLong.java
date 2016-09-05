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
import com.gmail.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.Collections;
import java.util.List;

public class JavaSourceLong implements JavaSourceLiteral<Long> {
    private Long val;

    public JavaSourceLong(Long val) {
        this.val = val;
    }

    public static JavaSourceLong of(Long val) {
        return new JavaSourceLong(val);
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return String.valueOf(this.val) + "L";
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.of(long.class);
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return Collections.singletonList(this.type());
    }

    @Override
    public Long value() {
        return this.val;
    }

}