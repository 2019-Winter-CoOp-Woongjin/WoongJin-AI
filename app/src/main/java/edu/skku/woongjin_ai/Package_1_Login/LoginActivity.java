package edu.skku.woongjin_ai.Package_1_Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.Package_2_2_KaKao.KakaoRegisterActivity;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.TestService;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.Package_2_1_Register.RegisterActivity;
import edu.skku.woongjin_ai_winter.UserInfo;

/*
first
로그인 페이지
 */

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    EditText editTextID, editTextPW;
    String id, pw;
    Button buttonLogin, buttonRegister;
    DatabaseReference mPostReference;
    Intent intent;
    int flag = 0;
    int kakaoflag = 0, overlapflag = 0;
    String savedKakao, data;
    Intent intent_kakaoregister;

    CheckBox checkbox;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //강제 종료시 데이터베이스 삭제를 위한 코드
        startService(new Intent(this, TestService.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callback = new SessionCallback();
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPW = (EditText) findViewById(R.id.editTextPW);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        intent_kakaoregister = new Intent(LoginActivity.this, KakaoRegisterActivity.class);

        checkbox = (CheckBox) findViewById(R.id.checkBox);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        if (!isLoggedIn()) //카카오톡 로그인이 되어있지 않을 경우
            Session.getCurrentSession().addCallback(callback);
        else { //카카오톡 로그인이 되어있을 경우
            File file = getBaseContext().getFileStreamPath("memos.txt");
            data = null;
            if (file.exists()) {
                FileInputStream fis = null;
                StringBuffer buffer = new StringBuffer();
                try {
                    fis = openFileInput("memos.txt");
                    BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));
                    data = iReader.readLine();
                    iReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final ValueEventListener overlapCheck = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.child("user_list").getChildren()) {
                        String key = ds.getKey();
                        if (data.equals(key)) {
                            overlapflag = 1;
                            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                            intent_main.putExtra("id", data);
                            Toast.makeText(getApplicationContext(), "카카오 계정으로 로그인 되었습니다", Toast.LENGTH_SHORT).show();
                            startActivity(intent_main);
                            finish();
                            return;
                        }
                    }
                    if (overlapflag == 0) {
                        intent_kakaoregister.putExtra("id", data);
                        Toast.makeText(getApplicationContext(), "회원가입을 완료해주세요", Toast.LENGTH_SHORT).show();
                        startActivity(intent_kakaoregister);
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mPostReference.addListenerForSingleValueEvent(overlapCheck);
        }

        // 회원가입 버튼 이벤트
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 이벤트
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                pw = editTextPW.getText().toString();

                if(id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(LoginActivity.this, "빈칸을 모두 채우세요", Toast.LENGTH_SHORT).show();
                } else {
                    final ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                if(id.equals(key)) {
                                    UserInfo get = snapshot.getValue(UserInfo.class);
                                    String password = get.pw;
                                    if(pw.equals(password)) {
                                        flag = 1;
                                        if(checkbox.isChecked()) {
                                            editor.putString("ID", id);
                                            editor.putString("PW", pw);
                                            editor.commit();
                                        }
                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("id", id);
                                        mPostReference.child("user_list").child(id).child("onoffline").setValue(true);

                                        startActivity(intent);
                                        finish();

                                        //동계추가 전역변수에 아이디, 이름넣기
                                        GlobalApplication gv = (GlobalApplication) getApplication();
                                        gv.setmGlobalID(id);
                                        gv.setmGlobalname(snapshot.child("name").getValue(String.class));

                                    }
                                }
                            }
                            if(flag == 0) {
                                Toast.makeText(LoginActivity.this, "Wrong login", Toast.LENGTH_SHORT).show();
                                editTextID.setText("");
                                editTextPW.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {         }
                    };
                    mPostReference.child("user_list").addListenerForSingleValueEvent(postListener);
                }
            }
        });

        // 자동 로그인
        if (setting.getBoolean("Auto login is enabled", false)) {
            editTextID.setText(setting.getString("ID", ""));
            editTextPW.setText(setting.getString("PW", ""));
            checkbox.setChecked(true);
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Auto login is enabled", true);
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }
            }
        });
    }
    public boolean isLoggedIn() {
        return !Session.getCurrentSession().isClosed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) { //간편로그인시 호출
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "사용자 정보를 얻어오는 데 실패하였습니다 + " + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) { //로그인 성공시, 사용자의 정보 리턴
                    savedKakao = userProfile.getId() + "";
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("memos.txt", Context.MODE_PRIVATE);
                        fos.write(savedKakao.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Log.e("UserProfile", userProfile.toString());
                    mPostReference.addListenerForSingleValueEvent(checkIDRegister);
                    Log.e("userProfile","" + userProfile.getId());
                    Log.d("myLog", "userProfile" + userProfile.getId());
                    Log.d("myLog", "userProfile" + userProfile.getNickname());
                    Log.d("myLog", "userProfile" + userProfile.getThumbnailImagePath());
                    //postFirebaseDatabase(true);
                    if (kakaoflag == 0) {
                        intent_kakaoregister.putExtra("id", savedKakao);
                        Toast.makeText(getApplicationContext(), "카카오 계정으로 로그인 되었습니다", Toast.LENGTH_SHORT).show();
                        startActivity(intent_kakaoregister);
                        finish();
                    }
                }
            });
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) { //세선 연결 실패시
            Toast.makeText(getApplicationContext(), "세션 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private ValueEventListener checkIDRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.child("user_list").getChildren()) {
                String key = ds.getKey();
                if (savedKakao.equals(key)) {
                    //Toast.makeText(getApplicationContext(), "카카오 계정으로 가입되어 있는 ID입니다.", Toast.LENGTH_SHORT).show();
                    kakaoflag = 1;
                    Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                    intent_main.putExtra("id", savedKakao);
                    startActivity(intent_main);
                    finish();
                    return;
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}
