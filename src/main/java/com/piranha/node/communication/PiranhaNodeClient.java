package com.piranha.node.communication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.piranha.node.compile.DependencyPool;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by root on 4/13/16.
 */
public class PiranhaNodeClient {
    private static final Logger LOG = Logger.getLogger(PiranhaNodeClient.class);


    public static void RequestDependency(String className) throws SocketException {

        DependencyPool pool = DependencyPool.getDependencyPool();

        if (pool.haveIRequestedDependency(className)) {
            return;
        }
        String dependencyNodeURI = pool.whereIsDependency(className);

        InetAddress loacalIP = Utils.getFirstNonLoopbackAddress(true, false);
        int port = Integer.parseInt(PiranhaConfig.getProperty("CLIENT_PORT"));
        if (loacalIP.getHostAddress().equals(dependencyNodeURI)) {
            return;
        }

        dependencyNodeURI = "http://" + dependencyNodeURI + ":" + port + "/dependency/request";

        HttpPost request = new HttpPost(dependencyNodeURI);
        JsonObject json = new JsonObject();
        json.addProperty("op", "REQUEST");
        json.addProperty("className", className);

        try {
            request.setEntity(new StringEntity(json.toString()));
        } catch (UnsupportedEncodingException e) {
            LOG.error("Error in setting Entity ", e);
        }

        pool.addReqestedDependency(className);

        try {
            HttpResponse response = doRequest(request);
            LOG.debug("Successfully Requested Dependency " + className);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Status Code not 200 in DependencyRequest Response ");
            }
        } catch (IOException e) {
            LOG.error("Error in Dependency Request ", e);
        }
    }

    private static HttpResponse doRequest(HttpUriRequest request) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        return client.execute(request);
    }

    public static void SendDependency(String classNameString, String IPAddress) throws IOException {
        String path = PiranhaConfig.getProperty("DESTINATION_PATH") + Utils.PATH_SEPERATOR;
        String packagePath = classNameString;
        packagePath = packagePath.replace(".", Utils.PATH_SEPERATOR) + ".class";

        File classFile = new File(path + packagePath);
        FileInputStream fileInputStream = new FileInputStream(path + packagePath);
        JsonParser parser = new JsonParser();

        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        fileInputStream.close();
        String className = classNameString;
        className = className.replace(PiranhaConfig.getProperty("DESTINATION_PATH"), "");

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("op", "RESPONSE");
        requestJson.addProperty("className", className);
        requestJson.addProperty("classPath", classFile.getAbsolutePath());
        requestJson.addProperty("file", new String(Base64.encodeBase64(bytes)));

        int port = Integer.parseInt(PiranhaConfig.getProperty("CLIENT_PORT"));

        HttpPost request = new HttpPost("http://" + IPAddress + ":" + port + "/dependency/response");
        LOG.debug("SEND URII - " + request.getURI());
        request.setEntity(new StringEntity(requestJson.toString()));

        doRequest(request);
        LOG.debug("Successdully sent Dependency " + className);
        //comm.writeToSocket(socket, requestJson);
    }
}
