package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

/*
데이터베이스에서 유저 정보 가져올 때
 */

public class UserInfo {
    public String id;
    public String name;
    public String pw;
    public String nickname;
    public String school;
    public String gender;
    public String grade;
    public String coin;
    public String profile;
    public Boolean onoffline;

    public UserInfo(){

    }

    public UserInfo(String id, String pw, String name, String nickname, String school, String gender, String grade, String coin, String profile, Boolean value) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.name = name;
        this.nickname = nickname;
        this.school = school;
        this.gender = gender;
        this.grade = grade;
        this.coin = coin;
        this.profile = profile;
        this.onoffline = value;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("name",name);
        result.put("nickname", nickname);
        //result.put("address", address);
        result.put("school", school);
        result.put("gender", gender);
        result.put("grade", grade);
        result.put("coin", coin);
        result.put("profile", profile);
        result.put("onoffline", onoffline);
        return result;
    }
}
