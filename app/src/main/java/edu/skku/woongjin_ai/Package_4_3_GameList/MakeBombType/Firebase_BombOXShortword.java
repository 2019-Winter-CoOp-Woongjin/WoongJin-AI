package edu.skku.woongjin_ai_winter.Package_4_3_GameList.MakeBombType;

import java.util.HashMap;
import java.util.Map;

public class Firebase_BombOXShortword {
    public String question;
    public String answer;
    public String type;
    public String solve;
    public String last;

    public Firebase_BombOXShortword() {

    }
    public Firebase_BombOXShortword(String question, String answer, String type, String solve, String last) {
        this.question = question;
        this.answer = answer;
        this.type = type;
        this.solve = solve;
        this.last = last;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("question", question);
        result.put("answer", answer);
        result.put("type", type);
        result.put("solve", solve);
        result.put("last", last);
        return result;
    }
}
