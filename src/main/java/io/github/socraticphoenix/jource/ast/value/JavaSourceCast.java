package io.github.socraticphoenix.jource.ast.value;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.List;

public class JavaSourceCast implements JavaSourceValue {
    private JavaSourceNamespace target;
    private JavaSourceValue value;

    public JavaSourceCast(JavaSourceNamespace target, JavaSourceValue value) {
        this.target = target;
        this.value = value;
    }

    public static JavaSourceCast of(JavaSourceNamespace target, JavaSourceValue value) {
        return new JavaSourceCast(target, value);
    }

    @Override
    public JavaSourceNamespace type() {
        return this.type();
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> associated = new ArrayList<>();
        associated.add(this.target);
        associated.addAll(this.value.associatedTypes());
        return associated;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return "(" + context.nameOf(this.target) + ") " + this.value.write(indent + 1, context);
    }

}
