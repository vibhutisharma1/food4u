package com.example.food4u;

import java.util.Arrays;

public class HealthLabel {

    String [] health_tags;

    public HealthLabel(){
        health_tags = new String[]{
                "alcohol-free",
                "immuno-supportive",
                "celery-free",
                "crustacean-free",//didnt include
                "dairy-free",
                "egg-free",
                "fish-free",
                "fodmap-free",
                "gluten-free",
                "keto-friendly",
                "kidney-friendly",
                "kosher",
                "low-potassium",
                "lupine-free",
                "mustard-free",
                "low-fat-abs",//start here
                "No-oil-added",
                "low-sugar",
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
        // return Arrays.asList(health_tags).lastIndexOf(health) != -1? true : false;

    }

}
