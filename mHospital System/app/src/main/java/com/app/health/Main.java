package com.app.health;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marcoscg.fingerauth.FingerAuth;
import com.marcoscg.fingerauth.FingerAuthDialog;
import com.parse.ParseUser;

public class Main extends AppCompatActivity {

    private Button login,sigup,admin;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        user = ParseUser.getCurrentUser();
        if(user!=null){
            new FingerAuthDialog(this)
                    .setTitle("Sign in")
                    .setCancelable(false)
                    .setMaxFailedCount(3) // Number of attemps, default 3
                    .setPositiveButton("Login Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do something
                            user.logOut();
                            Intent in = new Intent(Main.this,Login.class);
                            startActivity(in);

                        }
                    })
                    .setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(Main.this, "onSuccess", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(Main.this,MapsActivity.class);
                            startActivity(in);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(Main.this, "onFailure", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(Main.this, "onError", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();

        }

        login = (Button) findViewById(R.id.login);
        sigup = (Button) findViewById(R.id.signup);
        admin = (Button) findViewById(R.id.admin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Main.this,Login.class);
                startActivity(in);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Main.this,admin.class);
                startActivity(in);
            }
        });
        sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Main.this,Signup.class);
                startActivity(in);
            }
        });
    }
}
