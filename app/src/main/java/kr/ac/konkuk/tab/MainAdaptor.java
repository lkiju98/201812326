package kr.ac.konkuk.tab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainAdaptor extends RecyclerView.Adapter<MainAdaptor.CustomViewHolder> {

    public static ArrayList<MainData> arrayList;
    public MainAdaptor(ArrayList<MainData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MainAdaptor.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdaptor.CustomViewHolder holder, int position) {

        //tabata정보들을 화면에 나타내어주기
        String type=arrayList.get(position).getItem_name();
        String sets=arrayList.get(position).get_set();
        String resttime=arrayList.get(position).get_resttime();
        String exercisetime=arrayList.get(position).get_exercisetime();
        String settime=arrayList.get(position).get_settime();

        String namecontent="";
        ArrayList<String> exercises=arrayList.get(position).getIt_strList();
        for(int j=0;j<exercises.size();j++){
            if(j!=exercises.size()-1){
                namecontent=namecontent+exercises.get(j)+" & ";
            }else{
                namecontent=namecontent+exercises.get(j);
            }
        }

        String infocontent="tabata type = "+type+"\nsets = "+sets+"\n(exercise/rest/set break) = "+exercisetime+"/"+resttime+"/"+settime;

        holder.tabataname.setText(arrayList.get(position).getTabataname());
        holder.item_name.setText(namecontent);
        holder.item_sets.setText(infocontent);
        holder.itemView.setTag(position);

        //---------------------------------item 눌렀을때-fragment2로 이동하면서 timer실행준비------------------------------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //item 눌렀을때

                ((MainActivity)MainActivity.mContext).replaceFragment(); //타이머 화면으로 옮겨주기

                Bundle bundle = new Bundle();
                bundle.putInt("index", holder.getAdapterPosition()); //선택된 item의 index정보를 fragment1에서 fragment2로 넘겨주기

                Fragment2.newInstance(bundle); //fragment의 bundle값 update해주기

            }
        });

        //---------------------------item 길게 눌렀을때-alertdislog 띄운후 삭제하는게 맞다면 remove()함수로 삭제하기-----------------------------------------
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder((MainActivity.mContext));
                builder.setTitle("정말 삭제하시겠습니까?");
                //builder.setMessage();
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String curName = holder.tabataname.getText().toString();
                        Toast.makeText(v.getContext(), curName + "삭제", Toast.LENGTH_SHORT).show(); //삭제됨을 Toast로 알림
                        remove(holder.getAdapterPosition()); //현재화면에서 해당 타바타 삭제
                        ((MainActivity) MainActivity.mContext).delete(curName); //DB에서 선택한 타바타이름과 같은 정보를 delete
                    }
                });
                builder.setNegativeButton("아니요",null);
                builder.create().show();

                return true;
            }
        });

    }

    @Override //총 아이템 개수
    public int getItemCount() {
        return (null != arrayList? arrayList.size():0);
    }

    //tabata 삭제... item을 길게 누르면 실행됨
    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    //recyclerview에서 tabata의 이름 및 정보들 나타내주기
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tabataname;
        protected TextView item_name;
        protected TextView item_sets;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tabataname=(TextView)itemView.findViewById(R.id.tabataname);
            this.item_name=(TextView)itemView.findViewById(R.id.item_name);
            this.item_sets=(TextView)itemView.findViewById(R.id.item_sets);
                    }
    }

}
