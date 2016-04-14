package com.piranha.node.compile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.piranha.node.util.PiranhaConfig;
import org.apache.log4j.Logger;

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
 * Created by Padmaka on 1/26/16.
 */
public class Compiler extends Thread {
    private static final Logger LOG = Logger.getLogger(Compiler.class);
    private JsonObject classJson;

    public Compiler(JsonObject classJson) throws IOException {
        this.classJson = classJson;
    }

    /***
     * Overridden run method of Thread class
     */
    @Override
    public void run() {

        if (classJson.get("toBeCompiledWith") == null) {
            try {
                this.compile(classJson);
            } catch (Exception e) {
                LOG.error("Unable to compile the file", e);
                System.exit(0);
            }
        } else {
            JsonArray toBeCompiledWithArray = classJson.get("toBeCompiledWith").getAsJsonArray();
            toBeCompiledWithArray.add(classJson);
            try {
                this.compile(toBeCompiledWithArray);
            } catch (Exception e) {
                LOG.error("Unable to compile the file", e);
                System.exit(0);
            }
        }
    }


    /***
     * The method to compile the given set of classes
     *
     * @param classesToCompile JsonObjects of the classes to compile
     * @throws Exception
     */
    public void compile(JsonArray classesToCompile) throws Exception {

        List<JavaSourceFromString> jsfsList = new LinkedList<>();

        for (JsonElement element : classesToCompile) {

            JsonObject classToCompile = element.getAsJsonObject();
            StringBuilder packageName = new StringBuilder(classToCompile.get("package").getAsString());
            StringBuilder classString = new StringBuilder("");
            if (packageName.length() != 0) {
                classString = new StringBuilder("package " + packageName.replace(packageName.length() - 1, packageName.length(), "") + ";\n");
            }

            for (JsonElement importStatement : classToCompile.get("importStatements").getAsJsonArray()) {
                classString.append("import " + importStatement.getAsString() + ";\n");
            }
            classString.append(classToCompile.get("classDeclaration").getAsString());
            classString.append(classToCompile.get("classString").getAsString() + "}");

            JavaSourceFromString jsfs = new JavaSourceFromString(classToCompile.get("className").getAsString(), classString.toString());
            jsfsList.add(jsfs);

        }

        Iterable<? extends JavaFileObject> fileObjects = jsfsList;

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        if (jc == null) throw new Exception("Compiler unavailable");

        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(PiranhaConfig.getProperty("DESTINATION_PATH"));
        options.add("-classpath");

        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        StringBuilder sb = new StringBuilder();
        for (URL url : urlClassLoader.getURLs()) {
            sb.append(url.getFile()).append(File.pathSeparator);
        }
        sb.append(PiranhaConfig.getProperty("DESTINATION_PATH"));
        options.add(sb.toString());

        StringWriter output = new StringWriter();
        boolean success = jc.getTask(output, null, null, options, null, fileObjects).call();

        if (success) {
            ArrayList<String> compiledClasses = new ArrayList<>();
            for (JsonElement element : classesToCompile) {
                compiledClasses.add(element.getAsJsonObject().get("absoluteClassName").getAsString());
            }
            DependencyPool.getDependencyPool().addClasses(compiledClasses);
            LOG.info("Class [" + compiledClasses + "] has been successfully compiled");
        } else {
            throw new Exception("Compilation failed :" + output);
        }

    }

    /***
     * The method to compile the given class
     *
     * @param classToCompile JsonObject of the class to compile
     * @throws Exception
     */
    public void compile(JsonObject classToCompile) throws Exception {

        StringBuilder packageName = new StringBuilder(classToCompile.get("package").getAsString());
        StringBuilder classString = new StringBuilder("");
        if (packageName.length() != 0) {
            classString = new StringBuilder("package " + packageName.replace(packageName.length() - 1, packageName.length(), "") + ";\n");
        }

        for (JsonElement importStatement : classToCompile.get("importStatements").getAsJsonArray()) {
            classString.append("import " + importStatement.getAsString() + ";\n");
        }
        classString.append(classToCompile.get("classDeclaration").getAsString());
        classString.append(classToCompile.get("classString").getAsString() + "}");

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        if (jc == null) throw new Exception("Compiler unavailable");

        JavaSourceFromString jsfs = new JavaSourceFromString(classToCompile.get("className").getAsString(), classString.toString());

        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(jsfs);

        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(PiranhaConfig.getProperty("DESTINATION_PATH"));
        options.add("-classpath");
        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        StringBuilder sb = new StringBuilder();
        for (URL url : urlClassLoader.getURLs()) {
            sb.append(url.getFile()).append(File.pathSeparator);
        }
        sb.append(PiranhaConfig.getProperty("DESTINATION_PATH"));
        options.add(sb.toString());

        StringWriter output = new StringWriter();
        boolean success = jc.getTask(output, null, null, options, null, fileObjects).call();
        if (success) {
            DependencyPool.getDependencyPool().addAClass(classToCompile.get("absoluteClassName").getAsString());
            LOG.info("Class " + classToCompile.get("className").getAsString() + " has been successfully compiled");
        } else {
            throw new Exception("Compilation failed :" + output);
        }

    }
}