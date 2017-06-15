package com.grampower.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalenderDatePicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_date_picker);

         Intent intent=getIntent();
         final String targetClass =intent.getExtras().getString("intentFrom");
       final  String email=intent.getExtras().getString("email");
        final String name=intent.getExtras().getString("name");
        final String profileUrl=intent.getExtras().getString("profileUrl");

         OneCalendarView calendarView = (OneCalendarView) findViewById(R.id.oneCalendar);
        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {
                //hacer algo aqui
            }

            @Override
            public void nextMonth() {
                //hacer algo aqui
            }
        });

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {

            @Override
            public void dateOnClick(Day day, int position) {

                Date date=day.getDate();
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd_MM_yy");
                  String dateText=dateFormat.format(date);

               Log.d("az",dateText);

                if(targetClass.equals("Report")){
                    Intent reportIntent=new Intent(CalenderDatePicker.this,GetTaskReports.class);
                          reportIntent.putExtra("email",email);
                          reportIntent.putExtra("name",name);
                          reportIntent.putExtra("profileUrl",profileUrl);
                          reportIntent.putExtra("date",dateText);
                          startActivity(reportIntent);
                }else{
                    Intent reportIntent=new Intent(CalenderDatePicker.this,EmployeeAttendanceCheck.class);
                    reportIntent.putExtra("email",email);
                    reportIntent.putExtra("name",name);
                    reportIntent.putExtra("profileUrl",profileUrl);
                    reportIntent.putExtra("date",dateText);
                    startActivity(reportIntent);
                }
            }


            @Override
            public void dateOnLongClick(Day day, int position) {
                //
            }
        });

    }
}
