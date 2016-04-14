package com.piranha.node.communication;

import com.google.gson.JsonObject;
import com.piranha.node.compile.DependencyPool;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;
import com.sun.org.apache.bcel.internal.Constants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 4/14/16.
 */
public class Terminator extends Thread {

    private static final Logger LOG = Logger.getLogger(PiranhaNodeEndpoint.class);
    private ArrayList<Thread> rounds;

    public Terminator(ArrayList<Thread> rounds) {
        this.rounds = rounds;
    }

    public void run() {

        boolean isShutdown = false;

        while (!isShutdown) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.error("Error in Waiting in Termination ", e);
            }

            boolean isAllAlive = true;
            for (Thread x : rounds) {
                isAllAlive = isAllAlive && x.isAlive();
            }

            isShutdown = !isAllAlive;
        }

        //---------------------------------------------------

        String serverAddress = PiranhaConfig.getProperty("SERVER_ADDRESS");
        try {
            Socket socket = new Socket(serverAddress, 2500);
            DependencyPool pool = DependencyPool.getDependencyPool();
            ConcurrentHashMap<String, String> dependencyMap = pool.getDependencyMap();
            ConcurrentHashMap<String, String> completedCompiles = pool.getCompletedCompiles();
            ArrayList<String> classesToSend = new ArrayList<>();
            String myIP = Utils.getFirstNonLoopbackAddress(true, false).getHostAddress();

            for (String className : dependencyMap.keySet()) {
                if (myIP.equals(dependencyMap.get(className))) {
                    if (completedCompiles.containsKey(className)) {
                        classesToSend.add(className);
                    }
                }
            }

            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            for (String className : classesToSend){

                String path = PiranhaConfig.getProperty("DESTINATION_PATH") + Utils.PATH_SEPERATOR;
                String packagePath = className;
                packagePath = packagePath.replace(".", Utils.PATH_SEPERATOR) + ".class";
                File file = new File(path + packagePath);
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = IOUtils.toByteArray(fileInputStream);

                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("className", className);
                responseJson.addProperty("file", new String(Base64.encodeBase64(bytes)));

                stream.writeObject(responseJson.toString());

            }
            stream.flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.error("Error in waiting in Termination",e);
            }
            stream.close();
            socket.close();

            System.exit(0);

        } catch (IOException e) {
            LOG.error("Error in Connecting to Termination Socket", e);
        }

        //---------------------------------------------------
        System.exit(0);
    }
}
