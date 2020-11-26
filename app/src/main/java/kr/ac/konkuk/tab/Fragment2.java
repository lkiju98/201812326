package kr.ac.konkuk.tab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static kr.ac.konkuk.tab.MainAdaptor.arrayList;

public class Fragment2 extends Fragment {

    private static int flag=1;
    ProgressBar progressBarView;
    public static Button btn_start;
    TextView tv_time;
    TextView current_tabata;
    TextView current_workout;
    TextView current_set;
    int progress;
    CountDownTimer countDownTimer;
    int endTime = 250;
    SoundPool soundPool;
    int soundID;

    public Fragment2() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //item이 눌렸을떄, 해당 item의 정보가 담긴 index의 값을 bundle로 넘겨받고, 이를 update함
    public static void newInstance(Bundle bundle) {
        int index = bundle.getInt("index", 0);
        Fragment fragment = VPAdapter.items.get(1);
        fragment.setArguments(bundle);
        btn_start.setText("Start Timer");
        flag=1;
    }

    //애니메이션을 이용하여 타이머가 진행됨을 눈에 잘 보이도록 함
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview2 = (ViewGroup)inflater.inflate (R.layout.fragment_2,container,false);

        progressBarView = (ProgressBar) rootview2.findViewById(R.id.view_progress_bar);
        btn_start = (Button)rootview2.findViewById(R.id.btn_start);
        tv_time= (TextView)rootview2.findViewById(R.id.tv_timer);
        current_tabata= (TextView)rootview2.findViewById(R.id.current_tabata);
        current_workout= (TextView)rootview2.findViewById(R.id.current_workout);
        current_set=(TextView)rootview2.findViewById(R.id.current_set);

        //활동이 끝날때마다 알림효과음을 발생시켜줄 soundpool
        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundID=soundPool.load(MainActivity.mContext,R.raw.beep,1);

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        //flag를 설정하여 처음 버튼을 누른것(-1)이면 타이머를 실행하고, 아니라면(1) 타이머를 멈추는 역할
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=flag*-1;
                if(flag==-1){//시작
                    btn_start.setText("QUIT");
                    fn_countdown();

                }else{//멈추기
                    btn_start.setText("Reset & ReStart");
                    countDownTimer.cancel();
                }
            }
        });
        return rootview2;
    }


    public ArrayList<String> timeroutine=new ArrayList();
    public ArrayList<String> exerciseroutine=new ArrayList();
    public ArrayList<String> setroutine=new ArrayList();
    int jj;

    MainData mainData;

    private void fn_countdown() {

        int index = getArguments().getInt("index", 0); //업데이트된 bundle에서 index값 가져오기

        //기존에 존재하던 timeroutine및 exerciseroutine을 모두 초기화해준 후, 다시 정보를 담아줌
        mainData= arrayList.get(index);
        timeroutine.clear();
        exerciseroutine.clear();
        setroutine.clear();

        String set= mainData.get_set();
        ArrayList<String> exercise= mainData.getIt_strList();
        int exercise_size=exercise.size();
        String exercisetime=mainData.get_exercisetime();
        String resttime=mainData.get_resttime();
        String settime=mainData.get_settime();

        current_tabata.setText(mainData.getTabataname());

        System.out.println(mainData.getItem_name());


        //타입별("ABC../ABC../ABC.."?""AAAA/BBB/CCC ? 운동 및 시간 루틴 만들기
        if(mainData.getItem_name().contains("ABC")){

            for (int q = 0; q < Integer.parseInt(set); q++) {

                for(int k=0;k<exercise_size;k++) {
                    exerciseroutine.add(exercise.get(k));
                    timeroutine.add(exercisetime);
                    setroutine.add(String.valueOf(q+1)+"/"+set);
                    exerciseroutine.add("rest");
                    timeroutine.add(resttime);
                    setroutine.add(String.valueOf(q+1)+"/"+set);
                }
                timeroutine.add(settime);
                exerciseroutine.add("set breaktime");
                setroutine.add(String.valueOf(q+1)+"/"+set);
            }
        }else{
            for(int k=0;k<exercise_size;k++) {

                for (int q = 0; q < Integer.parseInt(set); q++) {
                    exerciseroutine.add(exercise.get(k));
                    timeroutine.add(exercisetime);
                    setroutine.add(String.valueOf(k+1)+"/"+String.valueOf(exercise_size));
                    exerciseroutine.add("rest");
                    timeroutine.add(resttime);
                    setroutine.add(String.valueOf(k+1)+"/"+String.valueOf(exercise_size));
                }
                timeroutine.add(settime);
                exerciseroutine.add("set breaktime");
                setroutine.add(String.valueOf(k+1)+"/"+String.valueOf(exercise_size));
            }
        }

        jj=0;
        clock(Integer.parseInt(timeroutine.get(0)),exerciseroutine.get(0),setroutine.get(0)); //타이머 시작

    }


    //clock이 finish하고 exerciseroutine이 다 안끝났으면 다음 clock 실행, 끝났다면 기록여부 묻기
    public interface MyEventListener {
        void onEvent();
    }
    MyEventListener listener = new MyEventListener() { //clock이 onfinish 할떄마다 이벤트 받아서 재시작하기
        @Override
        public void onEvent() {
            soundPool.play(soundID,1f,1f,0,0,1f);
            if(jj<timeroutine.size()){ //남은 운동 있으면 타이머 재시작
                clock(Integer.parseInt(timeroutine.get(jj)),exerciseroutine.get(jj),setroutine.get(jj));
            }
            else{ //끝났다면, 운동 기록을 할지 dialog띄워서 확인받기

                AlertDialog.Builder builder=new AlertDialog.Builder((MainActivity.mContext));
                builder.setTitle("오늘의 운동을 기록하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tabataname=mainData.getTabataname();
                        String namecontent="";
                        ArrayList<String> exercises=mainData.getIt_strList();
                        for(int j=0;j<exercises.size();j++){
                            if(j!=exercises.size()-1){
                                namecontent=namecontent+exercises.get(j)+" & ";
                            }else{
                                namecontent=namecontent+exercises.get(j);
                            }
                        }

                        String todayexerciserecord= tabataname+"="+namecontent;//+infocontent;

                        ((MainActivity)MainActivity.mContext).saverecord(todayexerciserecord);
                        Toast.makeText(MainActivity.mContext, "저장되었습니다.", Toast.LENGTH_SHORT).show(); //삭제표시

                    }
                });
                builder.setNegativeButton("아니요",null);
                builder.create().show();

            }
        }
    };



    //타이머 실행
    private int clock(int time, String current,String currentset) {
        //오류잡기
            try {
                countDownTimer.cancel();
            } catch (Exception e) {
            }

            progress = 1;
            endTime = time; // up to finish time

            //화면에 현재운동과 세트상황 띄우기
            current_workout.setText(current);
            current_set.setText(currentset);


            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    tv_time.setText(String.valueOf(seconds));
                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime);
                    jj=jj+1;
                    listener.onEvent();
                }
            };
            countDownTimer.start();
            return 0;
    }

    //프로그레스바뷰 설정하기
    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);
    }
}