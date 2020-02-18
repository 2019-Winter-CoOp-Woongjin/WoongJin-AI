
package edu.skku.woongjin_ai.Package_NotUsingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai.R;

public class AddFriendActivity extends AppCompatActivity {

    Intent intent;
    String id, friend = "";
    ListView mListview;
    ArrayList<String> friendList, randomFriendList;
    ArrayAdapter<String> randomFriendAdapter;
    private DatabaseReference mPostReference;
    //private DatabaseReference mPostReference2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        mListview = (ListView) findViewById(R.id.listview1);
        Button buttonAdd = (Button) findViewById(R.id.add);

        intent = getIntent();
        id = intent.getStringExtra("id");

        //mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");


        friendList = new ArrayList<String>();
        randomFriendList = new ArrayList<String>();
        randomFriendAdapter = new ArrayAdapter<String>(AddFriendActivity.this, android.R.layout.simple_list_item_1);
        mListview.setAdapter(randomFriendAdapter);
        getFirebaseDatabase();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friend.length() == 0) {
                    Toast.makeText(AddFriendActivity.this, "추가할 친구를 선택하세요", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String friendID = snapshot.getKey();
                    friendList.add(friendID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        //mPostReference.child("user_list/" + id + "/friend/").addValueEventListener(postListener);



    }


    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        Log.d("user key", key);
                        //if (key.equals(id_key))
                        friendList.add(key);
                    }
                    randomFriendAdapter.clear();
                    randomFriendAdapter.addAll(friendList);
                    randomFriendAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);

        } catch (java.lang.NullPointerException e) {

        }
    }
}



