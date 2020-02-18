package edu.skku.woongjin_ai.Package_2_1_Register;

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

import edu.skku.woongjin_ai.Package_1_Login.LoginActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.UserInfo;

/*
from LoginActivity
회원가입 페이지
 */

public class RegisterActivity extends AppCompatActivity {
    int use_id, use_nickname;
    private Spinner genderspinner;
    private Spinner gradespinner;
    EditText editTextID, editTextPW, editTextName, editTextNickname, editTextSchool;
    Button btn_register;
    Button check_id, check_nickname;
    ArrayList<String> genderList;ArrayAdapter<String> genderAdapter;
    ArrayList<String> gradeList;ArrayAdapter<String> gradeAdapter;

    String id, pw, name, nickname;
    String gender;
    String grade;
    String school;

    DatabaseReference mPostReference;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        use_id = 0;
        use_nickname = 0;

        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPW = (EditText) findViewById(R.id.editTextPW);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNickname = (EditText)findViewById(R.id.editTextNickname);
        editTextSchool = (EditText)findViewById(R.id.editTextSchool);
        btn_register = (Button) findViewById((R.id.btn_register));
        check_id = (Button)findViewById(R.id.check_id);
        check_nickname = (Button)findViewById(R.id.check_nickname);

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

        check_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextID.getText().toString().equals("") || TextUtils.isEmpty(editTextID.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "사용할 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (onlyNumCheck(editTextID.getText().toString()) == true) {
                    Toast.makeText(RegisterActivity.this, "숫자로만 구성된 ID는 사용이 불가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mPostReference.addListenerForSingleValueEvent(checkIDRegister);
                }
            }
        });

        check_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNickname.getText().toString().equals("") || TextUtils.isEmpty(editTextNickname.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "사용할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mPostReference.addListenerForSingleValueEvent(checkNicknameRegister);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                pw = editTextPW.getText().toString();
                name = editTextName.getText().toString();
                nickname = editTextNickname.getText().toString();
                school = editTextSchool.getText().toString();
                if ((use_id == 0 || use_nickname == 0)) {
                    Toast.makeText(RegisterActivity.this, "ID 중복체크 또는 닉네임 체크를 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (id.length() == 0 || pw.length() == 0 || name.length() == 0 || nickname.length() == 0 || grade.length() == 0
                        || gender.length() == 0 || school.length() == 0 || spaceCheck(id) == true || spaceCheck(pw) == true || spaceCheck(name) == true || spaceCheck(grade) == true
                        || spaceCheck(gender) == true || spaceCheck(school) == true) {
                    Toast.makeText(RegisterActivity.this, "모든 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    postFirebaseDatabaseUserInfo();
                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
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

    private ValueEventListener checkIDRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.child("user_list").getChildren().iterator();
            while (child.hasNext()) {
                if (editTextID.getText().toString().equals(child.next().getKey())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "사용할 수 있는 ID입니다.", Toast.LENGTH_SHORT).show();
            use_id = 1;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    private ValueEventListener checkNicknameRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.child("kakaouser_list").getChildren().iterator();
            while (child.hasNext()) {
                if (editTextNickname.getText().toString().equals(child.next().child("nickname").getValue())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Iterator<DataSnapshot> child1 = dataSnapshot.child("user_list").getChildren().iterator();
            while (child1.hasNext()) {
                if (editTextNickname.getText().toString().equals(child1.next().child("nickname").getValue())) {
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

    public boolean onlyNumCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == '1' || spaceCheck.charAt(i) == '2' || spaceCheck.charAt(i) == '3' || spaceCheck.charAt(i) == '4' || spaceCheck.charAt(i) == '5'
                    || spaceCheck.charAt(i) == '6' || spaceCheck.charAt(i) == '7' || spaceCheck.charAt(i) == '8' || spaceCheck.charAt(i) == '9' || spaceCheck.charAt(i) == '0') {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

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