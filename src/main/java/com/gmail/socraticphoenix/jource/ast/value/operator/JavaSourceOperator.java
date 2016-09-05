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

public enum  JavaSourceOperator {
    ADD("+", 2),
    SUBTRACT("-", 2),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
    MODULO("%", 2),
    OR("|", 2),
    XOR("^", 2),
    AND("&", 2),
    LEFT_SHIFT("<<", 2),
    RIGHT_SHIFT(">>", 2),
    UNSIGNED_RIGHT_SHIFT(">>>", 2),
    BOOLEAN_OR("||", 2, false, true),
    BOOLEAN_AND("&&", 2, false, true),
    LESS_THAN("<", 2, false, true),
    GREATER_THAN(">", 2, false, true),
    LESS_THAN_OR_EQUAL("<=", 2, false, true),
    GREATER_THAN_OR_EQUAL(">=", 2, false, true),
    INSTANCEOF("instanceof", 2, false, true),
    EQUAL("==", 2, false, true),
    NOT_EQUAL("!=", 2, false, true),

    POST_INCREMENT("++", 1, true, false),
    POST_DECREMENT("--", 1, true, false),
    INCREMENT("++", 1),
    DECREMENT("--", 1),
    POSITIVE("+", 1),
    NEGATIVE("-", 1),
    NEGATE("!", 1),
    BITWISE_COMPLEMENT("~", 1),

    EMPTY("", 0)

    ;
    private int operands;
    private boolean postfix;
    private boolean logical;
    private String rep;

    JavaSourceOperator(String rep, int operands) {
        this(rep, operands, false, false);
    }

    JavaSourceOperator(String rep, int operands, boolean postfix, boolean logical) {
        this.operands = operands;
        this.rep = rep;
        this.postfix = postfix;
        this.logical = logical;
    }

    public boolean isLogical() {
        return this.logical;
    }

    public boolean isPostfix() {
        return this.postfix;
    }

    public int getOperands() {
        return this.operands;
    }

    public String getRep() {
        return this.rep;
    }
}
