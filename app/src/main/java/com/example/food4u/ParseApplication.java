package com.example.food4u;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9kuyoY7XnMW8bUHAzcGV9uFb3EprHB6sf6zEc1sP")
                .clientKey("JycRJliOVkOR1jCbBUCOo9h7DUBhmu0uoCOvlFYn")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
