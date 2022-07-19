package com.example.newsappv01;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class QueryUtils {

    static ArrayList<Story> getRelevantStories(String requestUrl) {
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try {
            JSONResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Disconnection Error", "getRelevantStories: Error closing inputstream");
        }
        return extractFeaturesFromJson(JSONResponse);
    }

    static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e("Corrupted URL", "createUrl: Error converting String to URL");
        }
        return url;
    }

    static String makeHttpRequest(URL url) throws IOException {
        String JSONResponse = null;
        if(url == null)
        {
            return JSONResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try
        {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200)
            {
                inputStream = httpURLConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e("Response Code Error", "makeHttpRequest: Response Code - " + httpURLConnection.getResponseCode());
            }
        }
        catch(IOException e)
        {
            Log.e("Connection Error", "makeHttpRequest: Error making http connection: " + e);
        }
        finally {
            if(httpURLConnection != null)
            {
                httpURLConnection.disconnect();
            }
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        return JSONResponse;
    }

    static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static ArrayList<Story> extractFeaturesFromJson(String JSONResponse)
    {
        if(JSONResponse == null)
        {
            return null;
        }
        ArrayList<Story> arrayListOfStories = new ArrayList<Story>();


        try {
            JSONObject root = new JSONObject(JSONResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for(int i=0; i<results.length(); i++)
            {
                JSONObject currentStory = results.getJSONObject(i);
                String title = currentStory.getString("webTitle");
                String section = currentStory.getString("sectionName");
                String date = currentStory.getString("webPublicationDate");
                String link = currentStory.getString("webUrl");

                arrayListOfStories.add(new Story(title, section, "", date, link));
            }
        } catch (JSONException e) {
            Log.e("JSON Error", "extractFeaturesFromJson: Error converting json into arraylist: " + e);
        }

        return arrayListOfStories;
    }

}
