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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai_winter.R;

public class PopupSmall extends Activity {

    private ImageButton create, cancel;
    private ImageView checkProb;
    private EditText problem, answer;
    private TextView comment;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    private GlobalApplication gv;

    //intent 되고난 후 activity 지우기 위한 설정
    public static PopupSmall activity = null;

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
        setContentView(R.layout.activity_battlepopupsmall);

        create = findViewById(R.id.create_prob);
        cancel = findViewById(R.id.cancel_prob);
        checkProb = findViewById(R.id.checkProb);

        problem = findViewById(R.id.editProblem);
        problem.setMovementMethod(new ScrollingMovementMethod());

        answer = findViewById(R.id.editAns);
        comment = findViewById(R.id.problemComment);

        //스크립트파일보기버튼 연결
        showScript = findViewById(R.id.showScript);

        gv = (GlobalApplication) getApplication();

        canSubmit = false;

        //흔들리는 애니메이션
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        //스크립트 파일보기버튼 눌렀을때
        showScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentScript = new Intent(PopupSmall.this, PopupScript.class);
                intentScript.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentScript);

            }
        });

        ////////////////////////////////////////////////////////문제 유사도 검사 부분///////////////////////////////////////////////////////////////////////
        checkProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (problem.getText().toString().equals(""))
                    Toast.makeText(PopupSmall.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (answer.getText().toString().equals(""))
                    Toast.makeText(PopupSmall.this, "정답을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else {
                    Similarity similarity = new Similarity();

                    String[] array = new String[8];
                    array[0] = "3";
                    array[1] = gv.getScript();
                    array[2] = problem.getText().toString();
                    array[3] = answer.getText().toString();
                    array[4] = null;
                    array[5] = null;
                    array[6] = null;
                    array[7] = null;

                    similarity.execute(array);
                    checkProb.setEnabled(false);
                    checkProb.setImageResource(R.drawable.checking);
                }
            }
        });

        problem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    answer.requestFocus();
                    return true;
                }
                return false;
            }
        });

        answer.setOnKeyListener(new View.OnKeyListener() {
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

        //제출 버튼 클릭 시 데이터베이스로 데이터 보내고 창 닫으면서 결과 리턴
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (problem.getText().toString().equals(""))
                    Toast.makeText(PopupSmall.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if (answer.getText().toString().equals(""))
                    Toast.makeText(PopupSmall.this, "정답을 입력 해주세요.", Toast.LENGTH_SHORT).show();
                else if (comment.getText().toString().equals(""))
                    Toast.makeText(PopupSmall.this, "질문 검사를 해주세요.", Toast.LENGTH_SHORT).show();

                else if (canSubmit == false) {
                    comment.startAnimation(shake);
                }

                else {
                    Problem prob = new Problem(problem.getText().toString(), answer.getText().toString(), 3);

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

        answer.addTextChangedListener(new TextWatcher() {
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
