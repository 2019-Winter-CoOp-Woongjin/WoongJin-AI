package edu.skku.woongjin_ai;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.kakao.auth.KakaoSDK;

import edu.skku.woongjin_ai.Package_2_2_KaKao.KakaoSDKAdapter;

public class GlobalApplication extends Application {
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;
    private String mGlobalID, mGlobalprofile, mGlobalgrade, mGlobalschool, mGlobalnickname, mGlobalname;


    //겨울동계 추가, battleclass위해

    private int gameID;
    private int roomnum;
    private int life;
    private int prob;
    private int solve;

    //겨울동계 추가, script 전역변수로..
    private String script;
    private String BookName;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());

        //겨울동계추가
        life = 3;
        prob = 0;
        solve = 0;
    }
    public static GlobalApplication getGlobalApplicationContext() {
        return obj;
    }
    public static Activity getCurrentActivity() {
        return currentActivity;
    }
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    private static GlobalApplication instance = null;


    public static synchronized GlobalApplication getInstance(){

        if(null == instance){

            instance = new GlobalApplication();

        }

        return instance;

    }


    public void setGlobalStr(String globalID, String globalProfile, String globalGrade, String globalSchool, String globalNickname, String globalName){

        this.mGlobalID = globalID;
        this.mGlobalprofile = globalProfile;
        this.mGlobalgrade = globalGrade;
        this.mGlobalschool = globalSchool;
        this.mGlobalnickname = globalNickname;
        this.mGlobalname = globalName;
    }

    public String getGlobalID(){
        return mGlobalID;
    }

    public String getGlobalProfile(){
        return mGlobalprofile;
    }

    public String getGlobalGrade(){
        return mGlobalgrade;
    }

    public String getGlobalNickname(){
        return mGlobalnickname;
    }

    public String getGlobalName(){
        return mGlobalname;
    }

    public String getGlobalSchool(){
        return mGlobalschool;
    }

    public void startToast(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }


    //겨울동계추가
    @Override
    public void onTerminate(){
        super.onTerminate();
    }



    public void setmGlobalID(String mGlobalID) {
        this.mGlobalID = mGlobalID;
    }



    public void setmGlobalname(String mGlobalname) {
        this.mGlobalname = mGlobalname;
    }

    public int getGameID() { return gameID; }

    public void setGameID (int gameID) {
        this.gameID = gameID;
    }

    public int getRoomnum() {return roomnum;}

    public void setRoomnum(int roomnum){this.roomnum = roomnum;}

    public int getLife() {
        return life;
    }

    public void setLife (int life) {
        this.life = life;
    }

    public int getProb() {
        return prob;
    }

    public void setProb (int prob) {
        this.prob = prob;
    }

    public int getSolve() {
        return solve;
    }

    public void setSolve (int solve) {
        this.solve = solve;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getBookName(){
        return BookName;
    }

    public void setBookName(String BookName){
        this.BookName = BookName;
    }
}