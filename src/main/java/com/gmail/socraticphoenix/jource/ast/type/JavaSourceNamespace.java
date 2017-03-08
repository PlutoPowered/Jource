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
package com.gmail.socraticphoenix.jource.ast.type;

import com.gmail.socraticphoenix.jource.ast.JavaSourceContext;
import com.gmail.socraticphoenix.jource.ast.JavaSourceWritable;
import com.gmail.socraticphoenix.mirror.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaSourceNamespace implements JavaSourceWritable {
    public static final JavaSourceNamespace UNKNOWN = JavaSourceNamespace.of("{unknown}", JavaSourceGenerics.empty(), 0, false);
    public static final JavaSourceNamespace.Filled VOID = JavaSourceNamespace.of("void", JavaSourceGenerics.empty(), 0, false).fill();

    private String[] path;
    private String[] pack;
    private String[] name;
    private String packStatement;
    private String importStatement;
    private String qualifiedName;
    private String simpleName;
    private String src;


    protected int array;
    private int innerNesting;
    private JavaSourceGenerics generics;
    private Class clazz;

    private JavaSourceNamespace(String name, int array, int innerNesting, Class clazz, JavaSourceGenerics generics) {
        this.src = name;
        this.path = name.split(Pattern.quote("."));
        this.array = array;
        this.clazz = clazz;
        this.generics = generics;
        this.innerNesting = innerNesting;

        if(path.length <= innerNesting) {
            throw new IllegalArgumentException("Inner nesting outweigh path length");
        }

        this.name = new String[this.innerNesting + 1];
        for (int i = this.path.length - (this.innerNesting + 1), c = 0; i < this.path.length; i++, c++) {
            this.name[c] = this.path[i];
        }

        this.pack = new String[this.path.length - (this.innerNesting + 1)];
        for (int i = 0; i < this.pack.length; i++) {
            this.pack[i] = this.path[i];
        }


        StringBuilder qn = new StringBuilder();
        StringBuilder sn = new StringBuilder();
        StringBuilder ps = new StringBuilder();
        StringBuilder is = new StringBuilder();

        for (int i = 0; i < this.path.length; i++) {
            qn.append(this.path[i]);
            if(i < this.path.length - 1) {
                qn.append(".");
            }
        }

        for (int i = 0; i < this.name.length; i++) {
            sn.append(this.name[i]);
            if(i < this.name.length - 1) {
                sn.append(".");
            }
        }

        ps.append("package ");
        is.append("import ");
        for (int i = 0; i < this.pack.length; i++) {
            ps.append(this.pack[i]);
            is.append(this.pack[i]);
            is.append(".");
            if(i < this.pack.length - 1) {
                ps.append(".");
            }
        }

        is.append(this.name[0]);

        this.qualifiedName = qn.toString();
        this.simpleName = sn.toString();
        this.packStatement = ps.toString();
        this.importStatement = is.toString();
    }

    public static JavaSourceNamespace of(Class clazz) {
        return JavaSourceNamespace.of(clazz, new HashMap<>());
    }

    public static JavaSourceNamespace of(Class clazz, Map<Class, JavaSourceGenerics> cache) {
        int array = 0;
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            array++;
        }

        int innerNesting = 0;
        Class copy = clazz;
        while (copy.getDeclaringClass() != null) {
            copy = copy.getDeclaringClass();
            innerNesting++;
        }

        return new JavaSourceNamespace(clazz.getCanonicalName(), array, innerNesting, clazz, JavaSourceGenerics.ofRec(clazz, cache));
    }

    public static JavaSourceNamespace of(String s, JavaSourceGenerics generics) {
        return JavaSourceNamespace.of(s, generics, true);
    }

    public static JavaSourceNamespace of(String s, JavaSourceGenerics generics, boolean resolveClass) {
        return JavaSourceNamespace.of(s, generics, 0, resolveClass);
    }

    public static JavaSourceNamespace of(String s, JavaSourceGenerics generics, int innerNesting, boolean resolveClass) {
        int array = 0;
        if (s.startsWith("[")) {
            while (s.startsWith("[")) {
                s = s.replaceFirst("\\[", "");
                array++;
            }
            if (!s.equals("") && s.startsWith("L")) {
                s = s.replaceFirst("L", "");
            }

            if (!s.equals("") && s.endsWith(";")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return new JavaSourceNamespace(s, array, innerNesting, resolveClass ? Reflections.resolveClass(s).orElse(null) : null, generics);
    }

    public Optional<Class> getRepresented() {
        return Optional.ofNullable(this.clazz);
    }

    public String[] getPath() {
        return this.path.clone();
    }

    public String[] getPackage() {
        return this.pack.clone();
    }

    public String[] getName() {
        return this.name.clone();
    }

    public String getQualifiedName() {
        return this.qualifiedName;
    }

    public String getSimpleName() {
        return this.simpleName;
    }

    public boolean isPrimitive() {
        return this.getRepresented().isPresent() && this.getRepresented().get().isPrimitive();
    }

    public boolean sharesPackage(JavaSourceNamespace other) {
        return Arrays.equals(this.pack, other.pack);
    }

    public boolean sharesImport(JavaSourceNamespace other) {
        return Arrays.equals(this.pack, other.pack) && other.name[0].equals(this.name[0]);
    }

    public boolean sharesSimpleName(JavaSourceNamespace other) {
        return Arrays.equals(this.name, other.name);
    }

    public int getArray() {
        return this.array;
    }


    public String toImportStatement() {
        return this.importStatement;
    }

    public String toPackageStatement() {
        return this.packStatement;
    }

    public Filled fill(JavaSourceFilledGenerics generics) {
        return new Filled(this.src, this.array, this.innerNesting, this.clazz, this.generics, generics);
    }

    public Filled fill() {
        return this.fill(JavaSourceFilledGenerics.empty());
    }

    public JavaSourceNamespace array(int array) {
        return new JavaSourceNamespace(this.src, array, this.innerNesting, this.clazz, this.generics);
    }

    @Override
    public String toString() {
        return this.qualifiedName + "[" + this.array + "]";
    }

    @Override
    public int hashCode() {
        return (Arrays.hashCode(this.path) * 31 + this.array) * 31 + this.innerNesting;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof JavaSourceNamespace) {
            return Arrays.equals(this.path, ((JavaSourceNamespace) other).path) && this.array == ((JavaSourceNamespace) other).array && this.innerNesting == ((JavaSourceNamespace) other).innerNesting;
        } else {
            return false;
        }
    }

    @Override
    public String write(int indent, JavaSourceContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.nameOf(this));
        builder.append(this.generics.write(indent + 1, context));
        for (int i = 0; i < this.array; i++) {
            builder.append("[]");
        }
        return builder.toString();
    }

    @Override
    public boolean usesIndents() {
        return false;
    }

    public static class Wildcard extends JavaSourceNamespace.Filled {
        private List<Filled> upper;
        private List<Filled> lower;

        public Wildcard(List<Filled> upper, List<Filled> lower) {
            super("?", 0, 0, null, JavaSourceGenerics.empty(), JavaSourceFilledGenerics.empty());
            if(upper.size() == 1 && upper.get(0).getRepresented().orElse(Class.class) == Object.class) {
                upper = new ArrayList<>();
            }

            if(lower.size() == 1 && lower.get(0).getRepresented().orElse(Class.class) == Object.class) {
                lower = new ArrayList<>();
            }

            this.upper = upper.stream().collect(Collectors.toList());
            this.lower = lower.stream().collect(Collectors.toList());
        }

        public List<Filled> getUpper() {
            return this.upper.stream().collect(Collectors.toList());
        }

        public List<Filled> getLower() {
            return this.lower.stream().collect(Collectors.toList());
        }

        @Override
        public String write(int indent, JavaSourceContext context) {
            StringBuilder builder = new StringBuilder();
            builder.append("?");
            if (!this.upper.isEmpty()) {
                builder.append(" extends ");
                for (int i = 0; i < this.upper.size(); i++) {
                    builder.append(this.upper.get(i).write(indent + 1, context));
                    if (i < this.upper.size() - 1) {
                        builder.append(" & ");
                    }
                }
            } else if (!this.lower.isEmpty()) {
                builder.append(" super ");
                for (int i = 0; i < this.lower.size(); i++) {
                    builder.append(this.lower.get(i).write(indent + 1, context));
                    if (i < this.lower.size() - 1) {
                        builder.append(" & ");
                    }
                }
            }
            return builder.toString();
        }

    }

    public static class Filled extends JavaSourceNamespace {
        private JavaSourceFilledGenerics filledGenerics;

        private Filled(String name, int array, int innnerNesting, Class clazz, JavaSourceGenerics generics, JavaSourceFilledGenerics filledGenerics) {
            super(name, array, innnerNesting, clazz, generics);
            this.filledGenerics = filledGenerics;
        }

        public JavaSourceFilledGenerics getFilledGenerics() {
            return this.filledGenerics;
        }

        @Override
        public String write(int indent, JavaSourceContext context) {
            StringBuilder builder = new StringBuilder();
            builder.append(context.nameOf(this));
            builder.append(this.filledGenerics.write(indent + 1, context));
            for (int i = 0; i < this.array; i++) {
                builder.append("[]");
            }
            return builder.toString();
        }

    }
}
