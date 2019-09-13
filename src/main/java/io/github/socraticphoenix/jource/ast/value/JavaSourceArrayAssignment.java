package io.github.socraticphoenix.jource.ast.value;

import io.github.socraticphoenix.jource.ast.JavaSourceContext;
import io.github.socraticphoenix.jource.ast.type.JavaSourceNamespace;
import io.github.socraticphoenix.jource.ast.value.operator.JavaSourceOperator;

import java.util.ArrayList;
import java.util.List;

public class JavaSourceArrayAssignment implements JavaSourceValue {
    private JavaSourceValue array;
    private JavaSourceValue index;
    private JavaSourceOperator operator;
    private JavaSourceValue value;

    public JavaSourceArrayAssignment(JavaSourceValue array, JavaSourceValue index, JavaSourceOperator operator, JavaSourceValue value) {
        this.array = array;
        this.index = index;
        this.operator = operator;
        this.value = value;
        if(operator.isLogical()) {
            throw new IllegalArgumentException("Operator cannot be logical");
        } else if (operator != JavaSourceOperator.EMPTY && operator.getOperands() != 2) {
            throw new IllegalArgumentException("Operator does not accept two operands");
        }
    }

    public static JavaSourceArrayAssignment of(JavaSourceValue array, JavaSourceValue index, JavaSourceOperator operator, JavaSourceValue value) {
        return new JavaSourceArrayAssignment(array, index, operator, value);
    }

    public static JavaSourceArrayAssignment of(JavaSourceValue array, JavaSourceValue index, JavaSourceValue value) {
        return of(array, index, JavaSourceOperator.EMPTY, value);
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
        return this.array.write(indent + 1, context) + "[" + this.index.write(indent + 1, context) + "] " + this.operator.getRep() + " = " + this.value.write(indent + 1, context);
    }

}