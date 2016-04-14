package com.piranha.node.compile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.piranha.node.communication.PiranhaNodeClient;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by root on 4/13/16.
 */
public class CompileRound extends Thread {
    private static final Logger LOG = Logger.getLogger(CompileRound.class);
    private ExecutorService service;
    private ConcurrentLinkedQueue<JsonObject> pendingJobs;

    public CompileRound(ConcurrentLinkedQueue<JsonObject> pendingJobs) throws Exception {

        int compilerThreads = Integer.parseInt(PiranhaConfig.getProperty("COMPILER_THREADS"));
        service = Executors.newFixedThreadPool(compilerThreads);
        this.pendingJobs = pendingJobs;
    }


    public void run() {
        Gson gson = new Gson();

        while (pendingJobs.size() > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject currentObject = pendingJobs.peek();

            HashMap<String, String> dependencies = gson.fromJson(currentObject.get("dependencies").getAsString(), Utils.hashMapType);
            /*if(currentObject.get("absoluteClassName").getAsString().equals("com.piranha.scan.ScannerTask")){
                System.out.println("");
            }*/
            LOG.debug("Checking for Dependencies of " + currentObject.get("absoluteClassName") + "for Compilation "+gson.toJson(dependencies));
            //LOG.debug("PENDING JOBS "+gson.toJson(pendingJobs));


            try {
                DependencyPool dependencyPool = DependencyPool.getDependencyPool();
                ArrayList<String> nonContainingDependencies = dependencyPool.checkForDependencies(dependencies);
                if (nonContainingDependencies.size() == 0) {

                    LOG.debug("Found All Dependencies for "+currentObject.get("absoluteClassName")+". Initiating Compilation");
                    Compiler compiler = new Compiler(currentObject);

                    if (currentObject.get("toBeCompiledWith") != null) {
                        JsonArray toBeCompiledWiths = currentObject.get("toBeCompiledWith").getAsJsonArray();
                        for (JsonElement element : toBeCompiledWiths) {
                            JsonObject object = element.getAsJsonObject();
                            dependencyPool.addAPendingClass(object.get("absoluteClassName").getAsString());
                        }
                    }
                    dependencyPool.addAPendingClass(currentObject.get("absoluteClassName").getAsString());
                    service.submit(compiler);
                    LOG.debug("Submitted an Executor for " + currentObject.get("absoluteClassName") + " for compilation");
                    pendingJobs.poll();
                } else {
                    for (String classToRequest : nonContainingDependencies) {
                        PiranhaNodeClient.RequestDependency(classToRequest);
                    }
                    pendingJobs.add(pendingJobs.poll());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("Error in Dependency Pool Singleton Class", e);
            }
        }
    }
}
