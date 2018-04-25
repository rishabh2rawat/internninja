package com.rishabhrawat.internninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class IntroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView textContinue;
    SharedPreferences sharedPreferences;
    Boolean firstTime;

    CircleImageView profileImage;
    TextView profilename;
    TextView profileemail;
    TextView profilephone;
    TextView profileweb;
    String name;
    String email;
    Uri photoUrl;
    String uid;
    String phoneno;

    private DatabaseReference mDatabase, mDatabase1, mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean("firstTime", true);

        if (firstTime) {
            textContinue = (TextView) findViewById(R.id.textcontinue);
            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };

            textContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstTime = false;
                    editor.putBoolean("firstTime", firstTime);
                    editor.apply();
                    Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url
                name = user.getDisplayName();
                email = user.getEmail();
                photoUrl = user.getPhotoUrl();
                uid = user.getUid();
                phoneno = user.getPhoneNumber();


            }

            /*linking layout item*/
            profileImage = (CircleImageView) findViewById(R.id.profile_photo);
            profilename = (TextView) findViewById(R.id.profile_name);
            profileemail = (TextView) findViewById(R.id.profile_email);
            profilephone = (TextView) findViewById(R.id.profile_phone);
            profileweb = (TextView) findViewById(R.id.profile_web);

            Glide.with(getBaseContext()).load(photoUrl).into(profileImage);
            profilename.setText(name);
            profileemail.setText(email);
            profilephone.setText(phoneno);
            profileweb.setText("https://www.rishabhisin.com");


        } else {

            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
