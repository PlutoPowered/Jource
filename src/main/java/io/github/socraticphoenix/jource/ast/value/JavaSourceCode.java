package io.github.socraticphoenix.jource.ast.value;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.Collections;
import java.util.List;

public class JavaSourceCode implements JavaSourceLiteral<String> {
    private String string;

    public JavaSourceCode(String string) {
        this.string = string;
    }

    @Override
    public String value() {
        return this.string;
    }

    @Override
    public JavaSourceNamespace type() {
        return JavaSourceNamespace.UNKNOWN;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return Collections.emptyList();
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return this.string;
    }
}
