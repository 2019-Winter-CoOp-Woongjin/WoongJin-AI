package edu.skku.woongjin_ai_winter.Package_4_3_GameList.MakeBombType;

import java.util.HashMap;
import java.util.Map;

public class Firebase_BombChoice {
    public String question;
    public String answer;
    public String answer1;
    public String answer2;
    public String answer3;
    public String answer4;
    public String type;
    public String solve;
    public String last;

    public Firebase_BombChoice() {

    }

    public Firebase_BombChoice(String question, String answer, String answer1, String answer2, String answer3, String answer4, String type, String solve, String last) {
        this.question = question;
        this.answer = answer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.type = type;
        this.solve = solve;
        this.last = last;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("question", question);
        result.put("answer", answer);
        result.put("answer1", answer1);
        result.put("answer2", answer2);
        result.put("answer3", answer3);
        result.put("answer4", answer4);
        result.put("type", type);
        result.put("solve", solve);
        result.put("last", last);
        return result;
    }
}
