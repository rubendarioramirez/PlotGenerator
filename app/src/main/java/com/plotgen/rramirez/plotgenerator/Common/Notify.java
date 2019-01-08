package com.plotgen.rramirez.plotgenerator.Common;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Notify extends AsyncTask<Void, String, String> {
    String to;
    String message,id;

    public Notify(String to, String message, String id) {
        this.to = to;
        this.message = message;
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);

            // HTTP request header
            con.setRequestProperty("project_id", "53708015755");
            con.setRequestProperty("Authorization", "key=AAAADIE_SIs:APA91bG4sKHErVN0SbWxHsq476732yCWe51H0XHu3hJQrPOIzuO8H1l1qJc-DF_qa2CiT2p4HHWDlyPIaRUCKj4NTX3SI_rCFokayV9yHtXl0qYo_AHifWyg7h2MRaweA-YeMO4Pl-TD");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
            con.connect();

            JSONObject notification = new JSONObject();
            notification.put("title", "Weekly Challenge");
            notification.put("body", message);
            notification.put("id", id);
            notification.put("tag", "post");

            Log.e("notify",notification.toString());

            // HTTP request
            JSONObject data = new JSONObject();
            data.put("to", to);
            data.put("notification", notification);
            data.put("tag", "post");
            data.put("id", id);
            data.put("data", notification);

            OutputStream os = con.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.close();

            // Read the response into a string
            InputStream is = con.getInputStream();
            String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
            is.close();

            // Parse the JSON string and return the notification key
            JSONObject response = new JSONObject(responseString);
            Log.e("response", response.toString());
            return response.getString("multicast_id");

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
