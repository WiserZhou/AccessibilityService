package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GptRequestTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String result = "";

        try {
            URL url = new URL("https://gpt.yanghuan.site/api/openai/v1/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer ak-LgOVMZ0nHqetz55TjSBlZia9u7QvYxB2kNvNlx8ACsjkZexD");

            JSONObject body = new JSONObject();
            body.put("model", "gpt-3.5-turbo");

            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", params[0]);

            body.put("messages", new JSONObject[]{message});
            body.put("stream", false);
            body.put("temperature", 0.5f);

            // Send the request body
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(body.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            // Read the response
            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            result = response.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject responseJson = new JSONObject(result);
            String content = responseJson.getJSONArray("choices")
                    .getJSONObject(0).getJSONObject("message")
                    .getString("content");
            // 处理接收到的内容
            Log.d("GPTResponse", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}