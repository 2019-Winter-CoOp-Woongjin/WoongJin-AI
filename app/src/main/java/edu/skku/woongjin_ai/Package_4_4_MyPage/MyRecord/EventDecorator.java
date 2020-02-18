package edu.skku.woongjin_ai.Package_4_4_MyPage.MyRecord;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

import edu.skku.woongjin_ai.R;

class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> days, Context context) {
        drawable = ContextCompat.getDrawable(context, R.drawable.red_o);
        this.color = color;
        this.dates = new HashSet<>(days);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
