package com.piranha.node.util;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by root on 4/13/16.
 */
public class Utils {

    public final static Type hashMapType = new TypeToken<HashMap<String, String>>() {
    }.getType();
    public final static Type concurrentLinkedQueueType = new TypeToken<ConcurrentLinkedQueue<JsonObject>>() {
    }.getType();
    public final static Type concurrentHashMapType = new TypeToken<ConcurrentHashMap<String, String>>() {
    }.getType();

    public final static String PATH_SEPERATOR;

    static {
        String pathSeparator = null;

        if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Linux")) {
            pathSeparator = "/";
        } else if (System.getProperty("os.name").contains("Windows")) {
            pathSeparator = "\\";
        }
        PATH_SEPERATOR = pathSeparator;
    }


    /***
     * Util method to get the non loopback local IP address
     *
     * @param preferIpv4 boolean true if Ipv4 is needed
     * @param preferIPv6 boolean true if Ipv6 is needed
     * @return non loopback local IP address
     * @throws SocketException
     */
    public static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements(); ) {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    if (addr instanceof Inet4Address) {
                        if (preferIPv6) {
                            continue;
                        }
                        return addr;
                    }
                    if (addr instanceof Inet6Address) {
                        if (preferIpv4) {
                            continue;
                        }
                        return addr;
                    }
                }
            }
        }
        return null;
    }

    public static void deleteDirectory(File file){

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deleteDirectory(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public static void makeDirectory(File f){
        f.mkdir();
    }
}
