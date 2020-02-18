package edu.skku.woongjin_ai_winter.Package_4_3_GameList.NationGame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.R;

public class ScriptListAdapter extends BaseAdapter {

    private ArrayList<ScriptListItem> ScriptListItems = new ArrayList<ScriptListItem>();

    @Override
    public int getCount() {
        return ScriptListItems.size();
    }

    @Override
    public ScriptListItem getItem(int position) {
        return ScriptListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_scriptlist, parent, false);
        }

        TextView textViewTitle = (TextView) convertView.findViewById(R.id.titleScriptList);
        TextView textViewBookName = (TextView) convertView.findViewById(R.id.booknameScriptList);

        ScriptListItem ScriptListItem = getItem(position);

        textViewTitle.setText(ScriptListItem.getTitle());
        textViewBookName.setText(ScriptListItem.getBookName());

        return convertView;
    }

    public void addItem(String title, String bookName) {
        ScriptListItem ScriptListItem = new ScriptListItem();
        ScriptListItem.setTitle(title);
        ScriptListItem.setBookName(bookName);
        ScriptListItems.add(ScriptListItem);
    }
}
