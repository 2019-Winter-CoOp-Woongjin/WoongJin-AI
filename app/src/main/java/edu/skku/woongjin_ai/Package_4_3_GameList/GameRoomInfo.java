package edu.skku.woongjin_ai_winter.Package_4_3_GameList;

import java.util.HashMap;
import java.util.Map;

public class GameRoomInfo {
    public String Roomname;
    public String WithWhom;
    public String Status;

    public GameRoomInfo() {

    }

    public GameRoomInfo(String roomname, String withWhom, String status){
        this.Roomname=roomname;
        this.WithWhom=withWhom;
        this.Status=status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", Roomname);
        result.put("with", WithWhom);
        result.put("status", Status);
        return result;
    }
}
