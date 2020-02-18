package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSolving;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.GameOver.Result;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.GamePlayer;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.MainPage;
import edu.skku.woongjin_ai_winter.R;

import static edu.skku.woongjin_ai_winter.R.drawable.heart_empty;

public class GamePage_solve extends AppCompatActivity {

    private TextView player0name, player1name, timer, round, result;
    private ImageView back, life0_1, life0_2, life0_3, life1_1, life1_2, life1_3, resultImage;

    //intent 되고난 후 activity 지우기 위한 설정
    public static GamePage_solve activity = null;

    //라이프 변화를 위한 변수
    private int life0, life1;

    //문제 타입 저장 변수
    private int type;

    static final int GET_INT = 1;

    //firebase 선언
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //전역변수 선언
    GlobalApplication gv;

    //타이머를 위한 변수
    private int time;

    //라운드를 위한 변수
    private int roundnum;

    private int enemyID;

    private int life;

    //게임 시작 후 10초동안 안들어왔을 때 게임 종료하게 만들기 위한 변수
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battlegame_solve);

        player0name = findViewById(R.id.player0name);
        player1name = findViewById(R.id.player1name);
        timer = findViewById(R.id.Timer);
        round = findViewById(R.id.Round);
        result = findViewById(R.id.Result);
        back = findViewById(R.id.Back);
        resultImage = findViewById(R.id.ResultImage);

        //전역변수 선언
        gv = (GlobalApplication) getApplication();

        //라이프 선언
        life0_1 = findViewById(R.id.player0life1);
        life0_2 = findViewById(R.id.player0life2);
        life0_3 = findViewById(R.id.player0life3);
        life1_1 = findViewById(R.id.player1life1);
        life1_2 = findViewById(R.id.player1life2);
        life1_3 = findViewById(R.id.player1life3);

        //타이머 설정
        time = 31;
        //handler 설정
        handler.sendEmptyMessage(0);

        //전 액티비티 지우기 위한 코드
        if(GamePage.activity != null) {
            GamePage activity = GamePage.activity;
            activity.finish();
        }

        enemyID = (gv.getGameID() + 1) % 2;

        timeLeft = System.currentTimeMillis();

        //데이터베이스에서 필요한 데이터 가져옴
        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //적이 있을 때
                if (dataSnapshot.child("player" + enemyID).exists()) {
                    player0name.setText(dataSnapshot.child("player0").getValue(GamePlayer.class).name);
                    player1name.setText(dataSnapshot.child("player1").getValue(GamePlayer.class).name);
                    roundnum = dataSnapshot.child("round").getValue(int.class);
                    round.setText("Round " + roundnum);

                    type = dataSnapshot.child("problem" + enemyID).child("type").getValue(int.class);

                    //라이프 표시
                    life0 = dataSnapshot.child("player0").child("life").getValue(int.class);
                    life1 = dataSnapshot.child("player1").child("life").getValue(int.class);

                    if (life0 == 3) ;
                    else if (life0 == 2) {
                        life0_3.setImageResource(heart_empty);
                    } else if (life0 == 1) {
                        life0_3.setImageResource(heart_empty);
                        life0_2.setImageResource(heart_empty);
                    } else {
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
                    } else {
                        life1_1.setImageResource(heart_empty);
                        life1_2.setImageResource(heart_empty);
                        life1_3.setImageResource(heart_empty);
                    }

                    //OX문제일 때
                    if (type == 1) {
                        Intent intent = new Intent(GamePage_solve.this, SolveOX.class);
                        startActivityForResult(intent, GET_INT);
                    }
                    //객관식 문제일 때
                    else if (type == 2) {
                        Intent intent = new Intent(GamePage_solve.this, SolveMulti.class);
                        startActivityForResult(intent, GET_INT);
                    }
                    //주관식 문제일 때
                    else if (type == 3) {
                        Intent intent = new Intent(GamePage_solve.this, SolveShort.class);
                        startActivityForResult(intent, GET_INT);
                    }
                }

                //적이 나갔을 때
                else {
                    handler.removeMessages(0);
                    Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                    intentResult.putExtra("result", 1);
                    startActivity(intentResult);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GamePage_solve.this);
                alert.setMessage("게임을 나가시겠습니까? 게임을 나가시면 패배로 기록됩니다.");
                alert.setTitle("게임 종료")
                        .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                handler.removeMessages(0);
                                //전 액티비티 지우기 위한 코드
                                if (MainPage.activity != null) {
                                    MainPage activity = MainPage.activity;
                                    activity.finish();
                                }
                                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+enemyID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).removeValue();
                                            databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("problem"+gv.getGameID()).removeValue();
                                        }
                                        else {
                                            databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                finish();

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

    protected void onStart() {
        super.onStart();

        //앱 나간 시간이 10초 이상일 때 게임에서 튕기고 10초 안이면 안튕김
        long timeBack = System.currentTimeMillis();
        if (timeBack - timeLeft < 10000) {
        }
        else {
            databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).removeValue();
            AlertDialog.Builder alertOut = new AlertDialog.Builder(GamePage_solve.this);
            alertOut.setMessage("장기간 접속하지 않아 게임이 종료됩니다.");
            alertOut.setCancelable(false);
            alertOut.setTitle("게임 종료")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            handler.removeMessages(0);

                            if(MainPage.activity != null) {
                                MainPage activity = MainPage.activity;
                                activity.finish();
                            }
                            finish();
                        }
                    });

            alertOut.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //전역변수 사용을 위한 선언
        GlobalApplication gv = (GlobalApplication) getApplication();
        if (requestCode == GET_INT) {
            if(resultCode == RESULT_OK) {
                result.setText("정답입니다");
                resultImage.setImageResource(R.drawable.correct);
                gv.setSolve(1);
                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).child("solve").setValue(1);
            }
            else if(resultCode == RESULT_CANCELED) {
                life = gv.getLife();
                life--;
                gv.setLife(life);
                gv.setSolve(1);
                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).child("life").setValue(life);
                result.setText("오답입니다");
                resultImage.setImageResource(R.drawable.incorrect);
                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).child("solve").setValue(1);
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            time--;

            if (time == 1){
                if (gv.getSolve() == 0) {
                    //전 액티비티 지우기 위한 코드
                    if(SolveOX.activity != null) {
                        SolveOX activity = SolveOX.activity;
                        activity.finish();
                    }
                    //전 액티비티 지우기 위한 코드
                    if(SolveShort.activity != null) {
                        SolveShort activity = SolveShort.activity;
                        activity.finish();
                    }
                    //전 액티비티 지우기 위한 코드
                    if(SolveMulti.activity != null) {
                        SolveMulti activity = SolveMulti.activity;
                        activity.finish();
                    }
                }
            }

            //문제 출제 완료했을 때
            if(gv.getSolve() == 1) {
                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //상대 존재
                        roundnum = dataSnapshot.child("round").getValue(int.class);
                        if (dataSnapshot.child("player"+enemyID).exists()) {
                            //상대도 1이면 문제 만들기 종료
                            if (dataSnapshot.child("player"+enemyID).child("solve").getValue(int.class) == 1) {
                                int enemylife = dataSnapshot.child("player"+enemyID).child("life").getValue(int.class);
                                handler.removeMessages(0);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                //내가 죽었을 때
                                if (gv.getLife() <= 0) {
                                    //상대도 죽었을 때
                                    if (enemylife <= 0) {
                                        Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                                        intentResult.putExtra("result", 2);
                                        startActivity(intentResult);
                                        gv.setSolve(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                        finish();
                                    }
                                    //나만 죽었을 때
                                    else {
                                        Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                                        intentResult.putExtra("result", 3);
                                        startActivity(intentResult);
                                        gv.setSolve(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                        finish();
                                    }
                                }

                                //상대만 죽었을 때
                                else if (enemylife <= 0) {
                                    Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                                    intentResult.putExtra("result", 1);
                                    startActivity(intentResult);
                                    gv.setSolve(0);
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                    finish();
                                }

                                //라운드 수 5 됐을 때
                                else if (roundnum == 5) {
                                    //상대가 라이프가 더 많을 때
                                    if (enemylife > gv.getLife()) {
                                        Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                                        intentResult.putExtra("result", 3);
                                        startActivity(intentResult);
                                        gv.setSolve(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                        finish();
                                    }
                                    //내가 더 라이프가 많을 때
                                    else if (enemylife < gv.getLife()) {
                                        Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                                        intentResult.putExtra("result", 1);
                                        startActivity(intentResult);
                                        gv.setSolve(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                        finish();
                                    }
                                    //상대랑 라이프가 같을 때
                                    else if (enemylife == gv.getLife()) {
                                        Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                                        intentResult.putExtra("result", 2);
                                        startActivity(intentResult);
                                        gv.setSolve(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                        finish();
                                    }

                                }

                                //아무것도 아닐때
                                else {
                                    if (gv.getGameID() == 0) {
                                        roundnum++;
                                        databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("round").setValue(roundnum);
                                    }
                                    Intent intent = new Intent(GamePage_solve.this, GamePage.class);
                                    startActivity(intent);
                                    gv.setSolve(0);
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("solve").setValue(0);
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).removeValue();
                                    finish();
                                }

                            }
                        }
                        //상대 나갔을 때
                        else {
                            handler.removeMessages(0);
                            Intent intentResult = new Intent(GamePage_solve.this, Result.class);
                            intentResult.putExtra("result", 1);
                            startActivity(intentResult);
                            finish();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            if (time >= 0)
                timer.setText(""+time);
            else
                timer.setText("로딩중...");

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(GamePage_solve.this);
        alert.setMessage("게임을 나가시겠습니까? 게임을 나가시면 패배로 기록됩니다.");
        alert.setTitle("게임 종료")
                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeMessages(0);

                        //전 액티비티 지우기 위한 코드
                        if(MainPage.activity != null) {
                            MainPage activity = MainPage.activity;
                            activity.finish();
                        }

                        databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+enemyID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).removeValue();
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("problem"+gv.getGameID()).removeValue();
                                }
                                else {
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        finish();
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

    protected void onPause() {
        super.onPause();
        timeLeft = System.currentTimeMillis();
    }

}
