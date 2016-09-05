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
package com.gmail.socraticphoenix.jource.ast.modifier;

import static com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifierTarget.CONSTRUCTOR;
import static com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifierTarget.FIELD;
import static com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifierTarget.LOCAL_VARIABLE;
import static com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifierTarget.METHOD;
import static com.gmail.socraticphoenix.jource.ast.modifier.JavaSourceModifierTarget.TYPE;

public enum JavaSourceModifier {
    ABSTRACT("abstract", TYPE, METHOD),
    FINAL("final", TYPE, METHOD, FIELD, LOCAL_VARIABLE),
    NATIVE("native", true, METHOD),

    PRIVATE("private", TYPE, FIELD, METHOD, CONSTRUCTOR),
    PROTECTED("protected", TYPE, FIELD, METHOD, CONSTRUCTOR),
    PUBLIC("public", TYPE, FIELD, METHOD, CONSTRUCTOR),

    STRICTFP("strictfp", TYPE, METHOD),
    STATIC("static", TYPE, FIELD, METHOD),
    SYNCHRONIZED("synchronized", METHOD),
    TRANSIENT("transient", FIELD),
    VOLATILE("volatile", FIELD),
    DEFAULT("default", METHOD);

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
