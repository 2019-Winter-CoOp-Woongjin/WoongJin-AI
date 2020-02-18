package edu.skku.woongjin_ai.Package_4_4_MyPage.MyRecord;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.UserInfo;


//중요! 사용자에 Data 없을시 튕김 20.02.05

public class MyRecordActivity extends AppCompatActivity  {

    public DatabaseReference mPostReference;
    Intent intent;
    String id;
    TextView userGrade, userSchool, userName, userCoin;
    Button goback;
    ImageButton goHome;
    UserInfo me;
    Button graph_attend, graph_made, graph_correct, graph_level, graph_like, graph_bombcnt;
    int total_week;
    LineChart lineChart;
    XAxis xAxis;
    YAxis left, right;

    int MAX_SIZE=100;
    int f1=0, f2=0, f3=0, f4=0, f5=0, f6=0;

    Intent intentGoHome;

    ArrayList<String> week_made, week_correct, week_level, week_like, week_attend, week_bombcnt;
    ArrayList<Entry> entries;
    ArrayList<String> dates;


    MaterialCalendarView materialCalendarView;
    ArrayList<String> attendedDatesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecord);

        intent=getIntent();
        id=intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();


        userName = (TextView) findViewById(R.id.userName1);
        userSchool = (TextView) findViewById(R.id.userSchool);
        userGrade = (TextView) findViewById(R.id.userGrade1);
        userCoin = (TextView) findViewById(R.id.userCoin);
        goHome=(ImageButton)findViewById(R.id.home);
        graph_attend=(Button)findViewById(R.id.graph_attend);
        graph_made=(Button)findViewById(R.id.graph_made);
        graph_correct=(Button)findViewById(R.id.graph_correct);
        graph_level=(Button)findViewById(R.id.graph_level);
        graph_like=(Button)findViewById(R.id.graph_like);
        graph_bombcnt=(Button)findViewById(R.id.graph_bombcnt);
        lineChart=(LineChart)findViewById(R.id.chart);
        goback=(Button)findViewById(R.id.goback);
        xAxis=lineChart.getXAxis();
        left=lineChart.getAxisLeft();
        right=lineChart.getAxisRight();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15);
        xAxis.setDrawGridLines(false);
        left.setDrawGridLines(false);
        left.setAxisMinimum(0);
        right.setDrawGridLines(false);
        right.setEnabled(false);


        materialCalendarView = (MaterialCalendarView) findViewById(R.id.attendCalendar);
        attendedDatesList = new ArrayList<String>();
        dates=new ArrayList<String>();


        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setDynamicHeightEnabled(true);

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OnDayDecorator(),
                new MakeBoldDecorator());

        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int weekNum = 0;
                attendedDatesList.clear();

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_week_list/").getChildren()) {
                    weekNum++;
                    for(DataSnapshot snapshot1 : snapshot.child("attend_list").getChildren()) {
                        String time = snapshot1.getValue().toString();
                        String[] date = time.split(" ");
                        attendedDatesList.add(date[0]);
                    }
                }

                new CheckAttendedDay(attendedDatesList).executeOnExecutor(Executors.newSingleThreadExecutor());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });


        entries=new ArrayList<Entry>();


        week_attend=new ArrayList<String>();
        week_made=new ArrayList<String>();
        week_correct=new ArrayList<String>();
        week_level=new ArrayList<String>();
        week_like=new ArrayList<String>();
        week_bombcnt=new ArrayList<String>();

        getFirebaseDatabaseWeekInfo();
        getFirebaseDatabaseUserInfo();



        goHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intentGoHome=new Intent(MyRecordActivity.this, MainActivity.class);
                intentGoHome.putExtra("id", id);
                intentGoHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentGoHome);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        graph_attend.setOnClickListener(new View.OnClickListener(){
            //그래프 내부 데이터 설정 - MP AndroidChart API 사용
            @Override
            public void onClick(View v) {
                graph_attend.setBackgroundColor(getResources().getColor(R.color.blue));
                f1=1;
                if(f2==1||f3==1||f4==1||f5==1||f6==1){
                    graph_made.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_correct.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_level.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_like.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_bombcnt.setBackgroundColor(getResources().getColor(R.color.lightred));
                    f1=f2=f3=f4=f5=f6=0;
                    f1=1;
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Integer.parseInt(week_attend.get(j))));
                }
                //
                xAxis=lineChart.getXAxis();
                xAxis.setGranularityEnabled(true);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(dates.size()>(int)value)
                            return dates.get((int)value);
                        else
                            return null;
                    }
                });
                left.setGranularityEnabled(true);
                left.resetAxisMaximum();
                left.setAxisMinimum(0);
                left.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return Integer.toString((int)value)+"회";
                    }
                });
                LineDataSet dataset = new LineDataSet(entries, "주간 출석일 수");
                dataset.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return Integer.toString((int)value);//return your text
                    }
                });
                dataset.setValueTextSize(18);
                dataset.setLineWidth(6);
                dataset.setCircleRadius(12);
                dataset.setCircleHoleRadius(4);
                //dataset.setCircleColorHole(Color.WHITE);
                //dataset.setColor(Color.BLUE);
                //dataset.setCircleColor(Color.BLUE);
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.setDescription(null);
                lineChart.setBackgroundColor(Color.rgb(255,245,238));
                lineChart.animateY(500);
            }
        });

        graph_made.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_made.setBackgroundColor(getResources().getColor(R.color.blue));
                f2=1;
                if(f1==1||f3==1||f4==1||f5==1||f6==1){
                    graph_attend.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_correct.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_level.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_like.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_bombcnt.setBackgroundColor(getResources().getColor(R.color.lightred));
                    f1=f2=f3=f4=f5=f6=0;
                    f2=1;
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_made.get(j))));
                }
                xAxis=lineChart.getXAxis();
                xAxis.setGranularityEnabled(true);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(dates.size()>(int)value)
                            return dates.get((int)value);
                        else
                            return null;
                    }
                });
                left.setGranularityEnabled(true);
                left.resetAxisMaximum();
                left.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return Integer.toString((int)value)+"개";
                    }
                });
                LineDataSet dataset = new LineDataSet(entries, "주간 만든 문제 수");
                dataset.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return Integer.toString((int)value);//return your text
                    }
                });
                dataset.setValueTextSize(18);
                dataset.setLineWidth(6);
                dataset.setCircleRadius(12);
                dataset.setCircleHoleRadius(4);
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.setDescription(null);
                lineChart.setBackgroundColor(Color.rgb(255,245,238));
                lineChart.animateY(500);
            }
        });

        graph_correct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_correct.setBackgroundColor(getResources().getColor(R.color.blue));
                f3=1;
                if(f2==1||f1==1||f4==1||f5==1||f6==1){
                    graph_made.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_attend.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_level.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_like.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_bombcnt.setBackgroundColor(getResources().getColor(R.color.lightred));
                    f1=f2=f3=f4=f5=f6=0;
                    f3=1;
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_correct.get(j))));
                }
                xAxis=lineChart.getXAxis();
                xAxis.setGranularityEnabled(true);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(dates.size()>(int)value)
                            return dates.get((int)value);
                        else
                            return null;
                    }
                });
                left.setGranularityEnabled(true);
                left.resetAxisMaximum();
                left.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return Integer.toString((int)value)+"개";
                    }
                });
                LineDataSet dataset = new LineDataSet(entries, "주간 맞춘 문제 수");
                dataset.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return Integer.toString((int)value);//return your text
                    }
                });
                dataset.setValueTextSize(18);
                dataset.setLineWidth(6);
                dataset.setCircleRadius(12);
                dataset.setCircleHoleRadius(4);
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.setDescription(null);
                lineChart.setBackgroundColor(Color.rgb(255,245,238));
                lineChart.animateY(500);
            }
        });

        graph_level.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_level.setBackgroundColor(getResources().getColor(R.color.blue));
                f4=1;
                if(f2==1||f3==1||f1==1||f5==1||f6==1){
                    graph_made.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_correct.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_attend.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_like.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_bombcnt.setBackgroundColor(getResources().getColor(R.color.lightred));
                    f1=f2=f3=f4=f5=f6=0;
                    f4=1;
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_level.get(j))));
                }
                xAxis=lineChart.getXAxis();
                xAxis.setGranularityEnabled(true);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(dates.size()>(int)value)
                            return dates.get((int)value);
                        else
                            return null;
                    }
                });
                left.setGranularityEnabled(true);
                left.setAxisMaximum(5);
                left.setGranularityEnabled(true);
                left.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return Integer.toString((int)value)+"레벨";
                    }
                });
                LineDataSet dataset = new LineDataSet(entries, "주간 평균 레벨");
                dataset.setValueTextSize(18);
                dataset.setLineWidth(6);
                dataset.setCircleRadius(12);
                dataset.setCircleHoleRadius(4);
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.setDescription(null);
                lineChart.setBackgroundColor(Color.rgb(255,245,238));
                lineChart.animateY(500);
            }
        });

        graph_like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_like.setBackgroundColor(getResources().getColor(R.color.blue));
                f5=1;
                if(f2==1||f3==1||f4==1||f1==1||f6==1){
                    graph_made.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_correct.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_level.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_attend.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_bombcnt.setBackgroundColor(getResources().getColor(R.color.lightred));
                    f1=f2=f3=f4=f5=f6=0;
                    f5=1;
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j,Float.parseFloat(week_like.get(j))));
                }
                xAxis=lineChart.getXAxis();
                xAxis.setGranularityEnabled(true);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(dates.size()>(int)value)
                            return dates.get((int)value);
                        else
                            return null;
                    }
                });
                left.setGranularityEnabled(true);
                left.resetAxisMaximum();
                left.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return Integer.toString((int)value)+"개";
                    }
                });
                LineDataSet dataset = new LineDataSet(entries, "주간 좋아요 수");
                dataset.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return Integer.toString((int)value);//return your text
                    }
                });
                dataset.setValueTextSize(18);
                dataset.setLineWidth(6);
                dataset.setCircleRadius(12);
                dataset.setCircleHoleRadius(4);
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.setDescription(null);
                lineChart.setBackgroundColor(Color.rgb(255,245,238));
                lineChart.animateY(500);
            }
        });

        graph_bombcnt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_bombcnt.setBackgroundColor(getResources().getColor(R.color.blue));
                f6=1;
                if(f2==1||f3==1||f4==1||f1==1||f5==1){
                    graph_made.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_correct.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_level.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_attend.setBackgroundColor(getResources().getColor(R.color.lightred));
                    graph_like.setBackgroundColor(getResources().getColor(R.color.lightred));
                    f1=f2=f3=f4=f5=f6=0;
                    f6=1;
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j,Float.parseFloat(week_bombcnt.get(j))));
                }
                xAxis=lineChart.getXAxis();
                xAxis.setGranularityEnabled(true);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(dates.size()>(int)value)
                            return dates.get((int)value);
                        else
                            return null;
                    }
                });
                left.setGranularityEnabled(true);
                left.resetAxisMaximum();
                left.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return Integer.toString((int)value)+"개";
                    }
                });
                LineDataSet dataset = new LineDataSet(entries, "해체한 폭탄 수");
                dataset.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return Integer.toString((int)value);//return your text
                    }
                });
                dataset.setValueTextSize(18);
                dataset.setLineWidth(6);
                dataset.setCircleRadius(12);
                dataset.setCircleHoleRadius(4);
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.setDescription(null);
                lineChart.setBackgroundColor(Color.rgb(255,245,238));
                lineChart.animateY(500);
            }
        });

    }

    private void getFirebaseDatabaseWeekInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            //배열에 미리 데이터 넣어놓기 (그래프 그릴때 씀)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                week_like.clear();
                week_level.clear();
                week_correct.clear();
                week_attend.clear();
                week_made.clear();
                week_bombcnt.clear();
                dates.clear();
                total_week=0;
                DataSnapshot snapshot=dataSnapshot.child("user_list").child(id).child("my_week_list");
                for(DataSnapshot snapshot1:snapshot.getChildren()){ //week껍데기
                    long attendCnt=snapshot1.child("attend_list").getChildrenCount();
                    week_attend.add(Long.toString(attendCnt));
                    week_made.add(snapshot1.child("made").getValue().toString());
                    week_correct.add(snapshot1.child("correct").getValue().toString());
                    week_level.add(snapshot1.child("level").getValue().toString());
                    week_like.add(snapshot1.child("like").getValue().toString());
                    week_bombcnt.add(snapshot1.child("solvebomb").getValue().toString());
                    total_week++;
                }
                for(int j=0; j<total_week ; j++){
                    dates.add(j+1+"주");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.child("user_list").getChildren()) {
                    if(snapshot.getKey().equals(id)) {
                        me = snapshot.getValue(UserInfo.class);
                        userName.setText(me.name+" 학생");
                        userGrade.setText(me.grade + "학년");
                        userCoin.setText(me.coin + " 코인");
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.addValueEventListener(postListener);
    }


    private class CheckAttendedDay extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> attendedDatesList;

        CheckAttendedDay(ArrayList<String> attendedDatesList) {
            this.attendedDatesList = attendedDatesList;
        }

        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            List<CalendarDay> dates = new ArrayList<>();

            for(String date : attendedDatesList) {
                CalendarDay calendarDay = CalendarDay.from(calendar);
                String[] time = date.split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);
                dates.add(calendarDay);
                calendar.set(year, month-1, day);
            }

            int size = attendedDatesList.size();
            String date = attendedDatesList.get(size-1);
            CalendarDay calendarDay = CalendarDay.from(calendar);
            String[] time = date.split("-");
            int year = Integer.parseInt(time[0]);
            int month = Integer.parseInt(time[1]);
            int day = Integer.parseInt(time[2]);
            dates.add(calendarDay);
            calendar.set(year, month-1, day);

            return dates;
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if(isFinishing()) return;

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, getApplicationContext()));
        }
    }

    private class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    private class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    private class OnDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public OnDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD_ITALIC));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }

    private class MakeBoldDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public MakeBoldDecorator() {}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DATE);
            return weekDay > 0 && weekDay < 32;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan((new StyleSpan(Typeface.BOLD)));
        }
    }
}

