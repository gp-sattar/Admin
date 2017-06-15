package com.grampower.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TodayAttendance extends AppCompatActivity {

   ImageButton mBack;
    RecyclerView mRecyclerView;
    ArrayList<EmployeeWrapper> listOfAttendEmployee;
    Context context;
    Boolean Status;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_attendance);
         context=this;
        mBack=(ImageButton)findViewById(R.id.back_today_attendance);
        mRecyclerView=(RecyclerView)findViewById(R.id.todayAttendance_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        long date=System.currentTimeMillis();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd_MM_yy");
         final String dateText=dateFormat.format(date);

        listOfAttendEmployee=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name=dataSnapshot.child("profile").child("name").getValue().toString();
                String profileUrl=dataSnapshot.child("profile").child("profileUrl").getValue().toString();
                String email=dataSnapshot.getKey();

                EmployeeWrapper employee;
                if(dataSnapshot.child("attendance").hasChild(dateText)){
                    if(dataSnapshot.child("attendance").child(dateText).hasChild("endAttendance")){
                         employee=new EmployeeWrapper(email,name,profileUrl,"P","P");
                    }else{
                        employee=new EmployeeWrapper(email,name,profileUrl,"P","A");
                    }
                }else{
                    employee=new EmployeeWrapper(email,name,profileUrl,"A","A");
                }
                listOfAttendEmployee.add(employee);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new waitForList().execute();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        });

    }


    void setAttendanceAdapter(ArrayList<EmployeeWrapper> list){
        TodayAttendanceAdapter adapter=new TodayAttendanceAdapter(context,list);
        mRecyclerView.setAdapter(adapter);
    }

    class waitForList extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(TodayAttendance.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (listOfAttendEmployee.size() == 0) ;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            setAttendanceAdapter(listOfAttendEmployee);
        }

    }



    @Override
    public void onPause(){
        super.onPause();
        if(progressDialog != null)
            progressDialog.dismiss();
    }


}
