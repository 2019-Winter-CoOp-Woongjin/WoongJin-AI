package edu.skku.woongjin_ai_winter.Package_4_3_GameList.MakeBombType;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.R;

public class MakeBombTypeActivity extends AppCompatActivity {
    Intent intent, intentHome, intentOX, intentChoice, intentShortword, intentTemplate;
    String id_key, scriptnm, backgroundID;
    String timestamp_key, nickname_key, user1_key, user2_key, roomname_key, state_key;
    ImageButton frameOX, frameChoice, frameShortword;
    ImageButton goHome;
    TextView script_title, textViewId;
    private DatabaseReference mPostReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makebombtype);

        goHome = (ImageButton) findViewById(R.id.home);
        frameOX = (ImageButton) findViewById(R.id.quiz_ox);
        frameChoice = (ImageButton) findViewById(R.id.quiz_choice);
        frameShortword = (ImageButton) findViewById(R.id.quiz_shortword);
        script_title = (TextView) findViewById(R.id.title);
        textViewId = (TextView) findViewById(R.id.makeQuiz);

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        nickname_key = intent.getStringExtra("nickname");
        timestamp_key = intent.getStringExtra("timestamp");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        state_key = intent.getStringExtra("state");
        roomname_key = intent.getStringExtra("roomname");
        mPostReference = FirebaseDatabase.getInstance().getReference().child("script_list");

        final ValueEventListener findBackgroundID = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    if (key.equals(scriptnm)) {
                        backgroundID = postSnapshot.child("background").getValue().toString();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPostReference.addListenerForSingleValueEvent(findBackgroundID);

        script_title.setText("지문 제목: " + scriptnm);
        textViewId.setText(nickname_key + "(이)가 직접 폭탄으로 보낼 문제를 만들어볼까?");

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MakeBombTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
            }
        });

        frameOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentOX = new Intent(MakeBombTypeActivity.this, MakeBombOXActivity.class);
                intentOX.putExtra("id", id_key);
                intentOX.putExtra("scriptnm", scriptnm);
                intentOX.putExtra("background", backgroundID);
                intentOX.putExtra("timestamp", timestamp_key);
                intentOX.putExtra("nickname", nickname_key);
                intentOX.putExtra("user1", user1_key);
                intentOX.putExtra("user2", user2_key);
                intentOX.putExtra("state", state_key);
                intentOX.putExtra("roomname", roomname_key);
                startActivity(intentOX);
                finish();
            }
        });

        frameChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentChoice = new Intent(MakeBombTypeActivity.this, MakeBombChoiceActivity.class);
                intentChoice.putExtra("id", id_key);
                intentChoice.putExtra("scriptnm", scriptnm);
                intentChoice.putExtra("background", backgroundID);
                intentChoice.putExtra("timestamp", timestamp_key);
                intentChoice.putExtra("nickname", nickname_key);
                intentChoice.putExtra("user1", user1_key);
                intentChoice.putExtra("user2", user2_key);
                intentChoice.putExtra("state", state_key);
                intentChoice.putExtra("roomname", roomname_key);
                startActivity(intentChoice);
                finish();
            }
        });

        frameShortword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShortword = new Intent(MakeBombTypeActivity.this, MakeBombShortwordActivity.class);
                intentShortword.putExtra("id", id_key);
                intentShortword.putExtra("scriptnm", scriptnm);
                intentShortword.putExtra("background", backgroundID);
                intentShortword.putExtra("timestamp", timestamp_key);
                intentShortword.putExtra("nickname", nickname_key);
                intentShortword.putExtra("user1", user1_key);
                intentShortword.putExtra("user2", user2_key);
                intentShortword.putExtra("state", state_key);
                intentShortword.putExtra("roomname", roomname_key);
                startActivity(intentShortword);
                finish();
            }
        });
    }
}
