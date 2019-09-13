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
package io.github.socraticphoenix.jource.ast.block;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.statement.JavaSourceStatement;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import io.github.socraticphoenix.jource.ast.value.JavaSourceValue;
import com.gmail.socraticphoenix.parse.Strings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JavaSourceSwitchBlock implements JavaSourceStatement {
    private String name;
    private JavaSourceValue target;
    private Map<JavaSourceValue, JavaSourceScopeBlock> cases;
    private JavaSourceScopeBlock defaultCase;

    public JavaSourceSwitchBlock(JavaSourceValue target, String name) {
        this.target = target;
        this.cases = new LinkedHashMap<>();
        this.defaultCase = null;
    }

    public static JavaSourceSwitchBlock of(JavaSourceValue target, String name) {
        return new JavaSourceSwitchBlock(target, name);
    }

    public static JavaSourceSwitchBlock of(JavaSourceValue target) {
        return JavaSourceSwitchBlock.of(target, null);
    }

    public JavaSourceSwitchBlock defaultCase(JavaSourceScopeBlock defaultCase) {
        this.defaultCase = defaultCase;
        return this;
    }

    public JavaSourceSwitchBlock caseBlock(JavaSourceValue target, JavaSourceScopeBlock result) {
        this.cases.put(target, result);
        return this;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        String ind = Strings.indent(indent);
        String ind2 = Strings.indent(indent + 1);
        String ind3 = Strings.indent(indent + 2);
        String ls = System.lineSeparator();
        if(this.name != null) {
            builder.append(this.name).append(":").append(ls).append(ind);
        }
        builder.append("switch (").append(this.target.write(indent + 1, context)).append(") {").append(ls);
        this.cases.entrySet().forEach(entry -> {
            builder.append(ind2).append("case (").append(entry.getKey().write(indent + 1, context)).append("): ").append(entry.getValue().opensScope() ? "" : ls + ind3).append(entry.getValue().write(indent + (entry.getValue().opensScope() ? 1 : 2), context)).append(ls);
        });
        if(this.defaultCase != null) {
            builder.append(ind2).append("default: ").append(this.defaultCase.opensScope() ? "" : ls + ind3).append(this.defaultCase.write(indent + (this.defaultCase.opensScope() ? 1 : 2), context)).append(ls);
        }
        builder.append(ind).append("}");
        return builder.toString();
    }

    @Override
    public boolean usesIndents() {
        return true;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.addAll(this.target.associatedTypes());
        associated.addAll(this.defaultCase.associatedTypes());
        this.cases.entrySet().forEach(entry -> {
            associated.addAll(entry.getKey().associatedTypes());
            associated.addAll(entry.getValue().associatedTypes());
        });
        return associated;
    }

    @Override
    public boolean requiresSemiColon() {
        return false;
    }
}
