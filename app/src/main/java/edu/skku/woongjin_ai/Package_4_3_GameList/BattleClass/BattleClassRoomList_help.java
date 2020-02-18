package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import edu.skku.woongjin_ai_winter.R;


//Battleclassroomlist activity 에서 도움말 눌렀을때 발생

public class BattleClassRoomList_help extends AppCompatActivity {

    private ImageView buttonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battleclassroomlist_help);

        //상태바 제거(전체화면 모드)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonClose = findViewById(R.id.buttonClose);
        //취소버튼 눌렀을때

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

}