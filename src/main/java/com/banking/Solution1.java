package com.banking;

import java.io.*;
import java.net.*;
import com.google.gson.*;

public class Solution1 {

    public static String run(String film, String character) {
        // Initialize StringBuilder for result
        StringBuilder filmSb = new StringBuilder();
        filmSb.append(film + ":");

        try {
            // URL encoding to handle spaces and special characters
            String encodedCharacter = URLEncoder.encode(character, "UTF-8");
            String encodedFilm = URLEncoder.encode(film, "UTF-8");

            // Construct the URLs for both character and film
           String urlStringchr = "https://challenges.hackajob.co/swapi/api/people/?search=" + encodedCharacter + "&format=json";
            String urlStringfilm = "https://challenges.hackajob.co/swapi/api/films/?search=" + encodedFilm + "&format=json";


            // Fetch and process the character data
            String characterResult = fetchData(urlStringchr);

            // Parse JSON response for character
            JsonObject characterJsonObject = new JsonParser().parse(characterResult).getAsJsonObject();
            
            JsonArray characterResults = characterJsonObject.getAsJsonArray("results");

            // If no results, return 0
            if (characterResults.size()>0) {

            // Extract the character URLs
            JsonObject characterData = characterResults.get(0).getAsJsonObject();
            JsonArray characterUrls = characterData.getAsJsonArray("films");

            // For each film URL, fetch the character name
            for (JsonElement characterUrlElement : characterUrls) {
                String characterUrl = characterUrlElement.getAsString();
                String url = characterUrl.replaceAll("/$", "");
                String characterJson = fetchData(url+"?format=json");

                // Parse character response to get character name
                JsonObject characterResponse = new JsonParser().parse(characterJson).getAsJsonObject();
                String characterName = characterResponse.get("title").getAsString();

                System.out.println("Character Name: " + characterName);
                filmSb.append(characterName + ",");
            }

            // Remove last comma and append semicolon
            filmSb.deleteCharAt(filmSb.length() - 1);
            }
            filmSb.append(";");

            // Fetch and process the film data
            String filmsResult = fetchData(urlStringfilm);

            // Parse JSON response for film
            JsonObject filmsJsonObject = new JsonParser().parse(filmsResult).getAsJsonObject();
            JsonArray filmsResults = filmsJsonObject.getAsJsonArray("results");

            // If no results, return 0
            if (filmsResults.size()>0) {

            // Extract the film URLs
            JsonObject filmData = filmsResults.get(0).getAsJsonObject();
            JsonArray filmUrls = filmData.getAsJsonArray("characters");

            // For each character URL, fetch the film title
            for (JsonElement filmUrl : filmUrls) {
                String filmUrlStr = filmUrl.getAsString();
                String url = filmUrlStr.replaceAll("/$", "");
                String filmJson = fetchData(url+"?format=json");
               // System.out.println(filmJson);

                // Parse film response to get film title
                JsonObject filmResponse = new JsonParser().parse(filmJson).getAsJsonObject();
                String filmTitle = filmResponse.get("name").getAsString();

                System.out.println("Film Title: " + filmTitle);
                filmSb.append(filmTitle + ",");
            }

            // Remove last comma
            filmSb.deleteCharAt(filmSb.length() - 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

        return filmSb.toString();
    }

    // Method to fetch data from a URL
    public static String fetchData(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
/*
    public static void main(String[] args) {
        // Test the solution with some input
        String film = "A New Hope";
        String character = "Luke Skywalker";
        String result = run(film, character);
        System.out.println("Result: " + result);
    }*/
}
