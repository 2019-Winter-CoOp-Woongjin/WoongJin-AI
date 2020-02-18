package edu.skku.woongjin_ai.Package_4_4_MyPage.MyQuiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai.Package_4_2_MainQuizType.MyFriendQuizListAdapter;
import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.SeeChoiceQuizFragment;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.SeeOXQuizFragment;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.SeeShortQuizFragment;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.ShowWhoLikedFragment;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizChoiceTypeInfo;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizOXShortwordTypeInfo;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.Package_4_2_MainQuizType.ShowScriptFragment;
import edu.skku.woongjin_ai.UserInfo;

import static android.media.CamcorderProfile.get;

public class MyQuizActivity extends AppCompatActivity implements SeeOXQuizFragment.OnFragmentInteractionListener, SeeChoiceQuizFragment.OnFragmentInteractionListener, SeeShortQuizFragment.OnFragmentInteractionListener, ShowScriptFragment.OnFragmentInteractionListener, ShowWhoLikedFragment.OnFragmentInteractionListener {

    public DatabaseReference mPostReference;
    Intent intent, intentHome, intentUpdate;
    Button goback;
    String id, profile;
    TextView instruction;
    ListView quizlist, likefriends;
    ArrayList<QuizChoiceTypeInfo> myChoiceList;
    ArrayList<QuizOXShortwordTypeInfo> myOXList, myShortList;
    MyFriendQuizListAdapter myQuizListAdapter;
    int flag=0;
    public SeeOXQuizFragment OXFragment;
    public SeeChoiceQuizFragment ChoiceFragment;
    public SeeShortQuizFragment ShortFragment;
    public ShowScriptFragment showScriptFragment;
    public ShowWhoLikedFragment showWhoLikedFragment;
    ArrayList<UserInfo> Uinfos;
    int cntOX, cntChoice, cntShort;
    String showQkey;
    ShowFriendListAdapter showFriendListAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seequestion_likeormine);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        quizlist = (ListView) findViewById(R.id.quizlist);
        likefriends=(ListView) findViewById(R.id.wholiked_list);
        goback=(Button)findViewById(R.id.goback);

        instruction=(TextView)findViewById(R.id.instruction);


        Uinfos=new ArrayList<UserInfo>();

        intent = getIntent();
        id = intent.getStringExtra("id");
        profile=intent.getStringExtra("profile");

        OXFragment=new SeeOXQuizFragment();
        ChoiceFragment=new SeeChoiceQuizFragment();
        ShortFragment=new SeeShortQuizFragment();

        showScriptFragment=new ShowScriptFragment();
        showWhoLikedFragment=new ShowWhoLikedFragment();

        showFriendListAdapter=new ShowFriendListAdapter();

        mPostReference = FirebaseDatabase.getInstance().getReference();


        myQuizListAdapter = new MyFriendQuizListAdapter();

        myChoiceList=new ArrayList<QuizChoiceTypeInfo>();
        myOXList=new ArrayList<QuizOXShortwordTypeInfo>();
        myShortList=new ArrayList<QuizOXShortwordTypeInfo>();

        intentUpdate = new Intent(MyQuizActivity.this, MyQuizActivity.class);
        intentUpdate.putExtra("id", id);

        getFirebaseDatabaseMyQuizList();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(MyQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        quizlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                if(flag == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(OXFragment);
                    fragmentTransaction.commit();
                    OXFragment = new SeeOXQuizFragment();
                } else if(flag == 2) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(ChoiceFragment);
                    fragmentTransaction.commit();
                    ChoiceFragment = new SeeChoiceQuizFragment();
                } else if(flag == 3) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(ShortFragment);
                    fragmentTransaction.commit();
                    ShortFragment = new SeeShortQuizFragment();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(position < cntOX) {
                    flag = 1;
                    QuizOXShortwordTypeInfo quiz = myOXList.get(position);

                    transaction.replace(R.id.seequiz_fragment, OXFragment);
                    Bundle bundle = new Bundle(12);
                    bundle.putString("id", id);
                    bundle.putString("mine_or_like", "0");
                    bundle.putString("scriptnm", quiz.scriptnm);
                    bundle.putString("question", quiz.question);
                    bundle.putString("answer", quiz.answer);
                    bundle.putString("uid", quiz.uid);
                    bundle.putString("star", quiz.star);
                    bundle.putString("like", quiz.like);
                    bundle.putString("desc", quiz.desc);
                    bundle.putString("key", quiz.key);
                    bundle.putInt("cnt", quiz.cnt);
                    bundle.putInt("type", 1);
                    OXFragment.setArguments(bundle);
                    transaction.commit();
                } else {
                    position -= cntOX;
                    if(position < cntChoice) {
                        flag = 2;
                        QuizChoiceTypeInfo quiz = myChoiceList.get(position);

                        transaction.replace(R.id.seequiz_fragment, ChoiceFragment);
                        Bundle bundle = new Bundle(16);
                        bundle.putString("id", id);
                        bundle.putString("mine_or_like", "0");
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("answer1", quiz.answer1);
                        bundle.putString("answer2", quiz.answer2);
                        bundle.putString("answer3", quiz.answer3);
                        bundle.putString("answer4", quiz.answer4);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        bundle.putInt("type", 2);
                        ChoiceFragment.setArguments(bundle);
                        transaction.commit();
                    } else {
                        position -= cntChoice;
                        flag = 3;
                        QuizOXShortwordTypeInfo quiz = myShortList.get(position);

                        transaction.replace(R.id.seequiz_fragment, ShortFragment);
                        Bundle bundle = new Bundle(12);
                        bundle.putString("id", id);
                        bundle.putString("mine_or_like", "0");
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        bundle.putInt("type", 3);
                        ShortFragment.setArguments(bundle);
                        transaction.commit();
                    }
                }
            }
        });

    }

    public void onFragmentChange(int index, String Qkey, int originalType){
        if(index==0){
            showQkey=Qkey;
            Bundle bundle=new Bundle(2);
            bundle.putInt("type", originalType);
            bundle.putString("key", showQkey);
            showWhoLikedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, showWhoLikedFragment).commit();
        }else if(index==1){
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, ShortFragment).commit();
        }else if(index==2){
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, ChoiceFragment).commit();
        }else if(index==3){
            getSupportFragmentManager().beginTransaction().replace(R.id.seequiz_fragment, OXFragment).commit();
        }
    }


    private void getFirebaseDatabaseMyQuizList() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myOXList.clear();
                myChoiceList.clear();
                myShortList.clear();
                //퀴즈 데이터 받아오기
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("quiz_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) { //script
                            String scriptTitle = snapshot1.getKey();
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) { // inside-script
                                String question_key = snapshot2.getKey();
                                if (question_key.substring(10).equals(id)) {
                                    String type = snapshot2.child("type").getValue().toString();
                                    if (type.equals("1")) {
                                        QuizOXShortwordTypeInfo quiz = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                        myOXList.add(quiz);
                                    } else if (type.equals("2")) {
                                        QuizChoiceTypeInfo quiz = snapshot2.getValue(QuizChoiceTypeInfo.class);
                                        myChoiceList.add(quiz);
                                    } else {
                                        QuizOXShortwordTypeInfo quiz = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                        myShortList.add(quiz);
                                    }
                                }
                            }
                        }

                        cntOX = myOXList.size();
                        cntChoice = myChoiceList.size();
                        cntShort = myShortList.size();

                        //난이도별로 퀴즈리스트 어댑터에 아이템 추가


                        for(int i=0; i<myOXList.size(); i++) {
                            QuizOXShortwordTypeInfo quizinfo=myOXList.get(i);
                            float stars=Float.parseFloat(quizinfo.star);
                            if(stars < 1.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 1.5 && stars < 2.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 2.5 && stars < 3.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 3.5 && stars < 4.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            }
                        for(int i=0; i<myChoiceList.size(); i++){
                            QuizChoiceTypeInfo quizinfo=myChoiceList.get(i);
                            float stars=Float.parseFloat(quizinfo.star);
                            if(stars < 1.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 1.5 && stars < 2.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 2.5 && stars < 3.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 3.5 && stars < 4.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);

                        }
                        for(int i=0; i<myShortList.size(); i++){
                            QuizOXShortwordTypeInfo quizinfo=myShortList.get(i);
                            float stars=Float.parseFloat(quizinfo.star);
                            if(stars < 1.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 1.5 && stars < 2.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 2.5 && stars < 3.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 3.5 && stars < 4.5)
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else
                                myQuizListAdapter.addItem(profile, id, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            }
                        quizlist.setAdapter(myQuizListAdapter);
                    }
                    //final ValueEventListener quiz_list = mPostReference.child("quiz_list").addValueEventListener(postListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}