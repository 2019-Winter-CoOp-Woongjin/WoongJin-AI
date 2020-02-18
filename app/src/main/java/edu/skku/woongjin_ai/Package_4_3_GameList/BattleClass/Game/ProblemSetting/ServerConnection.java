package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSetting;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnection {

    private String url = "http://34.84.249.147:8080/api/analyze";

    public String urlConnection(String json) throws IOException {
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setReadTimeout(30000);
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String getJson;
            while ((getJson = bufferedReader.readLine()) != null) {
                sb.append(getJson + "\n");
            }
        }

        bufferedReader.close();

        conn.disconnect();

        return sb.toString().trim();
    }

    public String makeJson(String[] params) throws JSONException {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("text", params[1]);
        jsonObject.accumulate("question", params[2]);
        int int_type = Integer.parseInt(params[0]);
        int q_type = 0;
        //OX
        if (int_type == 1) {
            //answer = [O = 0, X = 1]
            q_type = 0;
            if (params[3].equals("O"))
                jsonObject.accumulate("answer", 0);
            else
                jsonObject.accumulate("answer", 1);

        }
        //객관식
        else if (int_type == 2) {
            //answer = {selection=1, choice1="asdf", choice2="asdf", choice3="asdf", choice4="asdf"}
            q_type = 1;
            String send_answer = "";
            JSONObject jsonAnswer = new JSONObject();
            jsonAnswer.accumulate("selection", Integer.parseInt(params[3]));
            jsonAnswer.accumulate("choices", params[4]);
            jsonAnswer.accumulate("choices", params[5]);
            jsonAnswer.accumulate("choices", params[6]);
            jsonAnswer.accumulate("choices", params[7]);
            jsonObject.accumulate("answer", jsonAnswer);

        }
        //주관식
        else if (int_type == 3) {
            q_type = 2;
            jsonObject.accumulate("answer", params[3]);
        }

        jsonObject.accumulate("q_type", q_type);

        json = jsonObject.toString().trim();

        return json;
    }

    //is_correct true false
    //matched 지문과 질문 관계 true false

    public Boolean[] get_result(String json) {

        JSONObject resultJson = new JSONObject();
        Boolean[] result = new Boolean[2];
        try {
            resultJson = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result[0] = resultJson.getBoolean("matched");
            result[1] = resultJson.getBoolean("is_correct");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
