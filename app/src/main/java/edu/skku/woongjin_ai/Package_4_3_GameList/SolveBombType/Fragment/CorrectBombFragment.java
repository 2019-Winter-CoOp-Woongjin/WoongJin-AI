package edu.skku.woongjin_ai_winter.Package_4_3_GameList.SolveBombType.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.skku.woongjin_ai_winter.Package_4_3_GameList.GameListActivity;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.MakeBombType.MakeBombTypeActivity;
import edu.skku.woongjin_ai_winter.R;

public class CorrectBombFragment extends AppCompatActivity {
    Intent intent, intent_makebombtype, intent_gamelist;
    private DatabaseReference mPostReference;
    ImageButton send;
    TextView textCheckCorrect;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bombcorrect);

        intent = getIntent();
        intent_makebombtype = new Intent(CorrectBombFragment.this, MakeBombTypeActivity.class);

        send = (ImageButton) findViewById(R.id.send);

        textCheckCorrect = (TextView) findViewById(R.id.textCheck);

        timestamp_key = intent.getStringExtra("timestamp");
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        roomname_key = intent.getStringExtra("roomname");
        script_key = intent.getStringExtra("scriptnm");
        state_key = intent.getStringExtra("state");

        textCheckCorrect.setText("우와!!! " + id_key + "이(가) 정답을 맞췄어!!!");
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");

        final ValueEventListener findgamers = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String gamer_coin = postSnapshot.child("coin").getValue().toString();
                    if (key.equals(id_key)) {
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                        mPostReference.child(key + "/my_coin_list/" + today + "/get").setValue("20");
                        mPostReference.child(key + "/my_coin_list/" + today + "/why").setValue("폭탄 퀴즈를 안전하게 해체했어요!");
                        long weeknum=postSnapshot.child("my_week_list").getChildrenCount();
                        String gamer_count=postSnapshot.child("my_week_list/week"+weeknum+"/solvebomb").getValue().toString();

                        int solvedbomb=Integer.parseInt(gamer_count)+1;
                        int coin = Integer.parseInt(gamer_coin) + 20;
                        String coin_convert = Integer.toString(coin);
                        String solved_convert=Integer.toString(solvedbomb);
                        mPostReference.child(key).child("coin").setValue(coin_convert);
                        mPostReference.child(key).child("my_week_list/week"+weeknum+"/solvebomb").setValue(solvedbomb);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPostReference.addListenerForSingleValueEvent(findgamers);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_gamelist = new Intent(CorrectBombFragment.this, GameListActivity.class);
                intent_gamelist.putExtra("id", id_key);
                intent_gamelist.putExtra("nickname", nickname_key);
                startActivity(intent_gamelist);
                finish();

                /*intent_makebombtype.putExtra("timestamp", timestamp_key);
                intent_makebombtype.putExtra("id", id_key);
                intent_makebombtype.putExtra("nickname", nickname_key);
                intent_makebombtype.putExtra("user1", user1_key);
                intent_makebombtype.putExtra("user2", user2_key);
                intent_makebombtype.putExtra("roomname", roomname_key);
                intent_makebombtype.putExtra("scriptnm", script_key);
                intent_makebombtype.putExtra("state", state_key);
                startActivity(intent_makebombtype);
                finish();*/
            }
        });
    }
}
