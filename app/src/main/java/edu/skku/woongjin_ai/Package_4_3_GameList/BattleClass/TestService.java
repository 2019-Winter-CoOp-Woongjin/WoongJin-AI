package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.skku.woongjin_ai.GlobalApplication;

public class TestService extends Service {

    //firebase 선언
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    GlobalApplication gv;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        
        gv = (GlobalApplication) getApplication();

        databaseReference.child("user_list").child(""+gv.getGlobalID()).child("onoffline").setValue(false);
        // 사용자 확인
        if (gv.getGameID() == 1) //guest일경우
        {databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).removeValue();
            stopSelf();
        }
        else if (gv.getGameID() == 0) { //host일경우 방을 삭제
            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).removeValue();
            stopSelf();
        }

    }
}
