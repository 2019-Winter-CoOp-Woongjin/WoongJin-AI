package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.skku.woongjin_ai.GlobalApplication;
import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.Game.ProblemSetting.GamePage;
import edu.skku.woongjin_ai.R;

public class MainPage extends AppCompatActivity {

    //필요한 layout 선언
    private TextView roomlocate, player0name, player1name, roomname, bookname;
    private ImageView player0image, player1image, back;
    private Button ready;

    //파이어베이스 사용을 위한 선언
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //전역변수 선언
    private GlobalApplication gv;

    private Boolean readyInt = true;

    //intent 되고난 후 activity 지우기 위한 설정
    public static MainPage activity = null;


    //이미지 불러오기
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://woongjin-ai.appspot.com");

    //폭탄이미지
    TextView VS;

    //인텐트
    private String id_key;
    private Intent intent;

    private ImageView imageHome;

    private int enemynum;

    //ready

    private TextView Player0Ready, Player1Ready;

    //책바꿈버튼
    private Button changeBook;

    //책바꼇을떄 제목
    private String changeBookName;
    private String script_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battlemainpage);

        //layout들 연동
        roomlocate = findViewById(R.id.RoomLocate);
        player0name = findViewById(R.id.Player0Name);
        player1name = findViewById(R.id.Player1Name);
        player0image = findViewById(R.id.Player0Image);
        player1image = findViewById(R.id.Player1Image);
        back = findViewById(R.id.Back);
        ready = findViewById(R.id.Ready);


        //player0image.setBackgroundResource(R.drawable.photo_frame_ready);

        gv = (GlobalApplication) getApplication();

        roomname = findViewById(R.id.RoomName);
        bookname = findViewById(R.id.BookName);

        enemynum = (gv.getGameID() + 1) % 2;

        //폭탄이미지 크게작게
        VS = findViewById(R.id.VS);
        Animation wave = AnimationUtils.loadAnimation(this, R.anim.wave);
        VS.startAnimation(wave);

        //받아오기
        intent = getIntent();
        id_key = intent.getExtras().getString("id");

        imageHome = (ImageView) findViewById(R.id.home_main);

        handler.sendEmptyMessage(0);

        //ready text

        Player0Ready = findViewById(R.id.Player0Ready);
        Player1Ready = findViewById(R.id.Player1Ready);

        //change book

        changeBook = findViewById(R.id.changeBook);

        //손님일경우 방바꾸는 버튼 안보이게
        if(gv.getGameID() == 1){
            changeBook.setVisibility(View.INVISIBLE);
        }

        //책바꿈버튼 눌럿을떄
        changeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //방장만 할수있또록
                if(gv.getGameID() == 0) {

                    Intent intentChange = new Intent(MainPage.this, ChangeBookActivity.class);
                    intentChange.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentChange);
                }

                else{

                    changeBook.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainPage.this, "방장만 책을 바꿀 수 있어요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //홈버튼 눌럿을떄
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gv.getGameID() == 1) {

                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).removeValue();

                    //게스트 나갔을때 personNum = 1 ㄱㄱ.
                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("personNum").setValue("1");

                    Intent intentHome = new Intent(MainPage.this, MainActivity.class);
                    intentHome.putExtra("id", id_key);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentHome);
                    finish();
                } else if (gv.getGameID() == 0) {

                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).removeValue();
                    handler.removeMessages(0);

                    Intent intentHome = new Intent(MainPage.this, MainActivity.class);
                    intentHome.putExtra("id", id_key);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentHome);
                    finish();
                }
            }
        });


        //ready버튼 누르면 데이터베이스에 ready변수를 1로 바꿔줌
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyInt == true) {
                    readyInt = false;
                    ready.setText("준비 완료");
                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("ready").setValue(1);
                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("life").setValue(3);

                } else {
                    readyInt = true;
                    ready.setText("준비");
                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("ready").setValue(0);


                }
            }
        });

        //뒤로가기 이미지 누르면 데이터베이스 삭제 후 종료
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainPage.this);
                alert.setMessage("게임방을 나가시겠습니까?");
                alert.setTitle("게임 종료")
                        .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (gv.getGameID() == 1) {
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).removeValue();
                                    //게스트 나갔을때 personNum = 1 ㄱㄱ.
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("personNum").setValue("1");
                                } else if (gv.getGameID() == 0)
                                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).removeValue();
                                handler.removeMessages(0);
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(MainPage.this);
        alert.setMessage("게임방을 나가시겠습니까?");
        alert.setTitle("게임 종료")
                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeMessages(0);
                        finish();
                        if (gv.getGameID() == 1) {
                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).removeValue();

                            //게스트 나갔을때 personNum = 1 ㄱㄱ.
                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("personNum").setValue("1");
                        } else if (gv.getGameID() == 0)
                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).removeValue();
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    //상대 존재할 때
                    if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + enemynum).exists()) {

                        changeBookName = dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("bookName").getValue(String.class);
                        script_text = dataSnapshot.child("script_list").child("" + changeBookName).child("text").getValue(String.class);
                        gv.setScript(script_text);
                        Log.e("gv22", gv.getScript());

                        if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").getValue(GamePlayer.class).ready == 1) {

                            player0image.setBackgroundResource(R.drawable.photo_frame_ready);

                            //노란색으로 준비완료
                            Player0Ready.setTextColor(Color.rgb(255, 193, 7));


                        } else if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").getValue(GamePlayer.class).ready == 0) {
                            player0image.setBackgroundResource(R.drawable.photo_frame_transparent);

                            //투명색으로 준비
                            Player0Ready.setTextColor(Color.TRANSPARENT);
                        }

                        //1있을떄로
                        if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").exists()) {

                            if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").getValue(GamePlayer.class).ready == 1) {

                                player1image.setBackgroundResource(R.drawable.photo_frame_ready);

                                //노란색으로 준비완료
                                Player1Ready.setTextColor(Color.rgb(255, 193, 7));


                            } else if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").getValue(GamePlayer.class).ready == 0) {

                                player1image.setBackgroundResource(R.drawable.photo_frame_transparent);

                                //투명색으로 준비
                                Player1Ready.setTextColor(Color.TRANSPARENT);

                            }
                        }


                        //데이터베이스에서 player0와 player1의 이름 받아옴
                        player0name.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").child("name").getValue(String.class));
                        String player0id = dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").child("id").getValue(String.class);
                        String url0 = dataSnapshot.child("user_list").child("" + player0id).child("profile").getValue(String.class);

                        //System.out.println("이미지주소"+url0);
                        if (url0.equals("noimage")) {
                            player1image.setImageResource(R.drawable.noone);
                        } else {
                            url0 = "profile/" + url0;
                            StorageReference pathReference = storageReference.child(url0);
                            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(MainPage.this).load(uri).into(player0image);
                                }
                            });
                        }

                        //Glide.with(MainPage.this).load(url0).into(player0image);
                   /* if (url0.equals("noimage")) {
                        player0image.setImageResource(R.drawable.noone);
                    }
                    else {
                        Glide.with(MainPage.this).load(url0).into(player0image);
                    }*/

                        //원래는 이미지도 바꿔줘야함!!!!!!!!!!!
                        roomlocate.setText(gv.getRoomnum() + "번 방");

                        //방이름, 책이름 받아오기
                        bookname.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("bookName").getValue(String.class));
                        roomname.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("roomName").getValue(String.class));

                        player1name.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").getValue(GamePlayer.class).name);
                        String player1id = dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").child("id").getValue(String.class);
                        String url1 = dataSnapshot.child("user_list").child("" + player1id).child("profile").getValue(String.class);


                        //System.out.println("이미지주소"+url0);
                        if (url1.equals("noimage")) {
                            player1image.setImageResource(R.drawable.noone);
                        } else {
                            url1 = "profile/" + url1;
                            StorageReference pathReference = storageReference.child(url1);
                            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(MainPage.this).load(uri).into(player1image);
                                }
                            });
                        }


                        //System.out.println("이미지주소"+url1);
                        /*if (url1.equals("noimage")) {
                            player1image.setImageResource(R.drawable.noone);
                        }
                        else {
                            Glide.with(MainPage.this).load(url1).into(player1image);
                        }*/

                        //Glide.with(MainPage.this).load(url1).into(player1image);
                        //둘다 준비가 완료되면 게임 시작
                        if (dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").getValue(GamePlayer.class).ready == 1 && dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player1").getValue(GamePlayer.class).ready == 1) {

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).child("ready").setValue(0);
                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("round").setValue(1);
                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).setValue(new GamePlayer(gv.getGlobalName(), gv.getGameID(), gv.getGlobalID(), 0, 0, 0, 3));
                            gv.setLife(3);
                            gv.setBookName(changeBookName);

                            ready.setText("준비");
                            readyInt = true;

                            handler.removeMessages(0);

                            Intent intent = new Intent(MainPage.this, GamePage.class);
                            startActivity(intent);
                        }
                    }
                    //상대가 없을 때
                    else {
                        if (gv.getGameID() == 1) {
                            handler.removeMessages(0);
                            finish();
                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).removeValue();
                        } else {
                            player1name.setText("NONE");
                            player1image.setImageResource(R.drawable.noone);
                            //원래는 이미지도 바꿔줘야함!!!!!!!!!!!
                            roomlocate.setText(gv.getRoomnum() + "번 방");

                            player0name.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").child("name").getValue(String.class));
                            String player0id = dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("player0").child("id").getValue(String.class);
                            String url0 = dataSnapshot.child("user_list").child("" + player0id).child("profile").getValue(String.class);

                            //System.out.println("이미지주소"+url0);
                            if (url0.equals("noimage")) {
                                player1image.setImageResource(R.drawable.noone);
                            } else {
                                url0 = "profile/" + url0;
                                StorageReference pathReference = storageReference.child(url0);
                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(MainPage.this).load(uri).into(player0image);
                                    }
                                });
                            }


                            //방이름, 책이름 받아오기
                            bookname.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("bookName").getValue(String.class));
                            roomname.setText(dataSnapshot.child("gameroom_list2").child("" + gv.getRoomnum()).child("roomName").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
}