package com.rishabhrawat.internninja;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase,mDatabase1,mDatabase2;

    EditText expence;
    EditText description;
    Button addbtn,plotbtn;
    ListView listViewdesc,listViewexp;
    View clickSource;
    View touchSource;

    int offset = 0;

    int exp;
    String ex;
    String desc;
    private ArrayList<String> mDescription=new ArrayList<String>();
    private ArrayList<Long> mExpence=new ArrayList<Long>();
    private ArrayList<Integer> mExp=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        /*****************connecting to the layout xml file************************/

        expence = (EditText) findViewById(R.id.expence);
        description = (EditText) findViewById(R.id.desc);
        addbtn = (Button) findViewById(R.id.addbtn);
        listViewdesc = (ListView) findViewById(R.id.listdesc);
        listViewexp=(ListView)findViewById(R.id.listexp);
        plotbtn=(Button) findViewById(R.id.plotbtn);




        listViewdesc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listViewexp.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        listViewexp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listViewdesc.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        /*****checking user session*****************/
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        /******realtime database refrence************************/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("description");

     final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDescription);
        listViewdesc.setAdapter(arrayAdapter);

     mDatabase1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String valuedesc = dataSnapshot.getValue(String.class);
                mDescription.add(valuedesc);
                arrayAdapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*-----------------------------------------------expence-----------------------------------*/
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("expence");
        final ArrayAdapter<Long> arrayAdapter1 = new ArrayAdapter<Long>(this, android.R.layout.simple_list_item_1, mExpence);
        listViewexp.setAdapter(arrayAdapter1);

        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              Long valueexp = dataSnapshot.getValue(Long.class);
                mExpence.add(valueexp);
                arrayAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for(int i=0;i<mExpence.size();i++)
        {
            int a=mExpence.get(i).intValue();
            mExp.add(a);
            Log.d("Rishabh", "onCreate: +++++++++++++++++++++++++++"+mExp.get(i));
        }


        /**********addbtn on Click ********************/

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ex = expence.getText().toString();
                desc = description.getText().toString();

                if(ex.equals("")||desc.equals(""))
                {
                    Toast.makeText(HomeActivity.this, "Enter the values", Toast.LENGTH_SHORT).show();
                }
                else {

                    exp=Integer.parseInt(ex);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabase1 = FirebaseDatabase.getInstance().getReference();
                    mDatabase1.child(user.getUid()).child("description").child(desc).setValue(desc);
                    mDatabase2 = FirebaseDatabase.getInstance().getReference();
                    mDatabase2.child(user.getUid()).child("expence").child(desc).setValue(exp);

                    expence.setText("");
                    description.setText("");
                }
            }
        });







        /*-----------------------------plot btn On click listener------------------------------------------*/
        plotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<mExpence.size();i++)
                {
                    int a=mExpence.get(i).intValue();
                    mExp.add(a);
                    Log.d("Rishabh", "onCreate: +++++++++++++++++++++++++++"+mExp.get(i));
                }

                Intent intent=new Intent(HomeActivity.this,PlotActivity.class);

                intent.putIntegerArrayListExtra("Expence",mExp);
                intent.putStringArrayListExtra("Description",mDescription);
                startActivity(intent);
                finish();
            }
        });



    }
    // menue item in the top corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);

        return super.onCreateOptionsMenu(menu);


    }

    //action for the options button ie logout about and notifiacations
    public void action(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.logout:
                mAuth.signOut();

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

}
