package com.example.food4u;

import java.util.Arrays;

public class HealthLabel {

    String  [] health_tags;

    public HealthLabel(){
        health_tags = new String[]{
                "alcohol-free",
                "celery-free",
                "dairy-free",
                "egg-free",
                "fish-free",
                "fodmap-free",
                "gluten-free",
                "immuno-supportive",
                "keto-friendly",
                "kidney-friendly",
                "kosher",
                "low-fat-abs",
                "low-potassium",
                "low-sugar",
                "lupine-free",
                "mustard-free",
                "No-oil-added",
                "paleo",
                "peanut-free",
                "pecatarian",
                "pork-free",
                "red-meat-free",
                "sesame-free",
                "shellfish-free",
                "soy-free",
                "sugar-conscious",
                "tree-nut-free",
                "vegan",
                "vegetarian",
                "wheat-free"};

    }

    /**
     *
     * @param health: the tag the user selected
     * @return returns the proper string for the url
     */
    public static String apiHealthString(String health) {
         return "&health="+health;

    }

}
