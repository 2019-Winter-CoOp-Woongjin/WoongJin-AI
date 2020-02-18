package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSetting;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.R;

public class PopupScript extends Activity {

    private String script;
    private TextView textViewScript1, textViewScript2;
    private Button close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없앰
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_battlepopupscript);

        //스크립트
        textViewScript1 = findViewById(R.id.script1);
        textViewScript2 = findViewById(R.id.script2);
        close = findViewById(R.id.close);

        //스크립트 2개로 나누기
        textViewScript1.setMovementMethod(new ScrollingMovementMethod());
        textViewScript2.setMovementMethod(new ScrollingMovementMethod());

        //스크립트 가져오기
        GlobalApplication gv = (GlobalApplication) getApplication();
        script = gv.getScript();
        String[] array=script.split("###");
        textViewScript1.setText(array[0]);
        textViewScript2.setText(array[1]);

        //닫기 버튼 눌럿을때
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
