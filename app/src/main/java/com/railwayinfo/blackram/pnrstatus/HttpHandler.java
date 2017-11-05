package com.railwayinfo.blackram.pnrstatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by blackram on 16/02/17.
 */

public class HttpHandler {

    /**
     * Receives string uses URL to convert it to url and uses HttpURLConnection to open connection
     * and read response
     * @param railwayApiUrl
     * @return
     */
    public String makeServiceCall(String railwayApiUrl){
        String response = null;
        try{
            URL url = new URL(railwayApiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //reading response from http url
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Converts input stream to String
     * @param in
     * @return
     */
    private String convertStreamToString(InputStream in){
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String temp;
        try {
            while((temp = br.readLine()) != null){
                sb.append(temp).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
