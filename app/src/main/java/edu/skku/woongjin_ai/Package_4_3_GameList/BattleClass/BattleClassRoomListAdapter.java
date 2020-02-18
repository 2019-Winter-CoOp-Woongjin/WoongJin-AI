package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.R;

public class BattleClassRoomListAdapter extends RecyclerView.Adapter<BattleClassRoomListAdapter.CustomViewHolder> {

    //RoomInfo 정보를 배열에 입력
    private ArrayList<RoomInfo> arrayList;

    //어뎁터에서 액티비티 액션들을 가져올때 context가 필요
    private Context context;

    //클릭 이벤트하기위해
    private OnNoteListener mOnNoteListener;

    public BattleClassRoomListAdapter(ArrayList<RoomInfo> arrayList, Context context, OnNoteListener onNoteListener){
        this.arrayList = arrayList;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //roomnameitem xml을 연결, 리사이클러 한 컬럼 만드는거 선언해주는것
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.roomnameitem, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mOnNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        // 각 아이템 위치를 표시
        holder.tv_roomname.setText(arrayList.get(position).getRoomname());
        holder.tv_hostname.setText(arrayList.get(position).getHostname());
        holder.tv_bookname.setText(arrayList.get(position).getBookname());
        holder.tv_personnum.setText(arrayList.get(position).getPersonnum());
        holder.tv_roomnum.setText(arrayList.get(position).getRoomnum());

        holder.roomname1.setText(arrayList.get(position).getRoomname1());
        holder.hostname1.setText(arrayList.get(position).getHostname1());
        holder.bookname1.setText(arrayList.get(position).getBookname1());
        holder.personnum1.setText(arrayList.get(position).getPersonnum1());
        holder.roomnum1.setText(arrayList.get(position).getRoomnum1());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //list_item에 만든 각 xml들을 연결하기 위해 선언
        TextView tv_roomname;
        TextView tv_hostname;
        TextView tv_bookname;
        TextView tv_personnum;
        TextView tv_roomnum;
        OnNoteListener onNoteListener;

        //왼쪽꺼.. 파베말고 그냥 책이름 이런거 뛰울꺼
        TextView roomname1;
        TextView hostname1;
        TextView bookname1;
        TextView personnum1;
        TextView roomnum1;

        public CustomViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            //list_item에 만든 xml들을 연결 , viewholder 상속을 가져와서 아이디값을 가져와야함
            this.tv_roomname = itemView.findViewById(R.id.tv_roomname);
            this.tv_hostname = itemView.findViewById(R.id.tv_hostname);
            this.tv_bookname = itemView.findViewById(R.id.tv_bookname);
            this.tv_personnum = itemView.findViewById(R.id.tv_personnum);
            this.tv_roomnum = itemView.findViewById(R.id.tv_roomnum);

            //왼쪽꺼
            this.roomname1 = itemView.findViewById(R.id.roomname1);
            this.hostname1 = itemView.findViewById(R.id.hostname1);
            this.bookname1 = itemView.findViewById(R.id.bookname1);
            this.personnum1 = itemView.findViewById(R.id.personnum1);
            this.roomnum1 = itemView.findViewById(R.id.roomnum1);


            //액티비티에서 클릭이벤트하기위해
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
            //v.setBackgroundColor(Color.GRAY);
        }
    }

    //다른액티비티에서 클릭이벤트 추가하기 위해
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}