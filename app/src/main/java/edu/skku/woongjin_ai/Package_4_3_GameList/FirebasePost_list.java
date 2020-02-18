package edu.skku.woongjin_ai_winter.Package_4_3_GameList;
import java.util.HashMap;
import java.util.Map;

//GameListActivity랑 NationGameActivity에서 씀
public class FirebasePost_list {
    public String roomname;
    public String user1;
    public String user2;
    public String script;
    public String state;
    public FirebasePost_list(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost_list.class)
    }
    public FirebasePost_list(String roomname, String user1, String user2, String script, String state) {
        this.roomname = roomname;
        this.user1 = user1;
        this.user2 = user2;
        this.script = script;
        this.state = state;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", roomname);
        result.put("user1", user1);
        result.put("user2", user2);
        result.put("script", script);
        result.put("state", state);
        return result;
    }
}