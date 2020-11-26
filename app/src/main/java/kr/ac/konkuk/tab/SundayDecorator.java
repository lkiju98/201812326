package kr.ac.konkuk.tab;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.Calendar;

public class SundayDecorator implements DayViewDecorator {

    private final Calendar calendar=Calendar.getInstance();

    public SundayDecorator(){
    }

    //일요일에 해당하는 날짜를 받아오기
    @Override
    public boolean shouldDecorate(CalendarDay day){
        day.copyTo(calendar);
        int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay==Calendar.SUNDAY;
    }

    //빨간색으로 색깔 지정
    @Override
    public void decorate(DayViewFacade view){

        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}
