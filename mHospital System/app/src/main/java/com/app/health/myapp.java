package com.app.health;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by apple on 08/03/18.
 */

public class myapp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("TgsYP6lvffzIVFY4esSeThDkANBuQijNb8gyeuaA")
                .clientKey("zjylTw3sYl0IfzxUY6grszslT4oIu6vNIUIy1FwT")
                .server("https://parseapi.back4app.com/")   // '/' important after 'parse'
                .build());
    }
}
