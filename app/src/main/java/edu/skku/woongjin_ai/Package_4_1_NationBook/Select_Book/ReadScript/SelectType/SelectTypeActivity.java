package edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.ChoiceTypeActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.OXTypeActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.ShortwordTypeActivity;
import edu.skku.woongjin_ai.R;

public class SelectTypeActivity extends AppCompatActivity implements NewHoonjangFragment.OnFragmentInteractionListener {

    Intent intent, intentHome, intentOX, intentChoice, intentShortword, intentTemplate;
    String id, scriptnm, backgroundID, nickname, thisWeek;
    ImageButton frameOX, frameChoice, frameShortword;
    ImageButton goHome;
    public NewHoonjangFragment hoonjangFragment;
    DatabaseReference mPostReference;
//    ImageView backgroundImage;
//    FirebaseStorage storage;
//    private StorageReference storageReference, dataReference;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String nomore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        setting = getSharedPreferences("nomore", MODE_PRIVATE); //터진다????????????????????????????????????????????
        nomore = setting.getString("quiz", "keepgoing");

        goHome = (ImageButton) findViewById(R.id.home);
        frameOX = (ImageButton) findViewById(R.id.quiz_ox);
        frameChoice = (ImageButton) findViewById(R.id.quiz_choice);
        frameShortword = (ImageButton) findViewById(R.id.quiz_shortword);
        Button showTemplate = (Button) findViewById(R.id.template);
        TextView textViewTitle = (TextView) findViewById(R.id.title);
        TextView textViewId = (TextView) findViewById(R.id.makeQuiz);
//        backgroundImage = (ImageView) findViewById(R.id.background);

        textViewTitle.setText("지문 제목: " + scriptnm);
        textViewId.setText(nickname + "(이)가 직접 문제를 만들어볼까?\n퀴즈를 내고 이 달의 출제왕이 되어보자!");

        mPostReference = FirebaseDatabase.getInstance().getReference();
        getFirebaseDatabaseMedalInfo();

        // 배경 이미지 (background)
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getInstance().getReference();
//        dataReference = storageReference.child("/scripts_background/" + backgroundID);
//        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(SelectTypeActivity.this)
//                        .load(uri)
//                        .placeholder(R.drawable.bot)
//                        .error(R.drawable.btn_x)
//                        .into(backgroundImage);
//                backgroundImage.setAlpha(0.5f);
//            }
//        });

        // 타입별 질문 예시보기 버튼 이벤트
        showTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentTemplate = new Intent(SelectTypeActivity.this, TemplateActivity.class);
                intentTemplate.putExtra("id", id);
                intentTemplate.putExtra("scriptnm", scriptnm);
                intentTemplate.putExtra("background", backgroundID);
                intentTemplate.putExtra("nickname", nickname);
                intentTemplate.putExtra("thisWeek", thisWeek);
                startActivity(intentTemplate);
            }
        });

        // 메인 페이지로 돌아가기 버튼 이벤트
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(SelectTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        // OX 문제 만들기 버튼 이벤트
        frameOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentOX = new Intent(SelectTypeActivity.this, OXTypeActivity.class);
                intentOX.putExtra("id", id);
                intentOX.putExtra("scriptnm", scriptnm);
                intentOX.putExtra("background", backgroundID);
                intentOX.putExtra("nickname", nickname);
                intentOX.putExtra("thisWeek", thisWeek);
                startActivity(intentOX);
            }
        });

        // 객관식 문제 만들기 버튼 이벤트
        frameChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentChoice = new Intent(SelectTypeActivity.this, ChoiceTypeActivity.class);
                intentChoice.putExtra("id", id);
                intentChoice.putExtra("scriptnm", scriptnm);
                intentChoice.putExtra("background", backgroundID);
                intentChoice.putExtra("nickname", nickname);
                intentChoice.putExtra("thisWeek", thisWeek);
                startActivity(intentChoice);
            }
        });

        // 단답형 문제 만들기 버튼 이벤트
        frameShortword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShortword = new Intent(SelectTypeActivity.this, ShortwordTypeActivity.class);
                intentShortword.putExtra("id", id);
                intentShortword.putExtra("scriptnm", scriptnm);
                intentShortword.putExtra("background", backgroundID);
                intentShortword.putExtra("nickname", nickname);
                intentShortword.putExtra("thisWeek", thisWeek);
                startActivity(intentShortword);
            }
        });
    }

    // 훈장 수여 자격 여부 확인 및 수여, 데이터베이스에 저장
    private void getFirebaseDatabaseMedalInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int MadeCount=0;
                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id+"my_week_list");
                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){ //week 껍데기
                    MadeCount+=Integer.parseInt(dataSnapshot2.child("made").getValue().toString());
                }
                Calendar calendar = Calendar.getInstance();
                Date dateS = calendar.getTime();
                String MedalUpdate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hoonjangFragment=new NewHoonjangFragment();
                if(MadeCount==100 && nomore.equals("stop2")) {
                    uploadFirebaseUserCoinInfo_H("출제왕", 3);
                    mPostReference.child("user_list/" + id + "/my_medal_list/출제왕").setValue("Lev3##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("quiz", "stop3");
                    editor.commit();
                    transaction.replace(R.id.selectMainFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quiz");
                    bundle.putString("from", "quiz");
                    bundle.putInt("level", 3);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }else if(MadeCount==60 && nomore.equals("stop1")){
                    uploadFirebaseUserCoinInfo_H("출제왕", 2);
                    mPostReference.child("user_list/" + id + "/my_medal_list/출제왕").setValue("Lev2##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("quiz", "stop2");
                    editor.commit();
                    transaction.replace(R.id.selectMainFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quiz");
                    bundle.putString("from", "quiz");
                    bundle.putInt("level", 2);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }else if(MadeCount==30 && nomore.equals("keepgoing")){
                    uploadFirebaseUserCoinInfo_H("출제왕", 1);
                    mPostReference.child("user_list/" + id + "/my_medal_list/출제왕").setValue("Lev1##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("quiz", "stop1");
                    editor.commit();
                    transaction.replace(R.id.selectMainFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quiz");
                    bundle.putString("from", "quiz");
                    bundle.putInt("level", 1);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 훈장 수여에 따른 코인 증정 데이터 데이터베이스에 저장
    private void uploadFirebaseUserCoinInfo_H(String hoonjangname, int level){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/get").setValue(Integer.toString(level*100));
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/why").setValue(hoonjangname+" 레벨 "+Integer.toString(level)+"달성!");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + level*100;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child("user_list/" + id).child("coin").setValue(coin_convert);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
