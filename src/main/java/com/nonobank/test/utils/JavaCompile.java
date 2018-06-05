package com.nonobank.test.utils;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/5/16.
 */
public class JavaCompile {
    JavaCompiler compiler;
    StandardJavaFileManager stdManager;

    public JavaCompile() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    public Map<String, byte[]> compile(String filename, String source) throws IOException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(filename, source);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            Boolean result = task.call();
            if (result == null || !result.booleanValue()) {
                throw new RuntimeException("Compilation failed");
            }
            return manager.getClassBytes();
        }
    }

    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            return classLoader.loadClass(name);
        }
    }

    public static void main(String[] args) throws Exception {
        String classStr = "package com.nonobank.test.utils;\n" +
                "public class User implements U {\n" +
                "\n" +
                "\tprivate String id;\n" +
                "\tprivate String name;\n" +
                "\tprivate long created;\n" +
                "\n" +
                "\tpublic String getId() {\n" +
                "\t\treturn id;\n" +
                "\t}\n" +
                "\n" +
                "\tpublic void setId(String id) {\n" +
                "\t\tthis.id = id;\n" +
                "\t}\n" +
                "\n" +
                "\tpublic String getName() {\n" +
                "\t\treturn name;\n" +
                "\t}\n" +
                "\n" +
                "\tpublic void setName(String name) {\n" +
                "\t\tthis.name = name;\n" +
                "\t}\n" +
                "\n" +
                "\tpublic long getCreated() {\n" +
                "\t\treturn created;\n" +
                "\t}\n" +
                "\n" +
                "\tpublic void setCreated(long created) {\n" +
                "\t\tthis.created = created;\n" +
                "\t}\n" +
                "\n" +
                "}";

        JavaCompile compile = new JavaCompile();
        Map<String, byte[]> result = compile.compile("User.java", classStr);
        Class<?> clazz = compile.loadClass("com.nonobank.test.utils.User", result);
        U obj = (U) clazz.newInstance();
        Method setId = clazz.getMethod("setId", String.class);
        setId.invoke(obj, "test");
        System.out.println(obj.getId());
    }
}


class MemoryClassLoader extends URLClassLoader {

    Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

    public MemoryClassLoader(Map<String, byte[]> classBytes) {
        super(new URL[0], MemoryClassLoader.class.getClassLoader());
        this.classBytes.putAll(classBytes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
            return super.findClass(name);
        }
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
    }

}


class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    final Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

    MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public Map<String, byte[]> getClassBytes() {
        return new HashMap<String, byte[]>(this.classBytes);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        classBytes.clear();
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new MemoryOutputJavaFileObject(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    JavaFileObject makeStringSource(String name, String code) {
        return new MemoryInputJavaFileObject(name, code);
    }

    static class MemoryInputJavaFileObject extends SimpleJavaFileObject {

        final String code;

        MemoryInputJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }
    }

    class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
        final String name;

        MemoryOutputJavaFileObject(String name) {
            super(URI.create("string:///" + name), Kind.CLASS);
            this.name = name;
        }

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    classBytes.put(name, bos.toByteArray());
                }
            };
        }

    }
}