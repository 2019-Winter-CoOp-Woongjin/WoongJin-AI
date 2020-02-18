package edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType;

import java.util.HashMap;
import java.util.Map;

/*
OX, 단답형 질문
 */

public class QuizOXShortwordTypeInfo {
    public String uid;
    public String question;
    public String answer;
    public String star;
    public String desc;
    public String like;
    public String key;
    public int cnt;
    public String url;
    public int type;
    public String scriptnm;
    public String book_name;

    public QuizOXShortwordTypeInfo() {

    }

    public QuizOXShortwordTypeInfo(String uid, String question, String answer, String star, String desc, String like, String key, int cnt, String url, int type, String scriptnm, String book_name) {
        this.uid = uid;
        this.question = question;
        this.answer = answer;
        this.star = star;
        this.desc = desc;
        this.like = like;
        this.key = key;
        this.cnt = cnt;
        this.url = url;
        this.type = type;
        this.scriptnm = scriptnm;
        this.book_name = book_name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("question", question);
        result.put("answer", answer);
        result.put("star", star);
        result.put("desc", desc);
        result.put("like", like);
        result.put("key", key);
        result.put("cnt", cnt);
        result.put("url", url);
        result.put("type", type);
        result.put("scriptnm", scriptnm);
        result.put("book_name", book_name);
        return result;
    }
}
