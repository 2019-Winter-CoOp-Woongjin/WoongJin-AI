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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai_winter.R;

public class PopupOX extends Activity {

    private ImageButton create, cancel;
    private ImageView buttonO, buttonX, checkProb;
    private EditText problem;
    private TextView comment;

    private GlobalApplication gv;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //intent 되고난 후 activity 지우기 위한 설정
    public static PopupOX activity = null;

    private Boolean canSubmit;

    private String realAnswer = null;
    private int pushO, pushX;

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
        setContentView(R.layout.activity_battleox);

        create = findViewById(R.id.create_prob);
        cancel = findViewById(R.id.cancel_prob);
        buttonO = findViewById(R.id.o);
        buttonX = findViewById(R.id.x);

        problem = findViewById(R.id.editProblem);
        problem.setMovementMethod(new ScrollingMovementMethod());

        checkProb = findViewById(R.id.checkProb);
        comment = findViewById(R.id.problemComment);


        //스크립트파일보기버튼 연결
        showScript = findViewById(R.id.showScript);

        //전역변수 선언
        gv = (GlobalApplication) getApplication();

        canSubmit = false;

        //흔들리는 애니메이션
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        pushO = 0;
        pushX = 0;

        //스크립트 파일보기버튼 눌렀을때
        showScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentScript = new Intent(PopupOX.this, PopupScript.class);
                intentScript.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentScript);

            }
        });

        ////////////////////////////////////////////////////////문제 유사도 검사 부분///////////////////////////////////////////////////////////////////////
        checkProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (problem.getText().toString().equals(""))
                    Toast.makeText(PopupOX.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if ((pushO + pushX) == 0)
                    Toast.makeText(PopupOX.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();
                else if ((pushO + pushX) == 2)
                    Toast.makeText(PopupOX.this, "정답을 하나만 선택 해주세요.", Toast.LENGTH_SHORT).show();

                else {
                    Similarity similarity = new Similarity();

                    String[] array = new String[8];
                    array[0] = "1";
                    array[1] = gv.getScript();
                    array[2] = problem.getText().toString();
                    array[3] = realAnswer;
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
                    buttonO.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow( problem.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        buttonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //O가 눌려있을 때
                if(pushO == 1) {
                    pushO = 0;
                    realAnswer = null;
                    buttonO.setImageResource(R.drawable.o_orange);
                }
                //X가 눌려있을 때
                else if (pushX == 1) {
                    realAnswer = "O";
                    pushX = 0;
                    pushO = 1;
                    buttonO.setImageResource(R.drawable.ic_icons_blue_o);
                    buttonX.setImageResource(R.drawable.x_orange);
                }
                else {
                    pushO = 1;
                    realAnswer = "O";
                    buttonO.setImageResource(R.drawable.ic_icons_blue_o);
                }

                if (canSubmit == true) {
                    canSubmit = false;
                    comment.setText("");
                }
            }
        });

        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //X가 눌려있을 때
                if(pushX == 1) {
                    pushX = 0;
                    realAnswer = null;
                    buttonX.setImageResource(R.drawable.x_orange);
                }
                //X가 눌려있을 때
                else if (pushO == 1) {
                    realAnswer = "X";
                    pushO = 0;
                    pushX = 1;
                    buttonX.setImageResource(R.drawable.ic_icons_red_x);
                    buttonO.setImageResource(R.drawable.o_orange);
                }
                else {
                    pushX = 1;
                    realAnswer = "X";
                    buttonX.setImageResource(R.drawable.ic_icons_red_x);
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
                    Toast.makeText(PopupOX.this, "문제를 입력 해주세요.", Toast.LENGTH_SHORT).show();

                else if ((pushO + pushX) == 0)
                    Toast.makeText(PopupOX.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();
                else if ((pushO + pushX) == 2)
                    Toast.makeText(PopupOX.this, "정답을 하나만 선택 해주세요.", Toast.LENGTH_SHORT).show();
                else if (comment.getText().toString().equals(""))
                    Toast.makeText(PopupOX.this, "질문 검사를 해주세요.", Toast.LENGTH_SHORT).show();

                else if (canSubmit == false) {
                    comment.startAnimation(shake);
                }

                else if ((pushO + pushX) == 1) {
                    Problem prob = new Problem(problem.getText().toString(), realAnswer, 1);

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
