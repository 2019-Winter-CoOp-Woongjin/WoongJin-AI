package edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.skku.woongjin_ai.Package_4_3_GameList.GameListActivity;
import edu.skku.woongjin_ai.R;

public class WrongBombFragment extends AppCompatActivity {
    Intent intent, intent_gamelist;
    private DatabaseReference mPostReference;
    Button quit;
    String id_key, nickname_key, user1_key, user2_key;
    int check1, check2;
    String loser_nickname, winner_nickname;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bombwrong);

        intent = getIntent();
        intent_gamelist = new Intent(WrongBombFragment.this, GameListActivity.class);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
        quit = (Button) findViewById(R.id.quit);
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        check1 = 0;
        check2 = 0;

        final ValueEventListener findloser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String gamer_nickname = postSnapshot.child("nickname").getValue().toString();
                    if (key.equals(id_key)) {
                        if (gamer_nickname.equals(user1_key)) {
                            loser_nickname = user1_key;
                            winner_nickname = user2_key;
                            break;
                        }
                        else if (gamer_nickname.equals(user2_key)) {
                            loser_nickname = user2_key;
                            winner_nickname = user1_key;
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(findloser);

        final ValueEventListener providecoin = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String gnickname = postSnapshot.child("nickname").getValue().toString();
                    String gcoin = postSnapshot.child("coin").getValue().toString();
                    if (check1 == 0 && gnickname.equals(loser_nickname)) { //본인이 진 상태
                        int coin = Integer.parseInt(gcoin);
                        if (coin >= 30) {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                            mPostReference.child(key + "/my_coin_list/" + today + "/get").setValue("-30");
                            mPostReference.child(key + "/my_coin_list/" + today + "/why").setValue("폭탄이 터졌어요. ");

                            coin = coin - 30;
                            String coin_convert = Integer.toString(coin);
                            mPostReference.child(key).child("coin").setValue(coin_convert);
                        } else if (coin < 30) {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                            mPostReference.child(key + "/my_coin_list/" + today + "/get").setValue("-"+Integer.toString(coin));
                            mPostReference.child(key + "/my_coin_list/" + today + "/why").setValue("폭탄이 터졌어요. ");

                            mPostReference.child(key).child("coin").setValue("0");
                        }
                        check1 = 1;
                    }
                    else if (check2 == 0 && gnickname.equals(winner_nickname)) { //본인이 진 상태
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                        mPostReference.child(key + "/my_coin_list/" + today + "/get").setValue("30");
                        mPostReference.child(key + "/my_coin_list/" + today + "/why").setValue("상대방이 내 폭탄을 터트렸어요!");

                        int coin = Integer.parseInt(gcoin) + 30;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child(key).child("coin").setValue(coin_convert);
                        check2 = 1;
                    }
                    if (check1 == 1 && check2 == 1) {
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPostReference.addListenerForSingleValueEvent(providecoin);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_gamelist.putExtra("id", id_key);
                intent_gamelist.putExtra("nickname", nickname_key);
                startActivity(intent_gamelist);
                finish();
            }
        });
    }
}
