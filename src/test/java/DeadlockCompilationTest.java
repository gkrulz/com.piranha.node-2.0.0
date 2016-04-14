import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.piranha.node.compile.Compiler;
import com.piranha.node.compile.DependencyPool;
import com.piranha.node.compile.JavaSourceFromString;
import com.piranha.node.util.PiranhaConfig;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Padmaka on 4/14/16.
 */
public class DeadlockCompilationTest {

    public static void main(String[] args) {
        DeadlockCompilationTest compiler = null;

        compiler = new DeadlockCompilationTest();

        String classString01 = "package com.me;\n" +
                "import com.u.B;\n" +
                "public class A {\n" +
                "    public void doSomething(){\n" +
                "        B b = new B();\n" +
                "    }\n" +
                "}";

        String classString02 = "package com.u;\n" +
                "import com.me.A;\n" +
                "public class B {\n" +
                "    public void someTask(){\n" +
                "        A a = new A();\n" +
                "    }\n" +
                "}";

//        try {
//            compiler.compile("A", "B", classString01, classString02);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            compiler.compile("A", classString01);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compile(String className01, String className02, String classString01, String classString02) throws Exception {

        List<JavaSourceFromString> jsfsList = new LinkedList<>();

//        for (JsonElement element : classesToCompile) {
//
////            JsonObject classToCompile = element.getAsJsonObject();
////            StringBuilder packageName = new StringBuilder(classToCompile.get("package").getAsString());
////            StringBuilder classString = new StringBuilder("package " + packageName.replace(packageName.length() - 1, packageName.length(), "") + ";\n");
////
////            for (JsonElement importStatement : classToCompile.get("importStatements").getAsJsonArray()) {
////                classString.append("import " + importStatement.getAsString() + ";\n");
////            }
////            classString.append(classToCompile.get("classDeclaration").getAsString());
////            classString.append(classToCompile.get("classString").getAsString() + "}");
//
//            JavaSourceFromString jsfs = new JavaSourceFromString(classToCompile.get("className").getAsString(), classString.toString());
//            jsfsList.add(jsfs);
//
//        }

        JavaSourceFromString jsfs = new JavaSourceFromString(className01, classString01);
        jsfsList.add(jsfs);
        JavaSourceFromString jsfs02 = new JavaSourceFromString(className02, classString02);
        jsfsList.add(jsfs02);

        Iterable<? extends JavaFileObject> fileObjects = jsfsList;

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        if (jc == null) throw new Exception("Compiler unavailable");

        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add("/Users/Padmaka/Desktop/Test_compiler");
        options.add("-classpath");
        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        StringBuilder sb = new StringBuilder();
        for (URL url : urlClassLoader.getURLs()) {
            sb.append(url.getFile()).append(File.pathSeparator);
        }
        sb.append("/Users/Padmaka/Desktop/Test_compiler");
        options.add(sb.toString());

        StringWriter output = new StringWriter();
        boolean success = jc.getTask(output, null, null, options, null, fileObjects).call();
        if (success) {
//            ArrayList<String> compiledClasses = new ArrayList<>();
//            for (JsonElement element : classesToCompile) {
//                compiledClasses.add(element.getAsJsonObject().get("absoluteClassName").getAsString());
//            }
//            DependencyPool.getDependencyPool().addClasses(compiledClasses);
            System.out.printf(" has been successfully compiled");
        } else {
            throw new Exception("Compilation failed :" + output);
        }

    }

    public void compile(String className, String classString) throws Exception {

//        StringBuilder packageName = new StringBuilder(classToCompile.get("package").getAsString());
//        StringBuilder classString = new StringBuilder("package " + packageName.replace(packageName.length() - 1, packageName.length(), "") + ";\n");
//
//        for (JsonElement importStatement : classToCompile.get("importStatements").getAsJsonArray()) {
//            classString.append("import " + importStatement.getAsString() + ";\n");
//        }
//        classString.append(classToCompile.get("classDeclaration").getAsString());
//        classString.append(classToCompile.get("classString").getAsString() + "}");

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        if (jc == null) throw new Exception("Compiler unavailable");

        JavaSourceFromString jsfs = new JavaSourceFromString(className, classString);

        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(jsfs);

        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add("/Users/Padmaka/Desktop/Test_compiler");
        options.add("-classpath");
        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        StringBuilder sb = new StringBuilder();
        for (URL url : urlClassLoader.getURLs()) {
            sb.append(url.getFile()).append(File.pathSeparator);
        }
        sb.append("/Users/Padmaka/Desktop/Test_compiler");
        options.add(sb.toString());

        StringWriter output = new StringWriter();
        boolean success = jc.getTask(output, null, null, options, null, fileObjects).call();
        if (success) {
            DependencyPool.getDependencyPool().addAClass("someClass");
            System.out.println("Class " + "someClass" + " has been successfully compiled");
        } else {
            throw new Exception("Compilation failed :" + output);
        }

    }
}

class A {
    public void doSomething(){
        B b = new B();
    }
}

class B {
    public void someTask(){
        A a = new A();
    }
}
