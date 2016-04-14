package com.piranha.node.communication;

import com.google.gson.JsonObject;
import com.piranha.node.compile.DependencyPool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by root on 4/13/16.
 */
public class DependencyResponseHandler extends Thread {

    private static final Logger LOG = Logger.getLogger(DependencyResponseHandler.class);
    private ConcurrentLinkedQueue<JsonObject> dependenciesToSend = new ConcurrentLinkedQueue<>();
    private static DependencyResponseHandler singletonObject;

    private DependencyResponseHandler() {

    }

    public static DependencyResponseHandler getDependencyResponseHandler() {
        if (singletonObject == null) {
            synchronized (DependencyResponseHandler.class) {
                if (singletonObject == null) {
                    singletonObject = new DependencyResponseHandler();
                }
            }
        }
        return singletonObject;
    }

    public void addARequestedDependency(JsonObject classObject) {
        dependenciesToSend.add(classObject);
    }

    public void run() {
        DependencyPool pool = DependencyPool.getDependencyPool();
        ConcurrentHashMap<String, String> compiledClasses = pool.getCompletedCompiles();

        while (true) {

            while (dependenciesToSend.size() > 0) {
                LOG.debug(dependenciesToSend);
                JsonObject classObject = dependenciesToSend.peek();
                String classname = classObject.get("className").getAsString();
                if (compiledClasses.get(classname) != null) {
                    String IP = classObject.get("IP").getAsString();
                    try {
                        PiranhaNodeClient.SendDependency(classname, IP);
                        dependenciesToSend.poll();
                    } catch (IOException e) {
                        LOG.error("Error In Sending Dependency " + classname, e);
                    }
                }else{
                    dependenciesToSend.add(dependenciesToSend.poll());
                }
            }
        }
    }
}
