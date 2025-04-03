package com.banking;


import java.io.*;
import java.net.*;
import java.util.*;

import com.banking.model.UnifiedProduct;
import com.google.gson.*;

public class Solution2 {

    public static String run(String film, String character) {
        // Initialize StringBuilder for result
        StringBuilder filmSb = new StringBuilder();
        filmSb.append(film + ": ");

        try {
            // URL encoding to handle spaces and special characters
            String encodedCharacter = URLEncoder.encode(character, "UTF-8");
            String encodedFilm = URLEncoder.encode(film, "UTF-8");

            // Construct the URLs for both character and film
            String urlStringchr = "https://challenges.hackajob.co/swapi/api/people/?search=" + encodedCharacter + "&format=json";
            String urlStringfilm = "https://challenges.hackajob.co/swapi/api/films/?search=" + encodedFilm + "&format=json";

          


            

            // Fetch and process the film data
            String filmsResult = fetchData(urlStringfilm);

            // Parse JSON response for film
            JsonObject filmsJsonObject = new JsonParser().parse(filmsResult).getAsJsonObject();
            JsonArray filmsResults = filmsJsonObject.getAsJsonArray("results");

            // List to store film names
            List<String> filmNames = new ArrayList<>();

            // If no results, return 0
            if (filmsResults.size() ==0) {
                filmSb.append("none");
            }
            else {
                // Extract the film URLs
                JsonObject filmData = filmsResults.get(0).getAsJsonObject();
                JsonArray filmUrls = filmData.getAsJsonArray("characters");

                // For each character URL, fetch the film title
                for (JsonElement filmUrl : filmUrls) {
                    String filmUrlStr = filmUrl.getAsString();
                    String url = filmUrlStr.replaceAll("/$", "");
                    String filmJson = fetchData(url + "?format=json");

                    // Parse film response to get film title
                    JsonObject filmResponse = new JsonParser().parse(filmJson).getAsJsonObject();
                    String filmTitle = filmResponse.get("name").getAsString();

                    System.out.println("Film Title: " + filmTitle);
                    filmNames.add(filmTitle);
                }
            
           

            // Sort film names alphabetically
            Collections.sort(filmNames);

            // Append sorted film names to result
            for (String filmName : filmNames) {
                filmSb.append(filmName + ", ");
            }

            // Remove last comma
             if (filmSb!=null){
                filmSb.deleteCharAt(filmSb.length() - 1);
            }
             if (filmSb!=null){
                filmSb.deleteCharAt(filmSb.length() - 1);
            }
            }
            filmSb.append("; "+character+": ");

             
             // Fetch and process the character data
             String characterResult = fetchData(urlStringchr);

             // Parse JSON response for character
             JsonObject characterJsonObject = new JsonParser().parse(characterResult).getAsJsonObject();
             JsonArray characterResults = characterJsonObject.getAsJsonArray("results");

             // List to store character names
             List<String> characterNames = new ArrayList<>();

             // If no results, return 0
             if (characterResults.size() == 0) {
                 filmSb.append("none");
             }
             else {
                 // Extract the character URLs
                 JsonObject characterData = characterResults.get(0).getAsJsonObject();
                 JsonArray characterUrls = characterData.getAsJsonArray("films");

                 // For each film URL, fetch the character name
                 for (JsonElement characterUrlElement : characterUrls) {
                     String characterUrl = characterUrlElement.getAsString();
                     String url = characterUrl.replaceAll("/$", "");
                     String characterJson = fetchData(url + "?format=json");

                     // Parse character response to get character name
                     JsonObject characterResponse = new JsonParser().parse(characterJson).getAsJsonObject();
                     String characterName = characterResponse.get("title").getAsString();

                     System.out.println("Character Name: " + characterName);
                     characterNames.add(characterName);
                 }
             
             


            
         // Sort character names alphabetically
            Collections.sort(characterNames);

            // Append sorted character names to result
            for (String characterName : characterNames) {
                filmSb.append(characterName + ", ");
            }

            // Remove last comma and append semicolon
            if (filmSb!=null){
                filmSb.deleteCharAt(filmSb.length() - 1);
            }
             if (filmSb!=null){
                filmSb.deleteCharAt(filmSb.length() - 1);
            }
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
    
    public static JsonArray fetchProductInfo() throws IOException{
        String url="https://s3.eu-west-1.amazonaws.com/hackajob-assets1.p.hackajob/challenges/sainsbury_products/products_v2.json";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new
        InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
        result.append(line);
        }
        reader.close();
         JsonElement jsonElement = new JsonParser().parse(result.toString());
        System.out.println(jsonElement);

        // Ensure it's an array before returning
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        } else {
            throw new IllegalStateException("Expected a JSON array but got something else.");
        }

    }
    public static JsonArray fetchProductPrices() throws IOException{
         String url="https://s3.eu-west-1.amazonaws.com/hackajob-assets1.p.hackajob/challenges/sainsbury_products/products_price_v2.json";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new
        InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
        result.append(line);
        }
        reader.close();
        JsonElement jsonElement = new JsonParser().parse(result.toString());
        System.out.println(jsonElement);

        // Ensure it's an array before returning
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        } else {
            throw new IllegalStateException("Expected a JSON array but got something else.");
        }

    }
    
    public  static List<UnifiedProduct> combineApiResponses() throws IOException{
        JsonArray productInfo=fetchProductInfo();
        JsonArray productPrices=fetchProductPrices();
        List<UnifiedProduct> unifiedProductList=new ArrayList<>();
        for (JsonElement priceElement : productPrices) {
         JsonObject priceObject = priceElement.getAsJsonObject();
         //int product_uid = priceObject.get("product_uid").getAsInt();
         int product_uid = Integer.parseInt(priceObject.get("product_uid").getAsString());

         double unit_price = priceObject.get("unit_price").getAsDouble();
         String unit_price_measure = priceObject.get("unit_price_measure").getAsString();
         int unit_price_measure_amount = priceObject.get("unit_price_measure_amount").getAsInt();
        // Find the matching product info from API 2 based on product_uid
        for (JsonElement infoElement : productInfo) {
       JsonObject infoObject = infoElement.getAsJsonObject();
       //int infoProductUid = infoObject.get("product_uid").getAsInt();
       int infoProductUid = Integer.parseInt(infoObject.get("product_uid").getAsString());

      String infoProductType=infoObject.get("product_type").getAsString();
      String infoProductName=infoObject.get("name").getAsString();
     String infoProductFullUrl=infoObject.get("full_url").getAsString();
      if (product_uid==infoProductUid) {

unifiedProductList.add(new UnifiedProduct(infoProductUid,infoProductType,infoProductName,infoProductFullUrl,unit_price,unit_price_measure,unit_price_measure_amount));
}
}
}

        return unifiedProductList;

    }
    /*
    public static void main(String[] args) {
        // Test the solution with some input
        String film = "The Force Awakens";
        String character = "Walter White";
      //  String result = run(film, character);
        try {
			fetchProductInfo();
			fetchProductPrices();
			 List<UnifiedProduct> unifiedProductList=combineApiResponses();
			 for(UnifiedProduct p:unifiedProductList) {
				 System.out.println(p.name());
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // System.out.println("Result: " + result);
    }*/
}
