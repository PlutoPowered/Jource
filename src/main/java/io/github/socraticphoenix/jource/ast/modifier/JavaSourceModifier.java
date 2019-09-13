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
package io.github.socraticphoenix.jource.ast.modifier;

public enum JavaSourceModifier {
    ABSTRACT("abstract", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.METHOD),
    FINAL("final", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.METHOD, JavaSourceModifierTarget.FIELD, JavaSourceModifierTarget.LOCAL_VARIABLE),
    NATIVE("native", true, JavaSourceModifierTarget.METHOD),

    PRIVATE("private", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.FIELD, JavaSourceModifierTarget.METHOD, JavaSourceModifierTarget.CONSTRUCTOR),
    PROTECTED("protected", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.FIELD, JavaSourceModifierTarget.METHOD, JavaSourceModifierTarget.CONSTRUCTOR),
    PUBLIC("public", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.FIELD, JavaSourceModifierTarget.METHOD, JavaSourceModifierTarget.CONSTRUCTOR),

    STRICTFP("strictfp", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.METHOD),
    STATIC("static", JavaSourceModifierTarget.TYPE, JavaSourceModifierTarget.FIELD, JavaSourceModifierTarget.METHOD),
    SYNCHRONIZED("synchronized", JavaSourceModifierTarget.METHOD),
    TRANSIENT("transient", JavaSourceModifierTarget.FIELD),
    VOLATILE("volatile", JavaSourceModifierTarget.FIELD),
    DEFAULT("default", JavaSourceModifierTarget.METHOD);

    private String name;
    private JavaSourceModifierTarget[] allowed;
    private boolean negatesBody;

    JavaSourceModifier(String name, JavaSourceModifierTarget... allowed) {
        this(name, false, allowed);
    }

    JavaSourceModifier(String name, boolean negatesBody, JavaSourceModifierTarget... allowed) {
        this.name = name;
        this.allowed = allowed;
        this.negatesBody = negatesBody;
    }

    public boolean negatesBody() {
        return this.negatesBody;
    }

    public String getName() {
        return this.name;
    }

    public boolean canModify(Object sourceObject) {
        for(JavaSourceModifierTarget target : this.allowed) {
            if(target.canModify(sourceObject)) {
                return true;
            }
        }
        return false;
    }

}
