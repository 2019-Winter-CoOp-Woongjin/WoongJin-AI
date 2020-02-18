package edu.skku.woongjin_ai_winter.Package_4_3_GameList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.R;

public class GameListAdapter extends BaseAdapter {

    private ArrayList<GameListItem> gameListItems = new ArrayList<GameListItem>();

    @Override
    public int getCount() {
        return gameListItems.size();
    }

    @Override
    public GameListItem getItem(int position) {
        return gameListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewcustom_gamelist, parent, false);
        }

        TextView withwhom=(TextView)convertView.findViewById(R.id.withwhom);
        TextView roomname=(TextView)convertView.findViewById(R.id.gameroomname);
        TextView status=(TextView) convertView.findViewById(R.id.game_status);

        GameListItem gameListItem = getItem(position);

        withwhom.setText(gameListItem.getWithwhom());
        roomname.setText(gameListItem.getRoomname());
        String state=gameListItem.getStatus();
        if(state.equals("end")){
            status.setTextColor(Color.parseColor("#696969")); //회색
            status.setText("같이\n다 풀었어요!");
        }else if(state.equals("iwin")){
            status.setTextColor(Color.parseColor("#8A2BE2")); //보라색
            status.setText("내가\n이겼어요!");
        } else if(state.equals("youwin")){
            status.setTextColor(Color.parseColor("#EE82EE")); //분홍색
            status.setText("아쉽게도\n졌어요...");
        } else if (state.equals("makeplease")) {
            status.setTextColor(Color.parseColor("#00FA9A")); //민트색
            status.setText("첫 문제를\n만들어보아요!");
        } else if(state.equals("newbomb")){
            status.setTextColor(Color.parseColor("#EB0000")); //빨간색
            status.setText("새 폭탄이\n도착했어요!");
        }else if(state.equals("myturn")){
            status.setTextColor(Color.parseColor("#3399CC")); //파란색
            status.setText("내가 폭탄\n만들 차례!");
        }else if(state.equals("elsenotsolve")){
            status.setTextColor(Color.parseColor("#FF9900")); //주황색
            status.setText("친구가 폭탄\n푸는 중!"); //친구가 폭탄을 아직 풀지 않음
        }else if(state.equals("elsenotmake")){
            status.setTextColor(Color.parseColor("#504A17")); //갈색
            status.setText("친구가 폭탄\n만드는 중!"); //친구가 폭탄을 아직 만들지 않음
        }
        return convertView;
    }

    public void addItem(String roomname, String withwhom, String status) {
        GameListItem gameListItem = new GameListItem();
        gameListItem.setRoomname(roomname);
        gameListItem.setWithwhom(withwhom);
        gameListItem.setStatus(status);
        gameListItems.add(gameListItem);
    }
}
