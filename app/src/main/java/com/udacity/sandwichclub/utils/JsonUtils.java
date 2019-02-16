package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
        try {
            List<String> alsoKnownAs = new ArrayList<>();
            List<String> ingredients = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            sandwich.setMainName(jsonObject.getJSONObject("name").getString("mainName"));
            JSONArray alsoKnownJsonArray = jsonObject.getJSONObject("name").getJSONArray("alsoKnownAs");
            for (int i = 0; i < alsoKnownJsonArray.length(); i++) {
                alsoKnownAs.add(alsoKnownJsonArray.get(i).toString());
            }
            sandwich.setAlsoKnownAs(alsoKnownAs);
            sandwich.setDescription(jsonObject.getString("description"));
            sandwich.setImage(jsonObject.getString("image"));
            JSONArray ingredientsJsonArray = jsonObject.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                ingredients.add(ingredientsJsonArray.get(i).toString());
            }
            sandwich.setIngredients(ingredients);
            sandwich.setPlaceOfOrigin(jsonObject.getString("placeOfOrigin"));
        } catch (Exception e) {
            Log.d("JsonUtils", e.getMessage());
            return null;
        }
        return sandwich;
    }
}
