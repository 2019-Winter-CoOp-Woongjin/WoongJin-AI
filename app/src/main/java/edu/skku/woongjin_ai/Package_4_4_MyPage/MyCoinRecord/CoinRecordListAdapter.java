package edu.skku.woongjin_ai_winter.Package_4_4_MyPage.MyCoinRecord;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.R;

public class CoinRecordListAdapter extends BaseAdapter {

    private ArrayList<CoinRecordListItem> coinRecordListItems = new ArrayList<CoinRecordListItem>();

    @Override
    public int getCount() {
        return coinRecordListItems.size();
    }

    @Override
    public CoinRecordListItem getItem(int position) {
        return coinRecordListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_coinrecord, parent, false);
        }

        TextView why=(TextView)convertView.findViewById(R.id.WHY);
        TextView howmany=(TextView)convertView.findViewById(R.id.HOWMANY);
        TextView date=(TextView)convertView.findViewById(R.id.DATE);

        CoinRecordListItem coinRecordListItem = getItem(position);

        String beforeparse=coinRecordListItem.getDate();
        String year, month, day, hour, minute;
        year=beforeparse.substring(0, 2);
        month=beforeparse.substring(2, 4);
        day=beforeparse.substring(4, 6);
        hour=beforeparse.substring(6, 8);
        minute=beforeparse.substring(8);

        why.setText(coinRecordListItem.getWhy());
        String get=coinRecordListItem.getHowmany();
        if(get.substring(0,1).equals("-")){
            howmany.setTextColor(Color.parseColor("#EB0000"));
            howmany.setText(get);
        }else{
            howmany.setText("+"+get);
        }
        date.setText("20"+year+"년 "+ month+"월 "+day+"일 "+hour+"시"+minute+"분");


        return convertView;
    }

    public void addItem(String howmany, String date, String why) {
        CoinRecordListItem coinRecordListItem = new CoinRecordListItem();
        coinRecordListItem.setHowmany(howmany);
        coinRecordListItem.setDate(date);
        coinRecordListItem.setWhy(why);
        coinRecordListItems.add(coinRecordListItem);
    }
}
