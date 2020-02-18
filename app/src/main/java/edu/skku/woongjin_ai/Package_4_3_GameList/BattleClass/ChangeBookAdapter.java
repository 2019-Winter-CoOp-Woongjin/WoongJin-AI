package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import edu.skku.woongjin_ai.R;

public class ChangeBookAdapter extends RecyclerView.Adapter<ChangeBookAdapter.CustomViewHolder>{

    //BookName정보를 배열에 입력
    private ArrayList<ChangeBookName> arrayList;

    //어뎁터에서 액티비티 액션들을 가져올때 context가 필요
    private Context context;

    //클릭 이벤트하기위해
    private ChangeBookAdapter.OnNoteListener mOnNoteListener;

    private int selectedPosition = -1;

    public ChangeBookAdapter(ArrayList<ChangeBookName> arrayList, Context context, ChangeBookAdapter.OnNoteListener onNoteListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }



    @NonNull
    @Override
    //리스트뷰가 어뎁터가 연결된 다음 뷰홀더를 최초로 만드는 것
    public ChangeBookAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //booknameitem xml을 연결, 리사이클러 한 컬럼 만드는거 선언해주는것
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.changebooknameitem, parent, false);
        ChangeBookAdapter.CustomViewHolder holder = new ChangeBookAdapter.CustomViewHolder(view, mOnNoteListener);

        return holder;
    }

    @Override // 실제 각 아이을 매칭
    public void onBindViewHolder(@NonNull final ChangeBookAdapter.CustomViewHolder holder, int position) {

        holder.tv_name2.setText(arrayList.get(position).getName()); // 각 아이템 위치를 표시
        holder.tv_book_name2.setText(arrayList.get(position).getBook_name());

        //클릭시 rgb 색으로..되게..
        if(selectedPosition == position) {
            holder.tv_name2.setBackgroundColor(Color.rgb(255,222,48));
            holder.tv_book_name2.setBackgroundColor(Color.rgb(255,222,48));
        }
        else {//다른거는 원래의 색으로 돌아오게 함.
            holder.tv_name2.setBackgroundColor(Color.TRANSPARENT);
            holder.tv_book_name2.setBackgroundColor(Color.TRANSPARENT);
        }



    }

    @Override
    public int getItemCount() {

        //삼향 연산자 arrayList가 null이 아니면 arrayList size를 가져오고 null이면 0을 가져와라
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //list_item에 만든 각 xml들을 연결하기 위해 선언

        TextView tv_name2;
        TextView tv_book_name2;
        ChangeBookAdapter.OnNoteListener onNoteListener;

        public CustomViewHolder(@NonNull View itemView, ChangeBookAdapter.OnNoteListener onNoteListener) {
            super(itemView);

            //list_item에 만든 xml들을 연결 , viewholder 상속을 가져와서 아이디값을 가져와야함
            this.tv_name2 = itemView.findViewById(R.id.tv_name2);
            this.tv_book_name2 = itemView.findViewById(R.id.tv_book_name2);

            //액티비티에서 클릭이벤트하기위해
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();

        }
    }

    //다른액티비티에서 클릭이벤트 추가하기 위해
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
