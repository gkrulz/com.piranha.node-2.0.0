package com.piranha.node;

import com.piranha.node.communication.DependencyResponseHandler;
import com.piranha.node.communication.PiranhaNodeEndpoint;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;
import com.sun.xml.internal.bind.v2.schemagen.Util;

import java.io.File;
import java.io.IOException;
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
            serverPing.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
