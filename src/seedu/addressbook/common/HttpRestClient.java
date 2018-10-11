package seedu.addressbook.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * Http Client needed for RESTful APIs. Currently, only GET request is supported.
 */
public class HttpRestClient {
    private HttpClient httpClient;

    public HttpRestClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public HttpResponse requestGetResponse(String url) {
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            return response;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }
}
