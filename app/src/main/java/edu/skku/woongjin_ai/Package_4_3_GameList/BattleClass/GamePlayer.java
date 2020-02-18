package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass;

public class GamePlayer {
    public String name;
    public int gamenum;
    public String id;
    public int ready;
    public int submit;
    public int solve;
    public int life;

    public GamePlayer(){

    }

    public void CleanPlayer(){
        ready = 0;
        submit = 0;
        solve = 0;
        life = 3;
    }

    public GamePlayer(String name, int gamenum, String id, int ready, int submit, int solve, int life) {
        this.name = name;
        this.gamenum = gamenum;
        this.id = id;
        this.ready = ready;
        this.submit = submit;
        this.solve = solve;
        this.life = life;
    }
}
