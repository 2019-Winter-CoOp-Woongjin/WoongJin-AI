package edu.skku.woongjin_ai.Package_2_2_KaKao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.skku.woongjin_ai_winter.Package_1_Login.LoginActivity;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.UserInfo;

public class KakaoRegisterActivity extends AppCompatActivity {
    int use_nickname;
    private Spinner gradespinner;
    private Spinner genderspinner;
    EditText editTextName, editTextNickname, editTextSchool;
    Button btn_register, check_nickname, btn_again;
    ArrayList<String> genderList;ArrayAdapter<String> genderAdapter;
    ArrayList<String> gradeList;ArrayAdapter<String> gradeAdapter;

    String id, pw, name, nickname;
    String gender;
    String grade;
    String school;

    DatabaseReference mPostReference;
    Intent intent;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaoregister);

        use_nickname = 0;

        intent = getIntent();
        id = intent.getStringExtra("id");
        pw = id;

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        editTextSchool = (EditText)findViewById(R.id.editTextSchool);
        btn_register = (Button) findViewById((R.id.btn_register));
        check_nickname = (Button) findViewById(R.id.check_nickname);
        btn_again = (Button)findViewById(R.id.btn_again);

        gradeList = new ArrayList<>();
        gradeList.add("1"); gradeList.add("2");  gradeList.add("3");  gradeList.add("4");  gradeList.add("5"); gradeList.add("6");
        gradeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, gradeList);
        gradespinner = (Spinner) findViewById(R.id.grade);
        gradespinner.setAdapter(gradeAdapter);
        gradespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade = (String)gradeAdapter.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        genderList = new ArrayList<>();
        genderList.add("남");
        genderList.add("여");
        genderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genderList);
        genderspinner = (Spinner) findViewById(R.id.gender);
        genderspinner.setAdapter(genderAdapter);
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = (String)genderAdapter.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mPostReference = FirebaseDatabase.getInstance().getReference();

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(KakaoRegisterActivity.this, LoginActivity.class);
                startActivity(intent_login);
                finish();
            }
        });

        check_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNickname.getText().toString().equals("") || TextUtils.isEmpty(editTextNickname.getText().toString())) {
                    Toast.makeText(KakaoRegisterActivity.this, "사용할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mPostReference.addListenerForSingleValueEvent(checkNicknameRegister);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString();
                nickname = editTextNickname.getText().toString();
                school = editTextSchool.getText().toString();
                if ((use_nickname == 0)) {
                    Toast.makeText(KakaoRegisterActivity.this, "닉네임 체크를 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (id.length() == 0 || pw.length() == 0 || name.length() == 0 || nickname.length() == 0 || grade.length() == 0
                        || gender.length() == 0 || school.length() == 0 || spaceCheck(id) == true || spaceCheck(pw) == true || spaceCheck(name) == true || spaceCheck(grade) == true
                        || spaceCheck(gender) == true || spaceCheck(school) == true) {
                    Toast.makeText(KakaoRegisterActivity.this, "모든 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    postFirebaseDatabaseUserInfo();
                    Intent intent_main = new Intent(KakaoRegisterActivity.this, MainActivity.class);
                    intent_main.putExtra("id", id);
                    startActivity(intent_main);
                    finish();
                }
            }
        });
    }

    private void postFirebaseDatabaseUserInfo() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        UserInfo post = new UserInfo(id, pw, name, nickname, school, gender, grade, "100", "noimage", false);
        postValues = post.toMap();
        childUpdates.put("/user_list/" + id + "/", postValues);
        mPostReference.updateChildren(childUpdates);
    }

    private ValueEventListener checkNicknameRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.child("user_list").getChildren().iterator();
            while (child.hasNext()) {
                if (editTextNickname.getText().toString().equals(child.next().child("nickname").getValue())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "사용할 수 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
            use_nickname = 1;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public boolean spaceCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == ' ')
                continue;
            else if (spaceCheck.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
}
