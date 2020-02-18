package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSetting;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizChoiceTypeInfo;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSolving.GamePage_solve;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.GameOver.Result;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.MainPage;
import edu.skku.woongjin_ai_winter.R;

import static edu.skku.woongjin_ai_winter.R.drawable.heart_empty;


public class GamePage extends AppCompatActivity {

    private TextView player0name, player1name, timer, round, problemText;
    private ImageButton ox, small, multi;
    private ImageView back, life0_1, life0_2, life0_3, life1_1, life1_2, life1_3;
    private LinearLayout problem;

    //firebase 선언
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    private GlobalApplication gv;

    static final int GET_INT = 1;
    //타이머를 위한 변수
    private int time;

    //라이프 변화를 위한 변수
    private int life0, life1;

    //intent 되고난 후 activity 지우기 위한 설정
    public static GamePage activity = null;

    //문제 제출 안했을 때를 위한 랜덤 변수
    private int randnum;

    private int enemyID;

    //게임 시작 후 10초동안 안들어왔을 때 게임 종료하게 만들기 위한 변수
    private long timeLeft;

    //책 이름
    private String bookName;
    //문제 가능 수
    private int problemNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battlegame);

        player0name = findViewById(R.id.player0name);
        player1name = findViewById(R.id.player1name);
        timer = findViewById(R.id.Timer);
        round = findViewById(R.id.Round);
        ox = findViewById(R.id.OX);
        small = findViewById(R.id.Short);
        multi = findViewById(R.id.Multi);
        back = findViewById(R.id.Back);
        problem = findViewById(R.id.problem);
        problemText = findViewById(R.id.problemText);

        //라이프 선언
        life0_1 = findViewById(R.id.player0life1);
        life0_2 = findViewById(R.id.player0life2);
        life0_3 = findViewById(R.id.player0life3);
        life1_1 = findViewById(R.id.player1life1);
        life1_2 = findViewById(R.id.player1life2);
        life1_3 = findViewById(R.id.player1life3);
        //전역변수 선언
        gv = (GlobalApplication) getApplication();
        time = 151;
        //handler 설정
        handler.sendEmptyMessage(0);

        //전 액티비티 지우기 위한 코드
        if(GamePage_solve.activity != null) {
            GamePage_solve activity = GamePage_solve.activity;
            activity.finish();
        }

        enemyID = (gv.getGameID() + 1) % 2;

        timeLeft = System.currentTimeMillis();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookName = dataSnapshot.child("gameroom_list2").child(""+gv.getRoomnum()).child("bookName").getValue(String.class);
                problemNum = (int) dataSnapshot.child("quiz_list").child(bookName).getChildrenCount();
                //상대 존재할 때
                if (dataSnapshot.child("gameroom_list2").child(""+gv.getRoomnum()).child("player" + enemyID).exists()) {
                    player0name.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").child("name").getValue(String.class));
                    player1name.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").child("name").getValue(String.class));
                    round.setText("Round " + dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("round").getValue(int.class));

                    life0 = dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").child("life").getValue(int.class);
                    life1 = dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").child("life").getValue(int.class);

                    //라이프 표시
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
                }
                //상대가 나갔을 때
                else {
                    handler.removeMessages(0);
                    Intent intentResult = new Intent(GamePage.this, Result.class);
                    intentResult.putExtra("result", 1);
                    startActivity(intentResult);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //OX문제 출제 버튼 눌렀을 때 팝업 창으로 이동
        ox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOX = new Intent(GamePage.this, PopupOX.class);
                startActivityForResult(intentOX, GET_INT);
            }
        });

        //단답형문제 출제 버튼 눌렀을 때 팝업 창으로 이동
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSmall = new Intent(GamePage.this, PopupSmall.class);
                startActivityForResult(intentSmall, GET_INT);
            }
        });

        //객관식문제 출제 버튼 눌렀을 때 팝업 창으로 이동
        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSmall = new Intent(GamePage.this, PopupMulti.class);
                startActivityForResult(intentSmall, GET_INT);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(GamePage.this);
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
        });

    }

    protected void onStart() {
        super.onStart();

        //앱 나간 시간이 10초 이상일 때 게임에서 튕기고 10초 안이면 안튕김
        long timeBack = System.currentTimeMillis();
        if (timeBack - timeLeft < 10000) {

        }
        else {
            handler.removeMessages(0);
            databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).removeValue();
            AlertDialog.Builder alertOut = new AlertDialog.Builder(GamePage.this);
            alertOut.setMessage("장기간 접속하지 않아 게임이 종료됩니다.");
            alertOut.setCancelable(false);
            alertOut.setTitle("게임 종료")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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

    //문제 만들었다는 intent return 받으면
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_INT) {
            if(resultCode == RESULT_OK) {
                problem.setVisibility(View.INVISIBLE);
                problemText.setText("친구가 문제를 내기까지 기다려요!");
                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).child("submit").setValue(1);
                gv.setProb(1);
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            time--;

            int enemyID = (gv.getGameID() + 1) % 2;

            if (time == 1){
                //문제 제출 못했을 때
                if (gv.getProb() == 0) {
                    gv.setProb(1);
                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).child("submit").setValue(1);
                    int li = gv.getLife();
                    li--;
                    gv.setLife(li);
                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).child("life").setValue(li);

                    //전 액티비티 지우기 위한 코드
                    if(PopupOX.activity != null) {
                        PopupOX activity = PopupOX.activity;
                        activity.finish();
                    }
                    //전 액티비티 지우기 위한 코드
                    if(PopupSmall.activity != null) {
                        PopupSmall activity = PopupSmall.activity;
                        activity.finish();
                    }
                    //전 액티비티 지우기 위한 코드
                    if(PopupMulti.activity != null) {
                        PopupMulti activity = PopupMulti.activity;
                        activity.finish();
                    }

                    Random rnd = new Random();
                    randnum = rnd.nextInt(problemNum);

                    databaseReference.child("quiz_list").child(gv.getBookName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int count = 0;
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                if (count == randnum - 1) {
                                    QuizChoiceTypeInfo prob = postSnapshot.getValue(QuizChoiceTypeInfo.class);
                                    Problem new_prob = new Problem();
                                    new_prob.type = prob.type;
                                    new_prob.answer = prob.answer;
                                    new_prob.problem = prob.question;
                                    if (new_prob.type == 2) {
                                        new_prob.ans1 = prob.answer1;
                                        new_prob.ans2 = prob.answer2;
                                        new_prob.ans3 = prob.answer3;
                                        new_prob.ans4 = prob.answer4;

                                        if (prob.answer.equals(prob.answer1))
                                            new_prob.answerNum = 1;
                                        else if (prob.answer.equals(prob.answer2))
                                            new_prob.answerNum = 2;
                                        else if (prob.answer.equals(prob.answer3))
                                            new_prob.answerNum = 3;
                                        else if (prob.answer.equals(prob.answer4))
                                            new_prob.answerNum = 4;
                                    }
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("problem"+gv.getGameID()).setValue(new_prob);
                                    break;
                                }
                                count++;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            //문제 출제 완료했을 때
            if(gv.getProb() == 1) {
                databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+enemyID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //상대 존재할 때
                        if (dataSnapshot.exists()) {
                            //상대도 1이면 문제 만들기 종료
                            if (dataSnapshot.child("submit").getValue(int.class) == 1) {
                                handler.removeMessages(0);
                                //멈춰줘서 한쪽만 실행되는 것 방지
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(GamePage.this, GamePage_solve.class);
                                startActivity(intent);
                                gv.setProb(0);
                                databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("submit").setValue(0);
                                finish();

                            }
                        }

                        else {
                            handler.removeMessages(0);
                            Intent intentResult = new Intent(GamePage.this, Result.class);
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

    //뒤로가기 눌렀을 때 handler 제거하기 위해 생성
    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(GamePage.this);
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

