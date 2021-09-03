package com.company;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Main {

    private static HttpURLConnection connection;

    public static void main(String[] args) {

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL("https://fakestoreapi.com/products");
            connection = (HttpURLConnection) url.openConnection();

            //Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            //System.out.println(status);

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();

            }
            //System.out.println(responseContent.toString());
            parse(responseContent.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public static void parse(String responseBody) {
        JSONArray products = new JSONArray(responseBody);


        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < products.length(); i++) {
            jsonValues.add(products.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "title";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return valA.compareTo(valB);

            }
        });

        for (int i = 0; i < products.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }

        //System.out.println(sortedJsonArray);


        System.out.println(". Electronics");
        float total_electronics = 0;
        int electronics_count = 0;
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = sortedJsonArray.getJSONObject(i);
            if (!product.get("category").equals("electronics"))
                continue;

            String name = product.getString("title");
            System.out.println("... " + name);
            float price = product.getFloat("price");
            System.out.println("    Price: $" + price);
            total_electronics += product.getFloat("price");
            String description = product.getString("description");
            System.out.println("    " + description.substring(0, 10) + "...");

            electronics_count++;

        }
        System.out.println(". Clothes");
        float total_clothes=0;
        int clothes_count=0;
        for(int i=0; i<products.length();i++){
            JSONObject product = sortedJsonArray.getJSONObject(i);
            if(!(product.get("category").equals("men's clothing") || !product.get("category").equals("women's clothing")))
                continue;

            String name= product.getString("title");
            System.out.println("... "+name);
            float price = product.getFloat("price");
            System.out.println("    Price: $"+price);
            total_clothes+=product.getFloat("price");
            String description = product.getString("description");
            System.out.println("    "+description.substring(0,10) +"...");


            clothes_count++;

        }
        System.out.println("Electronics total cost: $" + total_electronics);
        System.out.println("Electronics items: " + electronics_count);
        System.out.println("Clothes total cost: $" + total_clothes);
        System.out.println("Clothes items: " + clothes_count);
    }
}
