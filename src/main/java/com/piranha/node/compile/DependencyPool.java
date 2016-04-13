package com.piranha.node.compile;

import com.google.gson.JsonObject;
import com.piranha.node.communication.PiranhaNodeEndpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 4/13/16.
 */
public class DependencyPool {

    private static DependencyPool singletonObject;

    private ConcurrentHashMap<String, String> completedCompiles = new ConcurrentHashMap<String, String>();
    private ConcurrentHashMap<String, String> pendingCompiles = new ConcurrentHashMap<String, String>();
    private ConcurrentHashMap<String, String> requestedDependencies = new ConcurrentHashMap<String, String>();
    private ConcurrentHashMap<String, String> dependencyMap = new ConcurrentHashMap<String, String>();

    private DependencyPool() {

    }

    public synchronized ConcurrentHashMap<String, String> getCompletedCompiles(){
        return completedCompiles;
    }

    public synchronized ConcurrentHashMap<String, String> getPendingCompiles(){
        return pendingCompiles;
    }

    public static DependencyPool getDependencyPool() {
        if (singletonObject == null) {
            synchronized (PiranhaNodeEndpoint.class) {
                if (singletonObject == null) {
                    singletonObject = new DependencyPool();
                }
            }
        }
        return singletonObject;
    }

    public synchronized void addAClass(String absoluteClassName) {
        completedCompiles.put(absoluteClassName, absoluteClassName);
        pendingCompiles.remove(absoluteClassName);
    }

    public synchronized void addClasses(ArrayList<String> classList) {
        for (String className : classList) {
            completedCompiles.put(className, className);
            pendingCompiles.remove(className);
        }
    }

    public synchronized boolean doIHaveDependency(String absoluteClassName) {
        return (completedCompiles.get(absoluteClassName) != null);
    }

    public synchronized boolean haveIRequestedDependency(String absoluteClassName) {
        return requestedDependencies.containsKey(absoluteClassName);
    }

    public synchronized void addReqestedDependency(String absoluteClassName) {
        requestedDependencies.put(absoluteClassName, absoluteClassName);
    }

    public synchronized void updateDependencyMap(ConcurrentHashMap<String, String> newDependencyMap) {
        dependencyMap.putAll(newDependencyMap);
    }

    public synchronized String whereIsDependency(String absoluteClassName) {
        return dependencyMap.get(absoluteClassName);
    }

    public synchronized ArrayList<String> checkForDependencies(HashMap<String, String> testMap) {
        ArrayList<String> nonContainingList = new ArrayList<String>();
        for (String dependency : testMap.values()) {
            if (completedCompiles.get(dependency) == null) {
                nonContainingList.add(dependency);
            }
        }
        return nonContainingList;
    }

    public synchronized void addAPendingClass(String className) {
        pendingCompiles.put(className, className);
    }

    public synchronized void removeRequestedDependency(String className){
        requestedDependencies.remove(className);
    }
}
