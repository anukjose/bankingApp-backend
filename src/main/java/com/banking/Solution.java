package com.banking;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;



public class Solution {

    public static int run(String character) {
        try {
            // URL encoding to handle spaces and special characters
            String encodedCharacter = URLEncoder.encode(character, "UTF-8");
           // String urlString = "https://challenges.hackajob.co/swapi/api/people/?search=" + encodedCharacter;
            String urlString = "https://challenges.hackajob.co/swapi/api/people/?search=" + encodedCharacter + "&format=json";

            //        URL url = new URL("https://challenges.hackajob.co/swapi/api/films/?format=json");

            // Open a connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            System.out.println("Raw API Response: " + result.toString());

            
            // Parse JSON response
			JsonObject jsonObject = new
JsonParser().parse(result.toString()).getAsJsonObject();
           /* JsonObject jsonObject = JsonParser.parseString(result.toString()).getAsJsonObject();*/
            JsonArray results = jsonObject.getAsJsonArray("results");
System.out.println("API Response: " + result.toString());

            // If no results, return 0
            if (results.size() == 0) {
                return 0;
            }

            // Extract films array from first result
            JsonObject characterData = results.get(0).getAsJsonObject();
            JsonArray films = characterData.getAsJsonArray("films");

            return films.size(); // Return number of films

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Return 0 in case of error
        }
    }
/*
    public static void main(String[] args) {
        System.out.println(run("Luke Skywalker")); // Expected Output: 5
    }*/
}
