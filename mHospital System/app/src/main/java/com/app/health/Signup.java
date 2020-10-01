package com.app.health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Signup extends AppCompatActivity {

    private EditText fullname,email,password,mobnumber;
    private RadioButton male,female;
    private Button signup;
    public static int APP_REQUEST_CODE = 99;
    private ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        //Handle new or logged out userCopy Code




        user = ParseUser.getCurrentUser();

        if(user!=null){
            Intent in = new Intent(Signup.this,Profile.class);
            startActivity(in);
        }



        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pass);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        signup = (Button) findViewById(R.id.signup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkinput()) {
                    ParseUser user = new ParseUser();
                    user.setUsername(email.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.put("fullname", fullname.getText().toString());
                    if (male.isChecked()) {
                        user.put("gender", "MALE");
                    } else {
                        user.put("gender", "FEMALE");
                    }
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
phoneLogin();
                            } else {
                                ParseUser.logOut();
                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean checkinput(){

        boolean check = false;

        if(!fullname.getText().toString().equals("") && !email.getText().toString().equals("") &&  !password.getText().toString().equals("")){
            if(male.isChecked() || female.isChecked()){
                check = true;

            }
            else{
                Toast.makeText(Signup.this, "Please select gender", Toast.LENGTH_LONG).show();

            }
        }
        else{
            Toast.makeText(Signup.this, "Somethign is missing", Toast.LENGTH_LONG).show();

        }

        return check;

    }

    public void phoneLogin() {
        final Intent intent = new Intent(Signup.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(Signup.this,toastMessage,Toast.LENGTH_LONG).show();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                AccessToken accessToken = AccountKit.getCurrentAccessToken();
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        String accountKitId = account.getId();
                        Toast.makeText(Signup.this,"Fvvfvf",Toast.LENGTH_LONG).show();
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("mobnumber",phoneNumberString);
                        user.saveInBackground();
                        Intent in = new Intent(Signup.this,MapsActivity.class);
                        startActivity(in);


                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error

                        Log.d("dcdc",error.toString());
                        Toast.makeText(Signup.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });


            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

}
