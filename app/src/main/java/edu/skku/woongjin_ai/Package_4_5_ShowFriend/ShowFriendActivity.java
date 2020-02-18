package edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.UserInfo;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;
// 친구 삭제, 친구 신청시 수락후 추가 미구현
// 현재는 한쪽이 친구 추가시 일방적으로 양쪽 모두에게 친구 추가가 됨.

public class ShowFriendActivity extends Activity {
    private DatabaseReference mPostReference, mPostReference2;
    private FirebaseDatabase database;
    ListView friend_list, recommendfriend_list, search_list;
    ArrayList<String> myFriendList;
    ArrayList<UserInfo> recommendList, recommendFinalList;
    UserInfo me;
    String id_key, nickname_key;
    EditText findID;
    //    String friend_nickname;
    String myprofile,myschool,mygrade,mynickname,myname;
    String newfriend_nickname;
    String sfriend_nickname, sfriend_name, sfriend_id, sfriend_grade, sfriend_school, sfriend_profile;
    Boolean sfriend_onoffline;
    ImageButton invitefriendbtn, addfriendbtn, imageButtonHomebtn;
    Button searchbtn, gobackbtn;
    Intent intent, intentHome;
    int check_recommend;
    UserInfo searched;
    UserInfo ME;
    ArrayList<UserInfo> searchList;
    ShowFriendListAdapter showFriendListAdapterS;

    ImageButton searchedAddfriend;

    int searchedFlag=0;

    SharedPreferences WhoAmI;
    SharedPreferences.Editor editor;

    FirebaseStorage storage;

    String FriendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriend);

        //searchedFace=(ImageView)findViewById(R.id.friendFace);
        //searchedName=(TextView)findViewById(R.id.friendName);
        //searchedGrade=(TextView)findViewById(R.id.friendGrade);
        //searchedSchool=(TextView)findViewById(R.id.friendSchool);
        //searchedAddfriend=(ImageButton)findViewById(R.id.addFriendButton);
        //check_choose = 0;

        // just for checking error --- ignore
        ///intent = getIntent();
        ///id_key = intent.getStringExtra("id");
        ///myprofile = intent.getStringExtra("profile");
        id_key = GlobalApplication.getInstance().getGlobalID();
        myprofile = GlobalApplication.getInstance().getGlobalProfile();
        //myschool = intent.getStringExtra("school");
        //mygrade = intent.getStringExtra("grade");
        //mynickname = intent.getStringExtra("nickname");
        //myname = intent.getStringExtra("name");
        myschool = GlobalApplication.getInstance().getGlobalSchool();
        mygrade = GlobalApplication.getInstance().getGlobalGrade();
        mynickname = GlobalApplication.getInstance().getGlobalNickname();
        myname = GlobalApplication.getInstance().getGlobalName();

        // just for checking error --- ignore
        //GlobalApplication.getInstance().startToast(id_key + " " + myprofile + " " + myschool + " " + mygrade + " " + mynickname + " " + myname);
        //Log.e("에러발생",id_key + " " + myprofile + " 1 " + myschool + " 2 " + mygrade + " 3  " + mynickname + " 4 " + myname);
        // mypage -> myschool, mygrade, myname == null , mynickname은 공백으로 취급됨.
        // globalapplcation str로 null값 해결.

        WhoAmI=getSharedPreferences("myinfo", MODE_PRIVATE);
        editor=WhoAmI.edit();
        editor.putString("mynick", mynickname);
        editor.putString("myschool", myschool);
        editor.putString("mygrade", mygrade);
        editor.putString("myprofile", myprofile);
        editor.putString("myname", myname);
        editor.putString("myid", id_key);
        editor.commit();
        // just for checking error --- ignore
        //GlobalApplication.getInstance().startToast(id_key + " " + myprofile + " " + myschool + " " + mygrade + " " + mynickname + " " + myname);
        //Log.e("에러발생",id_key + " " + myprofile + " 1 " + myschool + " 2 " + mygrade + " 3  " + mynickname + " 4 " + myname);

        check_recommend = 0;

        gobackbtn=(Button)findViewById(R.id.goback);                       //goback btn
        invitefriendbtn = (ImageButton) findViewById(R.id.invitefriend);   //kakao btn
        imageButtonHomebtn = (ImageButton) findViewById(R.id.home);        //home btn
        searchbtn=(Button)findViewById(R.id.search);                       //search btn

        findID=(EditText)findViewById(R.id.searchID);                   // 아이디 검색

        friend_list = findViewById(R.id.friend_list);                   // already friend listview
        recommendfriend_list = findViewById(R.id.recommendfriend_list); // random friend listview
        search_list=findViewById(R.id.searched_friend);                 // 검색 결과 searched_friend

        me = new UserInfo();

        myFriendList = new ArrayList<String>();
        recommendList = new ArrayList<UserInfo>();
        recommendFinalList = new ArrayList<UserInfo>();
        searchList = new ArrayList<UserInfo>();
        showFriendListAdapterS=new ShowFriendListAdapter();

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

        getFirebaseDatabase();

        findViewById(R.id.home).setOnClickListener(onClickListener);
        findViewById(R.id.goback).setOnClickListener(onClickListener);
        findViewById(R.id.search).setOnClickListener(onClickListener);

        callback = new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Toast.makeText(getApplicationContext(), "Successfully sent KakaoLink v2 message.", Toast.LENGTH_LONG).show();
            }
        };
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.home:
                    startMainActivity();
                    break;

                case R.id.goback:
                    onBackPressed();
                    break;

                case R.id.search:
                    SearchingFriend();
                    break;

            }
        }
    };


    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        return callbackParameters;
    }

    private ResponseCallback<KakaoLinkResponse> callback;
    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();

    public void SearchingFriend(){

        String UID=findID.getText().toString();
        if(UID.equals(NULL) || UID.equals("")){
            startToast("검색할 닉네임을 입력하세요!");
        }else if(UID.equals(mynickname))
        {
            startToast("본인입니다.");
        }
        else{
            database.getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot dataSnapshot1=dataSnapshot.child("user_list");
                    for(DataSnapshot snapshot: dataSnapshot1.getChildren()){
                        searchedFlag=0;
                        if((snapshot.child("nickname").exists())){
                            if(UID.equals(snapshot.child("nickname").getValue().toString())){
                                // 디비 조회해서 없을 경우 널이 들어가는건가?
                                searchedFlag=1;
                                searched = snapshot.getValue(UserInfo.class);
                                String profileUri = searched.profile;
                                sfriend_id = searched.id;
                                sfriend_nickname = searched.nickname;
                                sfriend_name = searched.name;
                                sfriend_grade = searched.grade;
                                sfriend_school = searched.school;
                                sfriend_profile = profileUri;
                                sfriend_onoffline = searched.onoffline;
                                ShowFriendListAdapter showFriendListAdapter=new ShowFriendListAdapter();
                                showFriendListAdapter.addItem(sfriend_profile, sfriend_name, sfriend_grade, sfriend_school,  sfriend_id, sfriend_nickname, true, false, sfriend_onoffline);
                                search_list.setAdapter(showFriendListAdapter);
                                search_list.clearChoices();
                                showFriendListAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                    if(searchedFlag==0){
                        Toast.makeText(ShowFriendActivity.this, "검색 결과가 없습니다. 닉네임으로 검색해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    myFriendList.clear();

                    DataSnapshot snapshot=dataSnapshot.child("user_list/"+id_key+"/my_friend_list");
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key2 = snapshot1.getKey();
                        myFriendList.add(key2); //myFriendList 속에 내 친구들 id 저장
                    }

                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    DataSnapshot snapshot1=dataSnapshot.child("user_list");
                    for(DataSnapshot snapshot2: snapshot1.getChildren()){
                        String keys=snapshot2.getKey();
                        for(String UID: myFriendList){
                            if(UID.equals(keys)){
                                UserInfo friend=snapshot2.getValue(UserInfo.class);
                                showFriendListAdapter.addItem(friend.profile, friend.name, friend.grade, friend.school, friend.id, friend.nickname, false, true, friend.onoffline);
                            }
                        }
                    }
                    friend_list.setAdapter(showFriendListAdapter);
                    friend_list.clearChoices();
                    showFriendListAdapter.notifyDataSetChanged();
                    getFirebaseDatabaseRecommendFriendList();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference2.addValueEventListener(postListener);

        } catch (NullPointerException e) {

        }
    }


    public void getFirebaseDatabaseRecommendFriendList() {
        final ValueEventListener postListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFriendList.clear();
                recommendList.clear();
                recommendFinalList.clear();

                DataSnapshot snapshot=dataSnapshot.child("user_list/"+id_key+"/my_friend_list");
                //if(snapshot==null) Log.e("Error", "Null 발생 ");
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String key2 = snapshot1.getKey();
                    myFriendList.add(key2);
                }
                // 내 친구 리스트에 있는 key들을 myFriendList안에 추가.

                DataSnapshot snapshot1=dataSnapshot.child("user_list");
                int flag=0;
                for(DataSnapshot snapshot2: snapshot1.getChildren()){
                    String keys=snapshot2.getKey();         //user_list의 키 모두.


                    for(String UID: myFriendList){  // 이미 친구인 리스트에 UID와 user_list내 key 일치하면 이미 친구이므로 패스함.
                        if(UID.equals(keys) || UID.equals((id_key))){
                            flag=1; //이미 내 친구여
                            break;
                        }
                    }
                    if(flag==1) break;// 개빙신같은데 새악ㄱ 해보자

                    if(flag==0) {//아직 내 친구 아님
                        UserInfo friend = snapshot2.getValue(UserInfo.class);
                        //System.out.println("프렌드" + friend);   //이미 친구제외하고 다 뜸(나포함)
                        String grade = friend.grade;
                        String uid = friend.id;
                        //System.out.println("Id" + uid + "." + " Grade " + grade);

                        if((snapshot1.child(uid).exists())){
                            //System.out.println("내 친구는요 "+uid);
                            if(mygrade.equals(grade) && !id_key.equals(uid)){
                                        recommendList.add(friend);
                                        //System.out.println("내 친구는요 "+friend);
                            }
                            //null object가 계속 들어감.
                        }
                    }

                }

                int cntAll = recommendList.size();
                Random generator = new Random();
                int[] randList = new int[cntAll];
                for(int i = 0; i < cntAll; i++) {
                    randList[i] = generator.nextInt(cntAll);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                ShowFriendListAdapter showRecommendFriendListAdapter = new ShowFriendListAdapter();
                for(int i = 0; i < cntAll; i++) {
                    UserInfo finalRecommend = recommendList.get(randList[i]);
                    recommendFinalList.add(finalRecommend);
                    showRecommendFriendListAdapter.addItem(finalRecommend.profile, finalRecommend.name, finalRecommend.grade, finalRecommend.school, finalRecommend.id, finalRecommend.nickname, true, false, finalRecommend.onoffline);
                }
                recommendfriend_list.setAdapter(showRecommendFriendListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference2.addValueEventListener(postListner);
    }






    private void startMainActivity(){
        Intent intentHome = new Intent(this, MainActivity.class);
        // 액티비티 기록 지움으로써 뒤로가기 버튼으로 무한 츠쿠요미 벗어나기.
        // 메인화면에서 뒤로가기 하면 꺼지도록 함.
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra("id",id_key);
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intentHome);
    }

    public void startToast(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }


}