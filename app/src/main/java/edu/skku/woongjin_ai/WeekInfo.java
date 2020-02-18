package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

/*
데이터베이스에서 유저의 week별 학습 정보 가져올 때
 */

public class WeekInfo {
    public int correct;
    public float level;
    public int like;
    public int cnt;
    public int made;

    public WeekInfo() {

    }

    public WeekInfo(int correct, float level, int like, int cnt, int made) {
        this.correct = correct;
        this.level = level;
        this.like = like;
        this.cnt = cnt;
        this.made = made;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("correct", correct);
        result.put("level", level);
        result.put("like", like);
        result.put("cnt", cnt);
        result.put("made", made);
        return result;
    }
}
