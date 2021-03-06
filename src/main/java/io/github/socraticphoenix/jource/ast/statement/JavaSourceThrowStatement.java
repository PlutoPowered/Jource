package io.github.socraticphoenix.jource.ast.statement;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import io.github.socraticphoenix.jource.ast.value.JavaSourceValue;

import java.util.List;

public class JavaSourceThrowStatement implements JavaSourceStatement {
    private JavaSourceValue toThrow;

    public JavaSourceThrowStatement(JavaSourceValue toThrow) {
        this.toThrow = toThrow;
    }

    public static JavaSourceThrowStatement of(JavaSourceValue value) {
        return new JavaSourceThrowStatement(value);
    }

    @Override
    public boolean requiresSemiColon() {
        return true;
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        return this.toThrow.associatedTypes();
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return "throw " + this.toThrow.write(indent + 1, context);
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

}
