package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.MakingRoom;


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

public class MakingRoomAdapter extends RecyclerView.Adapter<MakingRoomAdapter.CustomViewHolder> {

    //BookName정보를 배열에 입력
    private ArrayList<BookName> arrayList;

    //어뎁터에서 액티비티 액션들을 가져올때 context가 필요
    private Context context;

    //클릭 이벤트하기위해
    private OnNoteListener mOnNoteListener;

    //클릭시 배경화면 색 바꾸기위해
    //onClick 이벤트 발생시, 클릭된 아이템의 position( ViewHolder.getAdapterPosition() )과 선택상태를 저장해 놓고 토글
    //position별 선택상태를 저장하는 구조는 SparseBooleanArray를 사용
    //private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    private int selectedPosition = -1;

    public MakingRoomAdapter(ArrayList<BookName> arrayList, Context context, OnNoteListener onNoteListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    /*//파베 사용하기위해
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
    private DatabaseReference databaseReference = firebaseDatabase.getReference(); //DB 테이블 연결*/



    @NonNull
    @Override
    //리스트뷰가 어뎁터가 연결된 다음 뷰홀더를 최초로 만드는 것
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //booknameitem xml을 연결, 리사이클러 한 컬럼 만드는거 선언해주는것
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booknameitem, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mOnNoteListener);

        return holder;
    }

    @Override // 실제 각 아이을 매칭
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        holder.tv_name.setText(arrayList.get(position).getName()); // 각 아이템 위치를 표시
        holder.tv_book_name.setText(arrayList.get(position).getBook_name());

        //클릭시 rgb 색으로..되게..
        if(selectedPosition == position) {
            holder.tv_name.setBackgroundColor(Color.rgb(255,222,48));
            holder.tv_book_name.setBackgroundColor(Color.rgb(255,222,48));
        }
        else {//다른거는 원래의 색으로 돌아오게 함.
            holder.tv_name.setBackgroundColor(Color.TRANSPARENT);
            holder.tv_book_name.setBackgroundColor(Color.TRANSPARENT);
        }

        /* 어뎁터에서 클릭이벤트 하는법 밑에 중요
        // 이러면 전역변수를 읽지 못해 액티비티에서 클릭이벤트 설정해야함.

        //각 컬럼 클릭시 이벤트 추가합니당
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String curbookName = holder.tv_bookName.getText().toString(); // 현재 책이름은 getText() object형태기 때문에 string으로 변환

                //databaseReference.child("game").child("room").child("roomName").setValue(curbookName);
                Toast.makeText(v.getContext(), curbookName, Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    @Override
    public int getItemCount() {

        //삼향 연산자 arrayList가 null이 아니면 arrayList size를 가져오고 null이면 0을 가져와라
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //list_item에 만든 각 xml들을 연결하기 위해 선언

        TextView tv_name;
        TextView tv_book_name;
        OnNoteListener onNoteListener;

        public CustomViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            //list_item에 만든 xml들을 연결 , viewholder 상속을 가져와서 아이디값을 가져와야함
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_book_name = itemView.findViewById(R.id.tv_book_name);

            //액티비티에서 클릭이벤트하기위해
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
        /*
            //클릭시 배경화면 회색, 1개만 클릭되도록해야함!!!
            int position = getAdapterPosition();
            Log.d("test1","position="+position);
            selectedPosition = position;
            Log.d("test2","position2="+selectedPosition);
            notifyDataSetChanged();

            if(selectedPosition == position){
                v.setBackgroundColor(Color.GRAY);
            } else {
                v.setBackgroundColor(Color.TRANSPARENT);
            }*/

            //여러개 클릭되는데 데이터 이상하게 들어가는거
            /*
            int position = getAdapterPosition();

            if ( mSelectedItems.get(position, false) ){
                mSelectedItems.put(position, false);
                v.setBackgroundColor(Color.TRANSPARENT);
            } else {
                mSelectedItems.put(position, true);
                v.setBackgroundColor(Color.GRAY);
            }*/

            //1개 클릭되는거
            //v.setBackgroundColor(Color.GRAY);

        }
    }

    //다른액티비티에서 클릭이벤트 추가하기 위해
    public interface OnNoteListener{
        void onNoteClick(int position);
    }


}
