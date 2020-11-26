package kr.ac.konkuk.tab;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;


public class Fragment1 extends Fragment {

    public Fragment1() {  }

    private ArrayList<MainData> arrayList;
    private MainAdaptor mainAdaptor;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //처음 실행되었을때, DB에서 기존 tabata들의 정보들을 가져와 recyclerView에 item화하여 나타내어준다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview1 = (ViewGroup)inflater.inflate (R.layout.fragment_1,container,false);

        recyclerView= (RecyclerView) rootview1.findViewById(R.id.rv);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); //recyclerview를 활용해서 리소스를 아꼈다.

        arrayList=new ArrayList<>(); //tabata들의 정보 및 타이머 실행과 연결해주는 item을 담은 arraylist
        mainAdaptor=new MainAdaptor(arrayList);
        recyclerView.setAdapter(mainAdaptor); //MainAdaptor에서 recyclerView에 담긴 item들을 관리해준다.

        //DB에 있는 기존 tabata정보들을 item화하여 arrayList에 넣어준다.
        ArrayList<MainData> firstarrayList=((MainActivity)MainActivity.mContext).firstdbsetting();
        for(int w=0;w<firstarrayList.size();w++){
            arrayList.add(firstarrayList.get(w));
            mainAdaptor.notifyDataSetChanged(); //넣어주고 변화를 업데이트해준다.
        }

        Button btn_add=(Button)rootview1.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickAdd();
            }
        });
        return rootview1;
    }


    //<+ADD>버튼을 눌렀을때
    public void ClickAdd (){
        final Dialog dialog= new Dialog(getActivity()); //dialog 만들기

        dialog.setContentView(R.layout.custom_setting); //custom_dialog.xml 불러오기
        final Button pre = (Button) dialog.findViewById(R.id.SettingPre); //확인버튼 가져오기 (pbutton)
        final Button next = (Button) dialog.findViewById(R.id.SettingNext); //취소버튼 가져오기 (nbutton)
        final EditText types = (EditText) dialog.findViewById(R.id.types);
        final RadioGroup rg= (RadioGroup) dialog.findViewById(R.id.radioGroup);

        //---------------------------------------dialog창 띄우기(custom_setting.xml파일)------------------------------------------------------------------
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.show(); //dialog 창띄우기

        // pre 버튼
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss(); //버튼 누르면 dialog창 끄기
            }
        });

        // next 버튼---------------------------------------dialog2창 띄우기(setting2.xml파일)---------------------------------------------------------------
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Dialog dialog2 = new Dialog(getActivity()); //dialog 만들기
                dialog2.setContentView(R.layout.setting2);

                //dislog에서 입력받은 운동종류의 개수에 따라 dialog2에 동적으로 edittext를 생성하였다.
                final LinearLayout exercisell= dialog2.findViewById(R.id.exercisell);
                final ArrayList<String> strList = new ArrayList<String>();
                int num=1;
                if (types.getText().toString().equals("") || types.getText().toString() == null){ }
                    else{num = Integer.parseInt(types.getText().toString());}
                for (int i=0;i<num;i++) {
                    EditText et = new EditText(getActivity().getApplicationContext());
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    et.setLayoutParams(p);
                    et.setHintTextColor(Color.parseColor("#495057"));
                    et.setTextColor(Color.parseColor("#ffffff"));
                    et.setHint("Exercise Name"+(i+1)+"의 이름");
                    et.setId(num);
                    exercisell.addView(et);
                }

                WindowManager.LayoutParams params = dialog2.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog2.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                dialog2.show();

                final Button pre = (Button) dialog2.findViewById(R.id.SettingPre2); //확인버튼 가져오기 (pbutton)
                final Button next = (Button) dialog2.findViewById(R.id.SettingNext2); //취소버튼 가져오기 (nbutton)

                // pre 버튼----------------------------------------------------------dialog2창 취소-------------------------------------------------------
                pre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });

                // next 버튼-------------------------------------------------------타바타 최종 생성버튼---------------------------------------------------
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //타바타이름
                        final EditText tabataname=(EditText)dialog.findViewById(R.id.tabataname);
                        String mtabataname = "";
                        if (tabataname.getText().toString().equals("") || tabataname.getText().toString() == null){ }
                        else{mtabataname = tabataname.getText().toString();}

                        //타바타 종류
                        RadioButton rd = (RadioButton) dialog.findViewById(rg.getCheckedRadioButtonId());
                        String str_Tabatatype = rd.getText().toString();

                        //타바타운동이름들
                        int num=1;
                        if (types.getText().toString().equals("") || types.getText().toString() == null){ }
                        else{num = Integer.parseInt(types.getText().toString());}

                        for (int i=0;i<num;i++) {
                            EditText et = (EditText) exercisell.getChildAt(i);
                            if (et.getText().toString().equals("") || et.getText().toString() == null){
                                String a=Integer.toString(i+1);
                                strList.add("exercise"+a); }
                            else{strList.add(et.getText().toString());}
                        }

                        //세트수
                        final EditText sets=(EditText)dialog.findViewById(R.id.sets);
                        int set = 3;
                        if (sets.getText().toString().equals("") || sets.getText().toString() == null){ }
                        else{set = Integer.parseInt(sets.getText().toString());}


                        //시간관련값
                        final EditText exercisetimes=(EditText)dialog2.findViewById(R.id.exercisetimes);
                        int exercisetime = 10;
                        if (exercisetimes.getText().toString().equals("") || exercisetimes.getText().toString() == null){ }
                        else{exercisetime = Integer.parseInt(exercisetimes.getText().toString());}

                        final EditText resttimes=(EditText)dialog2.findViewById(R.id.resttimes);
                        int resttime = 3;
                        if (resttimes.getText().toString().equals("") || resttimes.getText().toString() == null){ }
                        else{resttime = Integer.parseInt(resttimes.getText().toString());}

                        final EditText settimes=(EditText)dialog2.findViewById(R.id.settimes);
                        int settime = 5;
                        if (settimes.getText().toString().equals("") || settimes.getText().toString() == null){ }
                        else{settime = Integer.parseInt(settimes.getText().toString());}

                        dialog.dismiss();
                        dialog2.dismiss();

                        //DB에 타바타를 추가하는 insert문 실행
                        MainData mainData= new MainData(mtabataname ,str_Tabatatype,set,strList,exercisetime,resttime,settime);
                        ((MainActivity)MainActivity.mContext).insert(mtabataname,str_Tabatatype,strList, set, exercisetime, resttime, settime);

                        //fragment1화면에도 새로 추가한 tabata나타내어주기
                        arrayList.add(mainData);
                        mainAdaptor.notifyDataSetChanged();
                    }
                });


            }
        });
    }


}