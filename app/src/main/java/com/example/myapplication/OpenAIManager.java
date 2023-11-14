package com.example.myapplication;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OpenAIManager {
    private static final String BASE_URL = "https://api.openai.com/v1/completions";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private static final String API_KEY = "sk-dMHkagT7vyUQmldu49cDH3bOkdaU8Ue4dUXjnT93I70KNxMu";

    public OpenAIManager() {
        client = new OkHttpClient();
    }

    public void queryGPTV2(String prompt, Callback callback) {
        String url = BASE_URL + "";

        String json = "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}],\"timeout\":15}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println("Response: " + responseData);
                } else {
                    System.out.println("Request failed: " + response.code() + " - " + response.message());
                }
            }
        });
    }

}
