package com.app.health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class admin extends AppCompatActivity {

    ListView listView;
    ArrayList<String> name;
    ArrayList<String> email;
    ArrayList<String> id;
    ArrayList<String> his;
    private TextView namee;
    private EditText history;
    private Button save,back;
    private LinearLayout editt;
    private List<ParseUser> users;
    private String selectedid = null;
    private int position = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
name = new ArrayList<>();
        email = new ArrayList<>();
        id = new ArrayList<>();
        his = new ArrayList<>();

final ParseUser user = ParseUser.getCurrentUser();


        listView = (ListView) findViewById(R.id.list);

        editt = (LinearLayout) findViewById(R.id.editt);
        namee = (TextView) findViewById(R.id.name);
        history = (EditText) findViewById(R.id.history);
        save  = (Button) findViewById(R.id.save);
back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editt.setVisibility(View.GONE);

            }
        });
        editt.setVisibility(View.GONE);


        ParseQuery<ParseUser> innerQuery = ParseQuery.getUserQuery();
        innerQuery.whereExists("objectId");

       innerQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> commentList, ParseException e) {
                if(e==null) {
                    users = commentList;
                    for (int i = 0; i < commentList.size(); i++) {


                        name.add(i,commentList.get(i).getString("fullname"));
                        email.add(i,commentList.get(i).getUsername().toString());
                        id.add(i,commentList.get(i).getObjectId().toString());
                    }
                    Toast.makeText(admin.this,String.valueOf(commentList.size())+ " users",Toast.LENGTH_LONG).show();

    LazyAdapter adapter = new LazyAdapter(admin.this, name, email);
    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positionn, long idd) {
                            selectedid = id.get(positionn);
position = positionn;
                            namee.setText(users.get(positionn).getString("fullname")+" Medical History");
                            editt.setVisibility(View.VISIBLE);
                            if(users.get(positionn).getString("phistory")!=null) {
                                history.setText(users.get(positionn).getString("phistory").toString());
                            }

                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ParseUser uu = users.get(position);
                                    uu.put("phistory",history.getText().toString());
                                    uu.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Toast.makeText(admin.this,"Data Saved",Toast.LENGTH_LONG).show();

                                        }
                                    });

                                }
                            });


                        }
                    });

                }
                else{
                    Toast.makeText(admin.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }            }
        });


    }
}
