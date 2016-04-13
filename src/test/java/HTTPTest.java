import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.tools.javac.parser.JavacParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by root on 4/13/16.
 */
public class HTTPTest {
    public static void main(String[] args) throws IOException {

 /*       String url = "http://192.168.0.101/round";

        HttpClient client = HttpClientBuilder.create().build();
        //HttpGet get = new HttpGet(url);


        HttpPost post = new HttpPost(url);


        System.out.println(USER_AGENT);
        // add header
        //post.setHeader("User-Agent", USER_AGENT);
        //post.setHeader("Content-Type","application/json");

        JsonObject json = new JsonObject();
        json.addProperty("Name","Test");


        post.setEntity(new StringEntity(json.toString()));

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);*/

//        JavacParser parser = new JavacParserrser();
    }


}
