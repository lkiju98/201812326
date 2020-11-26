package kr.ac.konkuk.tab;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class Fragment3 extends Fragment {

    MaterialCalendarView calendarView;

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootview3 = (ViewGroup)inflater.inflate (R.layout.fragment_3,container,false); //viewpager에서 fragment3을 연동

        calendarView=rootview3.findViewById(R.id.calendarView);
        calendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator()); //토요일과 일요일 색깔 바꿔주는 decorator연동

        //날짜클릭리스너- 해당 날자의 record를 internal file에서 읽어온다.
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                TextView textView=rootview3.findViewById(R.id.textView);
                textView.setText("이날의 운동기록:"); //이날의 운동기록 보여주기
                textView.setMovementMethod(new ScrollingMovementMethod()); //칸이 좁아서 기록이 다 안보일수 있으니, 스크롤 달아주기

                ((MainActivity)MainActivity.mContext).callrecord(date.toString()); //callrecord 함수로 internal file에서 운동기록 읽어오기
                System.out.println(date.toString());
            }
        });
        return rootview3;
    }
}