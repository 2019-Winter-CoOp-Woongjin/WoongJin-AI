package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSetting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai_winter.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

public class PopupMulti extends Activity {

    private ImageButton create, cancel;
    private ImageView checkProb;
    private EditText problem, answer1, answer2, answer3, answer4;
    private TextView comment;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    private GlobalApplication gv;

    //intent 되고난 후 activity 지우기 위한 설정
    public static PopupMulti activity = null;

    private int ans, ans1, ans2, ans3, ans4;
    private String answer;
    private Boolean canSubmit;

    //스크립트보기버튼추가
    private Button showScript;

    Intent intentScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        //타이틀바 없앰
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_battlepopupmulti);

        create = findViewById(R.id.create_prob);
        cancel = findViewById(R.id.cancel_prob);
        checkProb = findViewById(R.id.checkProb);

        problem = findViewById(R.id.editProblem);
        problem.setMovementMethod(new ScrollingMovementMethod());

        answer1 = findViewById(R.id.ans1);
        answer2 = findViewById(R.id.ans2);
        answer3 = findViewById(R.id.ans3);
        answer4 = findViewById(R.id.ans4);
        comment = findViewById(R.id.problemComment);


        //스크립트파일보기버튼 연결
        showScript = findViewById(R.id.showScript);

        gv = (GlobalApplication) getApplication();

        ans = 0; ans1 = 0; ans2 = 0; ans3 = 0; ans4 = 0;
        answer = null;
        canSubmit = false;

        //흔들리는 애니메이션
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        //스크립트 파일보기버튼 눌렀을때
        showScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentScript = new Intent(PopupMulti.this, PopupScript.class);
                intentScript.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentScript);

            }
        });

        //엔터키 눌렀을 때 다음으로 넘어가게 설정
        problem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    answer1.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //엔터키 눌렀을 때 다음으로 넘어가게 설정
        answer1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    answer2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //엔터키 눌렀을 때 다음으로 넘어가게 설정
        answer2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    answer3.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //엔터키 눌렀을 때 다음으로 넘어가게 설정
        answer3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    answer4.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //엔터키 눌렀을 때 다음으로 넘어가게 설정
        answer4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow( problem.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //정답 1 눌렀을 때
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans1 == 1){
                    ans1 = 0; ans = 0;
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans1 = 1; ans = 1; answer = answer1.getText().toString();
                    ans2 = 0; ans3 = 0; ans4 = 0;
                    answer1.setBackgroundResource(R.drawable.rounded_orange);
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        //정답 2 눌렀을 때
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans2 == 1){
                    ans2 = 0; ans = 0;
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans2 = 1; ans = 2; answer = answer2.getText().toString();
                    ans1 = 0; ans3 = 0; ans4 = 0;
                    answer2.setBackgroundResource(R.drawable.rounded_orange);
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        //정답 3 눌렀을 때
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans3 == 1){
                    ans3 = 0; ans = 0;
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans3 = 1; ans = 3; answer = answer3.getText().toString();
                    ans1 = 0; ans2 = 0; ans4 = 0;
                    answer3.setBackgroundResource(R.drawable.rounded_orange);
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        //정답 4 눌렀을 때
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans4 == 1){
                    ans4 = 0; ans = 0;
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans4 = 1; ans = 4; answer = answer4.getText().toString();
                    ans1 = 0; ans2 = 0; ans3 = 0;
                    answer4.setBackgroundResource(R.drawable.rounded_orange);
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        //제출 버튼 클릭 시 데이터베이스로 데이터 보내고 창 닫으면서 결과 리턴
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (problem.getText().toString().equals(""))
                    Toast.makeText(PopupMulti.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (answer1.getText().toString().equals("") || answer2.getText().toString().equals("") || answer3.getText().toString().equals("") || answer4.getText().toString().equals(""))
                    Toast.makeText(PopupMulti.this, "정답을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (ans == 0)
                    Toast.makeText(PopupMulti.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();
                else if (comment.getText().toString().equals(""))
                    Toast.makeText(PopupMulti.this, "질문 검사를 해주세요.", Toast.LENGTH_SHORT).show();
                else if (canSubmit == false) {
                    comment.startAnimation(shake);
                }
                else {
                    Problem prob = new Problem(problem.getText().toString(), ans, 2, answer, answer1.getText().toString(), answer2.getText().toString(), answer3.getText().toString(), answer4.getText().toString());

                    databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("problem" + gv.getGameID()).setValue(prob);
                    databaseReference.child("problems").push().setValue(prob);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        //취소 버튼 누르면 팝업창 종료
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ////////////////////////////////////////////////////////문제 유사도 검사 부분///////////////////////////////////////////////////////////////////////
        checkProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (problem.getText().toString().equals(""))
                    Toast.makeText(PopupMulti.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (answer1.getText().toString().equals("") || answer2.getText().toString().equals("") || answer3.getText().toString().equals("") || answer4.getText().toString().equals(""))
                    Toast.makeText(PopupMulti.this, "정답을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (ans == 0)
                    Toast.makeText(PopupMulti.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();

                else {
                    Similarity similarity = new Similarity();

                    String[] array = new String[8];
                    array[0] = "2";
                    array[1] = gv.getScript();
                    array[2] = problem.getText().toString();
                    array[3] = "" + ans;
                    array[4] = answer1.getText().toString();
                    array[5] = answer2.getText().toString();
                    array[6] = answer3.getText().toString();
                    array[7] = answer4.getText().toString();
                    similarity.execute(array);
                    checkProb.setEnabled(false);
                    checkProb.setImageResource(R.drawable.checking);
                }
            }
        });

        problem.addTextChangedListener(new TextWatcher() {
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

        answer1.addTextChangedListener(new TextWatcher() {
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

        answer2.addTextChangedListener(new TextWatcher() {
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

        answer3.addTextChangedListener(new TextWatcher() {
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

        answer4.addTextChangedListener(new TextWatcher() {
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

    //바깥 눌러도 안꺼지게 설정
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
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
                checkProb.setEnabled(true);
                checkProb.setImageResource(R.drawable.check1);
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
                checkProb.setEnabled(true);
                checkProb.setImageResource(R.drawable.check1);
            }
        }

    }
}
