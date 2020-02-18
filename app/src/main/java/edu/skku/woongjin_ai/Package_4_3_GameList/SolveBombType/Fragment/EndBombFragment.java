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

public class EndBombFragment extends AppCompatActivity {
    Intent intent, intent_gamelist;
    private DatabaseReference mPostReference;
    Button end;
    String id_key, nickname_key, user1_key, user2_key;
    int check1, check2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bombend);

        intent = getIntent();
        intent_gamelist = new Intent(EndBombFragment.this, GameListActivity.class);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
        end = (Button) findViewById(R.id.end);
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        check1 = 0;
        check2 = 0;

        //uploadFirebaseUserCoinInfo(user1_key, user2_key);

        final ValueEventListener findgamers = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    String today = new SimpleDateFormat("yyMMddHHmm").format(date);

                    String key = postSnapshot.getKey();
                    String gamer_nickname = postSnapshot.child("nickname").getValue().toString();
                    String gamer_coin = postSnapshot.child("coin").getValue().toString();
                    if (check1 == 0 && gamer_nickname.equals(user1_key)) {
                        long weeknum=postSnapshot.child("my_week_list").getChildrenCount();
                        String gamer_count=postSnapshot.child("my_week_list/week"+weeknum+"/solvebomb").getValue().toString();
                        int solvedbomb=Integer.parseInt(gamer_count)+1;
                        String solved_convert=Integer.toString(solvedbomb);
                        mPostReference.child(key).child("my_week_list/week"+weeknum+"/solvebomb").setValue(solvedbomb);
                        int coin = Integer.parseInt(gamer_coin) + 60;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child(key).child("coin").setValue(coin_convert);
                        mPostReference.child("my_coin_list/" + today + "/get").setValue("60");
                        mPostReference.child("my_coin_list/" + today + "/why").setValue(user2_key+"친구와의 폭탄 게임을 성공적으로 마쳤어요. ");
                        check1 = 1;
                    }
                    if (check2 == 0 && gamer_nickname.equals(user2_key)) {
                        int coin = Integer.parseInt(gamer_coin) + 60;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child("my_coin_list/" + today + "/get").setValue("60");
                        mPostReference.child("my_coin_list/" + today + "/why").setValue(user1_key+"친구와의 폭탄 게임을 성공적으로 마쳤어요. ");
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
        mPostReference.addListenerForSingleValueEvent(findgamers);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_gamelist.putExtra("id", id_key);
                intent_gamelist.putExtra("nickname", nickname_key);
                startActivity(intent_gamelist);
                finish();
            }
        });
    }

    /*
    private void uploadFirebaseUserCoinInfo(String nick1, String nick2){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child(nick1 + "/my_coin_list/" + today + "/get").setValue("60");
                mPostReference.child(nick1 + "/my_coin_list/" + today + "/why").setValue(nick2+"친구와의 폭탄 게임을 성공적으로 마쳤어요. ");

                mPostReference.child(nick2 + "/my_coin_list/" + today + "/get").setValue("60");
                mPostReference.child(nick2 + "/my_coin_list/" + today + "/why").setValue(nick1+"친구와의 폭탄 게임을 성공적으로 마쳤어요. ");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

     */

}