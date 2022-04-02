package com.example.reciclerviewfirebasetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private ListView mUserList;
    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mKeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase= FirebaseDatabase.getInstance("https://listviewexample-10f09-default-rtdb.europe-west1.firebasedatabase.app/");
        mRef = mDatabase.getReference();
        mUserList= (ListView) findViewById(R.id.usersLV);

//        Returns a view for each object in a collection of data ... can be used with list-based user interface widgets such as ListView or Spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserNames);
        mUserList.setAdapter(arrayAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                mUserNames.add(value);

                String key = snapshot.getKey(); //01, 02, 03, 04
                mKeys.add(key); //[01], [01, 02], [01, 02, 03], [01, 02, 03, 04]

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String value = snapshot.getValue(String.class);
                String key = snapshot.getKey();

                int index = mKeys.indexOf(key);//gets the index of the matching key that was just updated
                mUserNames.set(index, value);//overrides the value of that index

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}