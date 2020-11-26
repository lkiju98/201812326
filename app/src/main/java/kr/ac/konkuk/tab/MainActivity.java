package kr.ac.konkuk.tab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

    public static Context mContext; //mainactivity의 context값
    private ViewPager vp;
    private VPAdapter adapter;
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷

    SQLiteDatabase db; //DB관련 변수들
    dbHelper dbhelper;
    public String str=null;


    //-------------------------------TabLayout및 화면구성 관련 함수--------------------------------------------------------
    @Override //앱이 build될때 TabLayout 화면구성 및 DB연결
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

        //viewpager와 FragmentManager로 메인화면을 화면 슬라이드가 가능한 탭layout으로 구성
        vp =findViewById(R.id.viewpager);
        adapter =new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        //Tab 연동
        TabLayout tab=findViewById(R.id.tab);
        tab.setupWithViewPager(vp);
        vp.setCurrentItem(0); //첫 시작화면은 setting으로 설정

        //DB 연동
        dbhelper=new dbHelper(this);
        try {
            db = dbhelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbhelper.getReadableDatabase();
        }

    }

    //타이머 화면으로 바꿔주는 함수
    public void replaceFragment() {
        vp.setCurrentItem(1);
    }

    //-----------------------------------------DB관련 함수------------------------------------------------------------------
    //DB에 있는 내용들을 어플이 시작하면 가져옴 (firstarrayList를 반환하여 Fragment1.java에서 처리함)
    public ArrayList<MainData> firstdbsetting(){
        Cursor cursor =db.rawQuery("SELECT * FROM tabata", null);
        ArrayList<MainData> firstarrayList =new ArrayList<>();

        while(cursor.moveToNext()){
            String a= cursor.getString(3);
            ArrayList<String> exercisearray=new ArrayList<String>();
            String[] splitText = a.split("///");
            for(int i=0;i<splitText.length;i++){
                exercisearray.add(splitText[i]);
                System.out.println(String.valueOf(i)+splitText[i]);
            }
            MainData maindata = new MainData(cursor.getString(0),cursor.getString(1),cursor.getInt(2),exercisearray,cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));
            firstarrayList.add(maindata);
        }
        return firstarrayList;
    }

    //DB insert문 실행-tabata와 관련된 정보들을 DB에 넣음
    public void insert(String mmtabataname, String str_Tabatatype, ArrayList<String> strList, int set, int exercisetime, int resttime, int settime) { //
        String tabataname= mmtabataname;
        String mstr_Tabatatype = str_Tabatatype;
        int mset=set;
        ArrayList<String> mstrList=strList;
        int mexercisetime=exercisetime;
        int mresttime=resttime;
        int msettime=settime;
        String mmstrList="";

        for(int j=0;j<mstrList.size();j++){
            mmstrList = mmstrList+mstrList.get(j)+"///";
        }

        ContentValues contentValues =new ContentValues();
        contentValues.put("mtabataname",tabataname);
        contentValues.put("tabatatype",mstr_Tabatatype);
        contentValues.put("sets",mset);
        contentValues.put("strList",mmstrList); //
        contentValues.put("exercisetime",mexercisetime);
        contentValues.put("resttime",mresttime);
        contentValues.put("settime",msettime);

        db.insert("tabata",null,contentValues);

        Toast.makeText(getApplicationContext(), "성공적으로 추가되었음", Toast.LENGTH_SHORT).show();

    }

    //DB delete문 실행-mtabataname를 기준으로 관련 정보들 삭제
    void delete(String name) {
        int result = db.delete("tabata", "mtabataname=?", new String[] {name});
    }

    //----------------------------------------Internal File관련 함수-------------------------------------------------------------
    //internal file에 record기록하기 & 수정하기 (Fragment2.java에서 운동을 마친후 저장됨)
    public void saverecord(String todayexerciserecord){

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH,-1);
        String fname = "CalendarDay{"+mFormat.format(cal.getTime())+"}"; //오늘의 날짜가 file제목임

        FileOutputStream fos=null;
        String str=callrecord(fname); //오늘의 날짜의 기존 글 가져오기

        //값저장하기
        try{
            fos=openFileOutput(fname,Context.MODE_PRIVATE);
            String content="";
            if(str==null){
                content="이날의 운동기록:\n"+todayexerciserecord;
            }else{
               content=str+"\n"+todayexerciserecord;
            }

            fos.write(content.getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //날짜 누를때마다 그날의 기록 보여주기 (Fragment3.java에서 날짜를 누를때마다 실행됨)
    public String callrecord(String fname){

        FileInputStream fis=null;

        //값불러오기
        try{
            fis=openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

            TextView textView=findViewById(R.id.textView);
            textView.setText(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;

    }


}