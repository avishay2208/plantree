package com.example.sqlproject;


import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class ServerPostCommunication extends AsyncTask<String, String, String> {

    private static final String URL = "https://plantreeal.000webhostapp.com/index.php";
    private static int counter = 0;
    private static HttpURLConnection con;

    public ServerPostCommunication() {
    }

    @Override
    protected synchronized String doInBackground(String... params) {
        while(counter!=0)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter++;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String url = URL;

            String urlParameters = params[0];

            for (int i = 1; i < params.length; i++) {
                urlParameters += "&&" + params[i];
            }

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            try {

                URL myurl = new URL(url);
                con = (HttpURLConnection) myurl.openConnection();

                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Java client");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {

                    wr.write(postData);
                }

                StringBuilder content;

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {

                    String line;
                    content = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }

                    return content.toString();
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
                counter--;

                notify();
            }
            return "Hhhh";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "hhh";
    }
}
