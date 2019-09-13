package io.github.socraticphoenix.jource.ast.value;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;

import java.util.ArrayList;
import java.util.List;

public class JavaSourceArrayGet implements JavaSourceValue {
    private JavaSourceValue array;
    private JavaSourceValue index;

    public JavaSourceArrayGet(JavaSourceValue array, JavaSourceValue index) {
        this.array = array;
        this.index = index;
    }

    public static JavaSourceArrayGet of(JavaSourceValue array, JavaSourceValue index) {
        return new JavaSourceArrayGet(array, index);
    }

    @Override
    public JavaSourceNamespace type() {
        return this.array.type().array(this.array.type().getArray() - 1);
    }

    @Override
    public List<JavaSourceNamespace> associatedTypes() {
        List<JavaSourceNamespace> types = new ArrayList<>();
        types.addAll(this.array.associatedTypes());
        types.addAll(this.index.associatedTypes());
        return types;
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        return this.array.write(indent + 1, context) + "[" + this.index.write(indent + 1, context) + "]";
    }

}
