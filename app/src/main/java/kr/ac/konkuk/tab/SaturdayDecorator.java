package kr.ac.konkuk.tab;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.Calendar;

public class SaturdayDecorator implements DayViewDecorator {

    private final Calendar calendar=Calendar.getInstance();

    public SaturdayDecorator(){
    }

    //토요일에 해당하는 날짜를 받아오기
    @Override
    public boolean shouldDecorate(CalendarDay day){
        day.copyTo(calendar);
        int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay==Calendar.SATURDAY;
    }

    //파란색으로 색깔 지정
    @Override
    public void decorate(DayViewFacade view){
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}
