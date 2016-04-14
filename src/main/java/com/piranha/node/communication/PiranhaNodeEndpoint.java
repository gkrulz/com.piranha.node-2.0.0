package com.piranha.node.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.piranha.node.compile.CompileRound;
import com.piranha.node.compile.DependencyPool;
import com.piranha.node.util.PiranhaConfig;
import com.piranha.node.util.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by root on 4/13/16.
 */
public class PiranhaNodeEndpoint {

    private static final Logger LOG = Logger.getLogger(PiranhaNodeEndpoint.class);
    private HttpServer server;
    private static PiranhaNodeEndpoint singletonObject;

    private PiranhaNodeEndpoint() throws IOException {
        int serverPort = Integer.parseInt(PiranhaConfig.getProperty("CLIENT_PORT"));
        int httpBacklog = Integer.parseInt(PiranhaConfig.getProperty("HTTP_BACKLOG"));

        InetSocketAddress serverSocketInet = new InetSocketAddress("0.0.0.0", serverPort);
        server = HttpServer.create(serverSocketInet, httpBacklog);

        server.createContext("/round", new StartRoundContext());
        server.createContext("/dependency/request", new DependencyRequestContext());
        server.createContext("/dependency/response", new DependencyResponseContext());
        server.createContext("/terminate", new TerminationContext());

        server.start();
        LOG.debug("Started the HTTP Server on 0.0.0.0 with Backlog=" + httpBacklog);
    }

    public static void startServer() throws IOException {
        if (singletonObject == null) {
            synchronized (PiranhaNodeEndpoint.class) {
                if (singletonObject == null) {
                    singletonObject = new PiranhaNodeEndpoint();
                }
            }
        }
    }

    //----------------------HTTP Handlers--------------------------
    private static class StartRoundContext implements HttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {

            if (httpExchange.getRequestMethod().equals("POST")) {
                LOG.debug("ENDPOINT HIT /round from" + httpExchange.getRemoteAddress());

                JsonParser parser = new JsonParser();
                try {
                    JsonObject roundObject = parser.parse(new String(IOUtils.toByteArray(httpExchange.getRequestBody()))).getAsJsonObject();

                    if (!roundObject.get("op").getAsString().equals("COMPILATION")) {
                        throw new Exception("Invalid OP code object from " + httpExchange.getRemoteAddress());
                    }

                    Gson gson = new Gson();

                    ConcurrentLinkedQueue<JsonObject> classes = gson.fromJson(roundObject.get("classes").getAsString(), Utils.concurrentLinkedQueueType);

                    ConcurrentHashMap<String, String> dependencyMap = gson.fromJson(roundObject.get("dependencyMap").getAsString(), Utils.concurrentHashMapType);
                    LOG.debug(dependencyMap);
                    DependencyPool.getDependencyPool().updateDependencyMap(dependencyMap);

                    CompileRound round = new CompileRound(classes);
                    round.start();

                    String msg = "Successfully Started Round";
                    httpExchange.sendResponseHeaders(200, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes());
                    os.close();

                    LOG.debug("Accepted Round Object from " + httpExchange.getRemoteAddress());
                } catch (Exception e) {
                    LOG.debug("Cannot parse /round Object from " + httpExchange.getRemoteAddress());
                    LOG.error("Exception Occurred in /round method ", e);
                    String errMsg = "Cannot parse Round Object or Invalid OP Code";
                    httpExchange.sendResponseHeaders(400, errMsg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(errMsg.getBytes());
                    os.close();
                }
            } else {
                String getErrResponse = "This is a POST only Endpoint";
                httpExchange.sendResponseHeaders(405, getErrResponse.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(getErrResponse.getBytes());
                os.close();
            }

        }
    }


    private static class DependencyRequestContext implements HttpHandler {

        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("POST")) {
                LOG.debug("ENDPOINT HIT /dependency/request (POST) from " + httpExchange.getRemoteAddress());
                JsonParser parser = new JsonParser();
                try {
                    JsonObject request = parser.parse(new String(IOUtils.toByteArray(httpExchange.getRequestBody()))).getAsJsonObject();

                    if (!request.get("op").getAsString().equals("REQUEST")) {
                        throw new Exception("Invalid OP code object from " + httpExchange.getRemoteAddress());
                    }

                    String className = request.get("className").getAsString();

                    if (className == null) {
                        throw new Exception("Class name not defined in /dependency/request from " + httpExchange.getRemoteAddress());
                    }

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("className", className);
                    jsonObject.addProperty("IP", httpExchange.getRemoteAddress().getHostString());

                    DependencyResponseHandler responseHandler = DependencyResponseHandler.getDependencyResponseHandler();
                    responseHandler.addARequestedDependency(jsonObject);

                    String msg = "Dependency Requested Successfully";
                    httpExchange.sendResponseHeaders(200, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes());
                    os.close();


                } catch (Exception e) {
                    LOG.debug("Cannot parse /dependency/request Object from " + httpExchange.getRemoteAddress());
                    LOG.error("Exception Occurred in /dependency/request method ", e);
                    String errMsg = "Cannot parse Round Object or Invalid OP Code";
                    httpExchange.sendResponseHeaders(400, errMsg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(errMsg.getBytes());
                    os.close();
                }


            } else if (httpExchange.getRequestMethod().equals("GET")) {
                LOG.debug("ENDPOINT HIT /dependency/request (GET) from " + httpExchange.getRemoteAddress());

                String getErrResponse = "This is a POST only Endpoint";
                httpExchange.sendResponseHeaders(405, getErrResponse.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(getErrResponse.getBytes());
                os.close();
            }
        }
    }

    private static class DependencyResponseContext implements HttpHandler {

        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("POST")) {
                LOG.debug("ENDPOINT HIT /dependency/response (POST) from " + httpExchange.getRemoteAddress());
                JsonParser parser = new JsonParser();

                try {
                    JsonObject request = parser.parse(new String(IOUtils.toByteArray(httpExchange.getRequestBody()))).getAsJsonObject();

                    if (!request.get("op").getAsString().equals("RESPONSE")) {
                        throw new Exception("Invalid OP code object from " + httpExchange.getRemoteAddress());
                    }

                    String fileName = request.get("classPath").getAsString();
                    fileName = fileName.replace("/", Utils.PATH_SEPERATOR);
                    fileName = fileName.replace("\\", Utils.PATH_SEPERATOR);

                    File file = new File(PiranhaConfig.getProperty("DESTINATION_PATH")+ fileName);
                    file.getParentFile().mkdirs();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    byte[] bytes = Base64.decodeBase64(request.get("file").getAsString());

                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

                    IOUtils.copy(bis, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();


                    LOG.debug("Successfully Received Dependency "+request.get("className").getAsString());
                    DependencyPool pool = DependencyPool.getDependencyPool();
                    pool.removeRequestedDependency(request.get("className").getAsString());
                    pool.addAClass(request.get("className").getAsString());

                    String msg = "Dependency Received Successfully";
                    httpExchange.sendResponseHeaders(200, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes());
                    os.close();

                } catch (Exception e) {
                    LOG.debug("Cannot parse /dependency/response Object from " + httpExchange.getRemoteAddress());
                    LOG.error("Exception Occurred in /dependency/response method ", e);
                    String errMsg = "Cannot parse Round Object or Invalid OP Code";
                    httpExchange.sendResponseHeaders(400, errMsg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(errMsg.getBytes());
                    os.close();
                }


            } else if (httpExchange.getRequestMethod().equals("GET")) {
                LOG.debug("ENDPOINT HIT /dependency/response (GET) from " + httpExchange.getRemoteAddress());

                String getErrResponse = "This is a POST only Endpoint";
                httpExchange.sendResponseHeaders(405, getErrResponse.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(getErrResponse.getBytes());
                os.close();
            }
        }
    }

    private static class TerminationContext implements HttpHandler {

        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("POST")) {
                LOG.debug("ENDPOINT HIT /terminate from" + httpExchange.getRemoteAddress());

            } else {

                String getErrResponse = "This is a POST only Endpoint";
                httpExchange.sendResponseHeaders(405, getErrResponse.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(getErrResponse.getBytes());
                os.close();
            }
        }
    }
}
