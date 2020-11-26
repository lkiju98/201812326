package kr.ac.konkuk.tab;

import java.util.ArrayList;

public class MainData {
    private String tabataname;
    private String item_name;
    private int set;
    private ArrayList<String> it_strList;
    private int exercisetime;
    private int resttime;
    private int settime;


    //tabata관련 데이터 관리를 쉽게 하기 위해 만든 MainData
    public MainData(String tabataname, String item_name, int set, ArrayList<String> it_strList, int exercisetime, int resttime, int settime) {
        this.tabataname = tabataname;
        this.item_name = item_name;
        this.set = set;
        this.it_strList = it_strList;
        this.exercisetime = exercisetime;
        this.resttime = resttime;
        this.settime = settime;
    }


    public String getTabataname() { return tabataname; }

    public void setTabataname(String tabataname) { this.tabataname= tabataname; }

    public String getItem_name() { return item_name; }

    public void setItem_name(String tv_name) { this.item_name = item_name; }

    public String get_set() { return String.valueOf(set); }

    public void set_set(int set) { this.set = set; }

    public ArrayList<String> getIt_strList() { return it_strList;  }

    public void setIt_strList(ArrayList<String> tv_content) { this.it_strList = it_strList; }

    public String get_exercisetime() { return String.valueOf(exercisetime); }

    public void set_exercisetime(int exercisetime) { this.exercisetime = exercisetime; }

    public String get_resttime() { return String.valueOf(resttime); }

    public void set_resttime(int resttime) { this.resttime = resttime; }

    public String get_settime() { return String.valueOf(settime); }

    public void set_settime(int settime) { this.settime = settime; }
}
