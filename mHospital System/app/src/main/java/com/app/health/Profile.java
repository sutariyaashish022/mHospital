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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static com.app.health.Signup.APP_REQUEST_CODE;

public class Profile extends AppCompatActivity {

    private EditText fullname,bgroup,phistory;
    private Button emobnumber;
    private RadioButton male,female;
    private ParseUser user;
    private Button save,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = ParseUser.getCurrentUser();



        fullname = (EditText) findViewById(R.id.fullname);
        bgroup = (EditText) findViewById(R.id.bgroup);
        emobnumber = (Button) findViewById(R.id.emobnumber);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        phistory = (EditText) findViewById(R.id.phistory);

        save = (Button) findViewById(R.id.save);

        fullname.setText(user.get("fullname").toString());
        if(user.get("gender").toString().equals("MALE")){
            male.setChecked(true);
            female.setChecked(false);
        }
        else{
            male.setChecked(false);
            female.setChecked(true);
        }

        if(user.get("phistory")!=null){
            phistory.setText(user.get("phistory").toString());
        }

        if(user.get("bgroup")!=null){
            bgroup.setText(user.get("bgroup").toString());
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    user.put("fullname", fullname.getText().toString());
                    user.put("bgroup", bgroup.getText().toString());
                user.put("phistory",phistory.getText().toString());
                    if (male.isChecked()) {
                        user.put("gender", "MALE");
                    } else {
                        user.put("gender", "FEMALE");
                    }
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(Profile.this, "Deatil saved successfully.", Toast.LENGTH_LONG).show();
                        }
                    });

            }
        });


        emobnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLogin();
            }
        });

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.logOut();
                finish();
                Intent in = new Intent(Profile.this,Main.class);
                startActivity(in);
            }
        });

    }


    public void phoneLogin() {
        final Intent intent = new Intent(Profile.this, AccountKitActivity.class);
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



                AccessToken accessToken = AccountKit.getCurrentAccessToken();
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        String accountKitId = account.getId();
                        Toast.makeText(Profile.this,"Fvvfvf",Toast.LENGTH_LONG).show();
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("emobnumber",phoneNumberString);
                        user.saveInBackground();



                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error

                        Log.d("dcdc",error.toString());
                        Toast.makeText(Profile.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }

}
