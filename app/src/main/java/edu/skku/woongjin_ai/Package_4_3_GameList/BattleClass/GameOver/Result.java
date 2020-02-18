package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.GameOver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSetting.GamePage;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSolving.GamePage_solve;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.GamePlayer;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.MainPage;
import edu.skku.woongjin_ai_winter.R;

import static edu.skku.woongjin_ai_winter.R.drawable.heart_empty;

public class Result extends AppCompatActivity {

    private TextView player0name, player1name, round, result,timer;
    private ImageView back, life0_1, life0_2, life0_3, life1_1, life1_2, life1_3, resultImage;

    //intent 되고난 후 activity 지우기 위한 설정
    public static Result activity = null;

    //라이프 변화를 위한 변수
    private int life0, life1;

    //firebase 선언
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //상대 고유 ID
    private int enemyID;

    //결과
    private int resultInt;

    //게임 끝나고 기다릴 시간 변수
    private int time;

    //전역변수 선언
    GlobalApplication gv;

    //비정상 종료 표시를 위한 변수
    private int unusual;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battleresult);

        player0name = findViewById(R.id.player0name);
        player1name = findViewById(R.id.player1name);
        round = findViewById(R.id.Round);
        result = findViewById(R.id.Result);
        timer = findViewById(R.id.Timer);
        resultImage = findViewById(R.id.ResultImage);

        //라이프 선언
        life0_1 = findViewById(R.id.player0life1);
        life0_2 = findViewById(R.id.player0life2);
        life0_3 = findViewById(R.id.player0life3);
        life1_1 = findViewById(R.id.player1life1);
        life1_2 = findViewById(R.id.player1life2);
        life1_3 = findViewById(R.id.player1life3);

        back = findViewById(R.id.Back);

        gv = (GlobalApplication) getApplication();

        time = 6;
        unusual = 0;

        //handler 설정
        handler.sendEmptyMessage(0);

        Intent intent = getIntent();
        resultInt = intent.getIntExtra("result", 0);

        //전 액티비티 지우기 위한 코드
        if(GamePage.activity != null) {
            GamePage activity = GamePage.activity;
            activity.finish();
        }
        //전 액티비티 지우기 위한 코드
        if(GamePage_solve.activity != null) {
            GamePage_solve activity = GamePage_solve.activity;
            activity.finish();
        }

        enemyID = (gv.getGameID() + 1) % 2;
    }

    protected void onStart() {
        super.onStart();

        if(resultInt == 1) {
            result.setText("이겼어요!");
            resultImage.setImageResource(R.drawable.icon_win);
        }
        else if (resultInt == 2) {
            result.setText("비겼어요");
            resultImage.setImageResource(R.drawable.icon_draw);
        }
        else if (resultInt == 3) {
            result.setText("졌어요..");
            resultImage.setImageResource(R.drawable.icon_lose);
        }

        databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("problem0").exists())
                    databaseReference.child("game").child("room").child(""+gv.getRoomnum()).child("problem0").removeValue();
                if (dataSnapshot.child("problem1").exists())
                    databaseReference.child("game").child("room").child(""+gv.getRoomnum()).child("problem1").removeValue();
                //상대가 존재할 때
                if (dataSnapshot.child("player"+enemyID).exists()) {
                    player0name.setText(dataSnapshot.child("player0").getValue(GamePlayer.class).name);
                    player1name.setText(dataSnapshot.child("player1").getValue(GamePlayer.class).name);
                    round.setText("Round " + (dataSnapshot.child("round").getValue(int.class)));

                    //라이프 표시
                    life0 = dataSnapshot.child("player0").child("life").getValue(int.class);
                    life1 = dataSnapshot.child("player1").child("life").getValue(int.class);

                    if (life0 == 3) ;
                    else if (life0 == 2) {
                        life0_3.setImageResource(heart_empty);
                    } else if (life0 == 1) {
                        life0_3.setImageResource(heart_empty);
                        life0_2.setImageResource(heart_empty);
                    } else if (life0 == 0) {
                        life0_3.setImageResource(heart_empty);
                        life0_2.setImageResource(heart_empty);
                        life0_1.setImageResource(heart_empty);
                    }

                    if (life1 == 3) ;
                    else if (life1 == 2) {
                        life1_1.setImageResource(heart_empty);
                    } else if (life1 == 1) {
                        life1_1.setImageResource(heart_empty);
                        life1_2.setImageResource(heart_empty);
                    } else if (life1 == 0) {
                        life1_1.setImageResource(heart_empty);
                        life1_2.setImageResource(heart_empty);
                        life1_3.setImageResource(heart_empty);
                    }
                }
                //상대가 나가서 이긴 경우
                else {
                    unusual = 1;
                    if (gv.getGameID() == 0) {
                        player0name.setText(dataSnapshot.child("player0").getValue(GamePlayer.class).name);
                        player1name.setText("NONE");
                        round.setText("Round " + (dataSnapshot.child("round").getValue(int.class) - 1));

                        //라이프 표시
                        life0 = dataSnapshot.child("player0").child("life").getValue(int.class);
                        life1 = 0;

                        if (life0 == 3) ;
                        else if (life0 == 2) {
                            life0_3.setImageResource(heart_empty);
                        } else if (life0 == 1) {
                            life0_3.setImageResource(heart_empty);
                            life0_2.setImageResource(heart_empty);
                        } else if (life0 == 0) {
                            life0_3.setImageResource(heart_empty);
                            life0_2.setImageResource(heart_empty);
                            life0_1.setImageResource(heart_empty);
                        }

                        life1_1.setImageResource(heart_empty);
                        life1_2.setImageResource(heart_empty);
                        life1_3.setImageResource(heart_empty);
                    }

                    else {
                        player0name.setText("NONE");
                        player1name.setText(dataSnapshot.child("player1").getValue(GamePlayer.class).name);
                        round.setText("Round " + (dataSnapshot.child("round").getValue(int.class) - 1));

                        //라이프 표시
                        life0 = 0;
                        life1 = dataSnapshot.child("player1").child("life").getValue(int.class);

                        life0_3.setImageResource(heart_empty);
                        life0_2.setImageResource(heart_empty);
                        life0_1.setImageResource(heart_empty);

                        if (life1 == 3) ;
                        else if (life1 == 2) {
                            life1_1.setImageResource(heart_empty);
                        } else if (life1 == 1) {
                            life1_1.setImageResource(heart_empty);
                            life1_2.setImageResource(heart_empty);
                        } else if (life1 == 0) {
                            life1_1.setImageResource(heart_empty);
                            life1_2.setImageResource(heart_empty);
                            life1_3.setImageResource(heart_empty);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(Result.this);
                alert.setMessage("프로그램을 종료하시겠습니까?");
                alert.setTitle("게임 종료")
                        .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (unusual == 0) {
                                    handler.removeMessages(0);
                                    finish();
                                }
                                else {
                                    if (gv.getGameID() == 0) {
                                        handler.removeMessages(0);
                                        finish();
                                    }
                                    else {
                                        databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).removeValue();
                                        if(MainPage.activity != null) {
                                            MainPage activity = MainPage.activity;
                                            activity.finish();
                                        }
                                        handler.removeMessages(0);
                                        finish();
                                    }
                                }

                            }
                        })
                        .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                alert.show();
            }


        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            time--;
            timer.setText(""+time);

            if (time == 0){
                if (unusual == 0) {
                    handler.removeMessages(0);
                    finish();
                }
                else {
                    if (gv.getGameID() == 0) {
                        handler.removeMessages(0);
                        finish();
                    }
                    else {
                        databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).removeValue();
                        if(MainPage.activity != null) {
                            MainPage activity = MainPage.activity;
                            activity.finish();
                        }
                        handler.removeMessages(0);
                        finish();
                    }
                }
            }

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(Result.this);
        alert.setMessage("게임방으로 돌아가시겠습니까?");
        alert.setTitle("게임 종료")
                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (unusual == 0) {
                            handler.removeMessages(0);
                            finish();
                        }
                        else {
                            if (gv.getGameID() == 0) {
                                handler.removeMessages(0);
                                finish();
                            }
                            else {
                                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).removeValue();
                                if(MainPage.activity != null) {
                                    MainPage activity = MainPage.activity;
                                    activity.finish();
                                }
                                handler.removeMessages(0);
                                finish();
                            }
                        }

                    }
                })
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        alert.show();
    }
}
