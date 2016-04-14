package com.piranha.node.communication;

import com.piranha.node.compile.DependencyPool;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;
import org.apache.log4j.Logger;

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
                //TODO SEnD HER
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
