package edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.SelectTypeActivity;
import edu.skku.woongjin_ai.Package_4_2_MainQuizType.ShowScriptFragment;
import edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.Game.ProblemSetting.ServerConnection;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.WeekInfo;
import edu.skku.woongjin_ai.mediarecorder.MediaRecorderActivity;

/*
from SelectTypeActivity
객관식 문제 만들기
 */

public class ChoiceTypeActivity extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener, HintWritingFragment.OnFragmentInteractionListener {

    DatabaseReference mPostReference;
    ImageView imageScript, imageCheck,imageViewS1, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    EditText editQuiz, editAns, editAns1, editAns2, editAns3, editAns4;
    public Intent intent, intentHome, intentType, intentVideo;
    String id, scriptnm, backgroundID, thisWeek, nickname, bookname;
    String quiz = "", ans = "", ans1 = "", ans2 = "", ans3 = "", ans4 = "", desc = "";
    int star = 0, starInt = 0, oldMadeCnt;
    public int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0, flagD = 0;
    public int flagA1 =0, flagA2=0, flagA3=0,flagA4 =0;
    ImageView backgroundImage;
    ImageButton checkButton, scriptButton, hintWritingButton, hintVideoButton, noHintButton;
    //    FirebaseStorage storage;
//    private StorageReference storageReference, dataReference;
    public Fragment showScriptFragment, hintWritingFragment;

    //문제 출제 가능 여부
    private Boolean canSubmit;

    //질문확인버튼
    ImageView question_check_button;
    //유사도 출력
    private TextView comment;
    //정답 번호
    private int int_answer;
    //스크립트
    private String script;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicetype);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageScript = (ImageView) findViewById(R.id.script);
        imageCheck = (ImageView) findViewById(R.id.check);
        imageViewS1 = (ImageView) findViewById(R.id.star1);
        imageViewS2 = (ImageView) findViewById(R.id.star2);
        imageViewS3 = (ImageView) findViewById(R.id.star3);
        imageViewS4 = (ImageView) findViewById(R.id.star4);
        imageViewS5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
        editAns1 = (EditText) findViewById(R.id.ans1);
        editAns2 = (EditText) findViewById(R.id.ans2);
        editAns3 = (EditText) findViewById(R.id.ans3);
        editAns4 = (EditText) findViewById(R.id.ans4);
        TextView title = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);
        checkButton = (ImageButton) findViewById(R.id.check);
        scriptButton = (ImageButton) findViewById(R.id.script);
        backgroundImage = (ImageView) findViewById(R.id.background);
        hintWritingButton = (ImageButton) findViewById(R.id.hintWriting);
        hintVideoButton = (ImageButton) findViewById(R.id.hintVideo);
        noHintButton = (ImageButton) findViewById(R.id.noHint);
        comment = findViewById(R.id.problemComment);

        canSubmit = false;
        int_answer = 0;

        //흔들리는 애니메이션
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        title.setText("지문 제목: " + scriptnm);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        getFirebaseDatabaseUserInfo();

        //질문확인버튼 동계추가
        question_check_button = (ImageButton) findViewById(R.id.question_check);

        mPostReference.child("script_list").child(scriptnm).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                script = dataSnapshot.child("text").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 배경이미지 넣기 (background)
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getInstance().getReference();
//        dataReference = storageReference.child("/scripts_background/" + backgroundID);
//        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(ChoiceTypeActivity.this)
//                        .load(uri)
//                        .placeholder(R.drawable.bot)
//                        .error(R.drawable.btn_x)
//                        .into(backgroundImage);
//                backgroundImage.setAlpha(0.5f);
//            }
//        });

        //질문확인버튼 클릭 이벤트 동계추가
        question_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("객관식","객관식버튼눌렀을떄");
            }
        });

        // 글 힌트주기 버튼 이벤트
        hintWritingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintWritingFragment = new HintWritingFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentSelectHint, hintWritingFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("type", "choice");
                hintWritingFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
                flagD = 1;
            }
        });

        // 영상 힌트주기 버튼 이벤트
        hintVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagD = 3;
                intentVideo = new Intent(ChoiceTypeActivity.this, MediaRecorderActivity.class);
                intentVideo.putExtra("id",id);
                startActivity(intentVideo);
                hintVideoButton.setColorFilter(Color.parseColor("#E4FF9800"), PorterDuff.Mode.MULTIPLY);
                noHintButton.setImageResource(R.drawable.hint_no);
            }
        });

        // 힌트 없음 버튼 이벤트
        noHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD != 2) {
                    noHintButton.setImageResource(R.drawable.hint_no_selected);
                    hintVideoButton.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.MULTIPLY);
                    flagD = 2;
                } else {
                    noHintButton.setImageResource(R.drawable.hint_no);
                    flagD = 0;
                }
            }
        });

        // 지문 보기 버튼 이벤트
        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentShowScriptChoice, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "choice");
                showScriptFragment.setArguments(bundle);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // 출제 완료 버튼 이벤트
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagD == 0) { // 힌트 고르지 않음
                    Toast.makeText(ChoiceTypeActivity.this, "힌트 타입을 고르시오.", Toast.LENGTH_SHORT).show();
                } else {
                    quiz = editQuiz.getText().toString();
                    HintWritingFragment hintWritingFragment1 = (HintWritingFragment) getSupportFragmentManager().findFragmentById(R.id.contentSelectHint);
                    if(flagD == 2) { // 힌트 없음
                        desc = "없음";
                    }
                    else if (flagD == 3) { // 영상 힌트
                        desc = "video";
                    } else { // 글 힌트
                        desc = hintWritingFragment1.editTextHint.getText().toString();
                    }
                    quiz = editQuiz.getText().toString();
                    ans1 = editAns1.getText().toString();
                    ans2 = editAns2.getText().toString();
                    ans3 = editAns3.getText().toString();
                    ans4 = editAns4.getText().toString();

                    if(quiz.length() == 0 || ans.length() == 0 || ans1.length() == 0 || ans2.length() == 0 || ans3.length() == 0 || ans4.length() == 0 || desc.length() == 0 || starInt < 1) {
                        Toast.makeText(ChoiceTypeActivity.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    }
                    else if (comment.getText().toString().equals(""))
                        Toast.makeText(ChoiceTypeActivity.this, "질문 검사를 해주세요.", Toast.LENGTH_SHORT).show();

                    else if (canSubmit == false) {
                        comment.startAnimation(shake);
                    }
                    else { // 출제 완료
                        postFirebaseDatabaseQuizChoice();
                        uploadFirebaseUserCoinInfo();
                        if(flagD == 1) hintWritingFragment1.editTextHint.setText("");
                        Toast.makeText(ChoiceTypeActivity.this, "출제 완료!", Toast.LENGTH_SHORT).show();

                        oldMadeCnt++;
                        mPostReference.child("user_list/" + id + "/my_week_list/week" + thisWeek + "/made").setValue(oldMadeCnt);

                        intentType = new Intent(ChoiceTypeActivity.this, SelectTypeActivity.class);
                        intentType.putExtra("id", id);
                        intentType.putExtra("scriptnm", scriptnm);
                        intentType.putExtra("background", backgroundID);
                        intentType.putExtra("nickname", nickname);
                        intentType.putExtra("thisWeek", thisWeek);
                        startActivity(intentType);
                    }
                }


            }
        });

        // 홈 버튼 이벤트
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ChoiceTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        // 난이도 별1
        imageViewS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS1 == 0) {
                    starInt = 1;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        // 난이도 별2
        imageViewS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS2 == 0) {
                    starInt = 2;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        // 난이도 별3
        imageViewS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS3 == 0) {
                    starInt = 3;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    imageViewS3.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        // 난이도 별4
        imageViewS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS4 == 0) {
                    starInt = 4;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    imageViewS3.setImageResource(R.drawable.star_full);
                    imageViewS4.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        // 난이도 별5
        imageViewS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS5 == 0) {
                    starInt = 5;
                    imageViewS1.setImageResource(R.drawable.star_full);
                    imageViewS2.setImageResource(R.drawable.star_full);
                    imageViewS3.setImageResource(R.drawable.star_full);
                    imageViewS4.setImageResource(R.drawable.star_full);
                    imageViewS5.setImageResource(R.drawable.star_full);
                    flagS1 = 1;
                    flagS2 = 1;
                    flagS3 = 1;
                    flagS4 = 1;
                    flagS5 = 1;
                } else {
                    starInt = 0;
                    imageViewS1.setImageResource(R.drawable.star_empty);
                    imageViewS2.setImageResource(R.drawable.star_empty);
                    imageViewS3.setImageResource(R.drawable.star_empty);
                    imageViewS4.setImageResource(R.drawable.star_empty);
                    imageViewS5.setImageResource(R.drawable.star_empty);
                    flagS1 = 0;
                    flagS2 = 0;
                    flagS3 = 0;
                    flagS4 = 0;
                    flagS5 = 0;
                }
            }
        });

        // 객관식 답1
        editAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA1 == 0 ) {
                    if(flagA2==0 && flagA3==0 && flagA4==0 ){
                        editAns1.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA1 = 1;
                        ans = editAns1.getText().toString();
                        int_answer = 1;
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns1.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA1 = 0;
                    ans="";
                    int_answer = 0;
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });


        // 객관식 답2
        editAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA2 == 0) {
                    if( flagA1==0 && flagA3==0 && flagA4==0){
                        editAns2.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA2 = 1;
                        ans = editAns2.getText().toString();
                        int_answer = 2;
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns2.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA2 = 0;
                    ans="";
                    int_answer = 0;
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        // 객관식 답3
        editAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA3 == 0 ) {
                    if(flagA2==0 && flagA4==0 && flagA1==0){
                        editAns3.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA3 = 1;
                        ans = editAns3.getText().toString();
                        int_answer = 3;
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns3.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA3 = 0;
                    ans = "";
                    int_answer = 0;
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });


        // 객관식 답4
        editAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA4 == 0 ) {
                    if(flagA2==0 && flagA3==0 && flagA1==0){
                        editAns4.setBackgroundColor(Color.rgb(255, 153, 0));
                        flagA4 = 1;
                        ans = editAns4.getText().toString();
                        int_answer = 4;
                    }
                    else{
                        Toast.makeText(ChoiceTypeActivity.this, "먼저 정답을 초기화하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editAns4.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA4 = 0;
                    ans = "";
                    int_answer = 0;
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        //문제 검사 버튼 눌렀을 때
        question_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editQuiz.getText().toString().equals(""))
                    Toast.makeText(ChoiceTypeActivity.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (editAns1.getText().toString().equals("") || editAns2.getText().toString().equals("") || editAns3.getText().toString().equals("") || editAns4.getText().toString().equals(""))
                    Toast.makeText(ChoiceTypeActivity.this, "정답을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (int_answer == 0)
                    Toast.makeText(ChoiceTypeActivity.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();

                else {
                    Similarity similarity = new Similarity();

                    String[] array = new String[8];
                    array[0] = "2";
                    array[1] = script;
                    array[2] = editQuiz.getText().toString();
                    array[3] = "" + int_answer;
                    array[4] = editAns1.getText().toString();
                    array[5] = editAns2.getText().toString();
                    array[6] = editAns3.getText().toString();
                    array[7] = editAns4.getText().toString();

                    similarity.execute(array);
                    question_check_button.setEnabled(false);
                    question_check_button.setImageResource(R.drawable.checking);
                }
            }
        });

        //정답이나 문제 바꿨을 때 canSubmit 수정
        editQuiz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                canSubmit = false;
                comment.setText("");
            }
        });

        editAns1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                canSubmit = false;
                comment.setText("");
            }
        });

        editAns2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                canSubmit = false;
                comment.setText("");
            }
        });

        editAns3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                canSubmit = false;
                comment.setText("");
            }
        });

        editAns4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                canSubmit = false;
                comment.setText("");
            }
        });

    }

    // 유저 made 데이터와 지문 책이름 데이터 가져오기
    private void getFirebaseDatabaseUserInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WeekInfo weekInfo = dataSnapshot.child("user_list/" + id + "/my_week_list/week" + thisWeek).getValue(WeekInfo.class);
                oldMadeCnt = weekInfo.made;

                bookname = dataSnapshot.child("script_list/" + scriptnm + "/book_name").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 데이터베이스에 출제한 객관식 퀴즈 저장
    private void postFirebaseDatabaseQuizChoice() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        QuizChoiceTypeInfo post = new QuizChoiceTypeInfo(id, quiz, ans, ans1, ans2, ans3, ans4, Integer.toString(starInt), desc, "0", ts, 1, "없음", 2, scriptnm, bookname);
        postValues = post.toMap();
        childUpdates.put("/quiz_list/" + scriptnm + "/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editAns1.setText("");
        editAns2.setText("");
        editAns3.setText("");
        editAns4.setText("");
    }

    // 데이터베이스에 받은 코인 데이터 올리기
    private void uploadFirebaseUserCoinInfo(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/get").setValue("10");
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/why").setValue("지문 [" + scriptnm + "]에 대한 퀴즈를 냈어요.");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + 10;
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

    //유사도 검사
    public class Similarity extends AsyncTask<String, Void, Boolean[]> {

        @Override
        protected Boolean[] doInBackground(String... params) {

            try {
                ServerConnection serverConnection = new ServerConnection();
                String json = null;
                String result = null;
                //보낼 json 파일 만들기
                try {
                    json = serverConnection.makeJson(params);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                //connection 열어서 json 파일 받기
                result = serverConnection.urlConnection(json);

                return serverConnection.get_result(result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean[] result) {
            super.onPostExecute(result);
            if (result == null) {
                question_check_button.setEnabled(true);
                question_check_button.setImageResource(R.drawable.check1);
                comment.setText("문제 검사를 다시 해보는건 어떨까?");
                comment.setTextColor(Color.parseColor("#d3d3d3"));
                canSubmit = false;
            } else {
                if (result[0] == false) {
                    comment.setText("아쉽지만 문제를 다시 생각해 보는게 어떨까?");
                    comment.setTextColor(Color.RED);
                    canSubmit = false;
                } else if (result[1] == false) {
                    comment.setText("아쉽지만 정답을 다시 생각해 보는게 어떨까?");
                    comment.setTextColor(Color.parseColor("#FF9900"));
                    canSubmit = false;
                } else {
                    comment.setText("너무 멋진 문제야! 친구에게 문제를 내러 가볼까?");
                    comment.setTextColor(Color.parseColor("#009900"));
                    canSubmit = true;
                }
                question_check_button.setEnabled(true);
                question_check_button.setImageResource(R.drawable.check1);
            }
        }

    }
}
