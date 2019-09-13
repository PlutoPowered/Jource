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

import io.github.socraticphoenix.jource.ast.definition.AbstractJavaSourceDefinition;
import io.github.socraticphoenix.jource.ast.member.JavaSourceConstructor;
import io.github.socraticphoenix.jource.ast.member.JavaSourceField;
import io.github.socraticphoenix.jource.ast.member.JavaSourceMethod;
import io.github.socraticphoenix.jource.ast.member.JavaSourceParameter;
import io.github.socraticphoenix.jource.ast.value.JavaSourceVariableDeclaration;

public enum  JavaSourceModifierTarget {
    TYPE(AbstractJavaSourceDefinition.class),
    METHOD(JavaSourceMethod.class),
    CONSTRUCTOR(JavaSourceConstructor.class),
    FIELD(JavaSourceField.class),
    LOCAL_VARIABLE(JavaSourceVariableDeclaration.class, JavaSourceParameter.class);

    private Class[] allowed;

    JavaSourceModifierTarget(Class<? extends JavaSourceModifiable>... allowed) {
        this.allowed = allowed;
    }

    public boolean canModify(Object sourceObject) {
        for(Class c : this.allowed) {
            if(c.isInstance(sourceObject)) {
                return true;
            }
        }

        return false;
    }
}
