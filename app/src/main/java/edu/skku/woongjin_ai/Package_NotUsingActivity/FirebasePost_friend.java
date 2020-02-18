package edu.skku.woongjin_ai_winter.Package_NotUsingActivity;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost_friend {
    public String nickname;
    public String name;
    public FirebasePost_friend(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost_list.class)
    }
    public FirebasePost_friend(String nickname, String name) {
        this.nickname = nickname;
        this.name = name;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nickname", nickname);
        result.put("name", name);
        return result;
    }
}
