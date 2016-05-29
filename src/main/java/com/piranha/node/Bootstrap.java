package com.piranha.node;

import com.google.gson.JsonObject;
import com.piranha.node.communication.Communication;
import com.piranha.node.communication.DependencyResponseHandler;
import com.piranha.node.communication.PiranhaNodeEndpoint;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by root on 4/13/16.
 */
public class Bootstrap {
    public static void main(String[] args) {

        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");

        //Innitializing Properties
        try {
            PiranhaConfig.innitializeProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.deleteDirectory(new File(PiranhaConfig.getProperty("DESTINATION_PATH")));
        Utils.makeDirectory(new File(PiranhaConfig.getProperty("DESTINATION_PATH")));
        try {
            PiranhaNodeEndpoint.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DependencyResponseHandler responseHandler = DependencyResponseHandler.getDependencyResponseHandler();
        responseHandler.start();

        try {
            Socket serverPing = new Socket(PiranhaConfig.getProperty("SERVER_ADDRESS"), 9005);
            JsonObject nodeDetails = new JsonObject();
            nodeDetails.addProperty("ip", Utils.getFirstNonLoopbackAddress(true, false).getHostAddress());
            nodeDetails.addProperty("cpu", getCpu());
            nodeDetails.addProperty("ram", getMem());
            nodeDetails.addProperty("os", System.getProperty("os.name"));
            Communication comm = new Communication();
            comm.writeToSocket(serverPing, nodeDetails);
            serverPing.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCpu(){
        String cmd = "sysctl -n machdep.cpu.brand_string";
        String line;
        String cpuString = "";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null) {
                cpuString += line;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cpuString;
    }

    public static String getMem(){
        String[] cmd = {
                "/bin/sh",
                "-c",
                "system_profiler SPHardwareDataType | grep Memory"
        };

        String line;
        String memString = "";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null) {
                memString += line;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return memString.trim();
    }
}
