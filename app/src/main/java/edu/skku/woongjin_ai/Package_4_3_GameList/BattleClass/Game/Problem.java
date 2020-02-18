package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game;

public class Problem {
    public String problem;
    public String answer;
    public int type;
    public int answerNum;
    public String ans1;
    public String ans2;
    public String ans3;
    public String ans4;

    public Problem(){

    }

    public Problem(String problem, String answer, int type) {
        this.problem = problem;
        this.answer = answer;
        this.type = type;
    }

    public Problem(String problem, int answerNum, int type, String answer, String ans1, String ans2, String ans3, String ans4) {
        this.problem = problem;
        this.answerNum = answerNum;
        this.type = type;
        this.answer = answer;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
    }


}