package com.grampower.admin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetTaskReports extends AppCompatActivity {

    int taskListSize;
    int currentTask;

    Context context;
    TextView mTaskTurn;
   FragmentManager FragManager;
    RelativeLayout mSuccessorLayout;
   List<Task> listOfTodayTask;
    CircleImageView mCircleImageView;
    TextView mEmployeeName;
    ImageButton mPredecesoor,mSuccessor,mBack;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_task_reports);
        context=this;
        flag=1;
         Intent intent=getIntent();
         String email=intent.getExtras().getString("email");
         String name=intent.getExtras().getString("name");
         String date=intent.getExtras().getString("date");
         String profileUrl=intent.getExtras().getString("profileUrl");
        mSuccessorLayout=(RelativeLayout)findViewById(R.id.successorLayout);
        mCircleImageView=(CircleImageView)findViewById(R.id.profile_task_report);
        mEmployeeName=(TextView)findViewById(R.id.employeeName);
        mPredecesoor=(ImageButton)findViewById(R.id.predecessor);
        mSuccessor=(ImageButton)findViewById(R.id.successor);
        mTaskTurn=(TextView)findViewById(R.id.taskTurn);
        mBack=(ImageButton)findViewById(R.id.backAssignTask);
        mEmployeeName.setText(name);
        Picasso.with(GetTaskReports.this).load(profileUrl).into(mCircleImageView);
        FragManager = getFragmentManager();

         listOfTodayTask=new ArrayList<>();

            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
            databaseReference.child(email).child("tasks").child(date).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String taskStatement = dataSnapshot.child("taskstatement").getValue().toString();
                        String taskId = dataSnapshot.child("taskId").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String detail = dataSnapshot.child("detail").getValue().toString();
                        Log.d("az", taskStatement + " " + status + " " + detail);
                        Task mTask = new Task(taskId, taskStatement, status, detail);
                        listOfTodayTask.add(mTask);

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


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        });

        DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("users");
        databaseRef.child(email).child("tasks").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    flag=0;
                    Log.d("az","flag is zero");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPredecesoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentTask=(currentTask-1+taskListSize)%taskListSize;
                Task firstTask=listOfTodayTask.get(currentTask);
                mTaskTurn.setText((currentTask+1)+"/"+taskListSize);
                FragmentTransaction transaction = FragManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("taskId",firstTask.getTaskId());
                bundle.putString("data","Yes");
                bundle.putString("status",firstTask.getStatus());
                bundle.putString("taskstatement",firstTask.getTaskstatement());
                bundle.putString("detail",firstTask.getDetail());
                taskFragment taskInfo = new taskFragment();
                taskInfo.setArguments(bundle);
                transaction.replace(R.id.taskInfoFragment, taskInfo);
                transaction.commit();
            }
        });

        mSuccessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentTask=(currentTask+1)%taskListSize;
                Task firstTask=listOfTodayTask.get(currentTask);
                mTaskTurn.setText((currentTask+1)+"/"+taskListSize);
                FragmentTransaction transaction = FragManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("taskId",firstTask.getTaskId());
                bundle.putString("data","Yes");
                bundle.putString("taskstatement",firstTask.getTaskstatement());
                bundle.putString("status",firstTask.getStatus());
                bundle.putString("detail",firstTask.getDetail());
                taskFragment taskInfo = new taskFragment();
                taskInfo.setArguments(bundle);
                transaction.replace(R.id.taskInfoFragment, taskInfo);
                transaction.commit();
            }
        });


        new waitForList().execute();

    }



class waitForList extends AsyncTask<Void,Void,Void> {

    ProgressDialog progressDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog =new ProgressDialog(GetTaskReports.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }
    @Override
    protected Void doInBackground(Void... params) {
        while (listOfTodayTask.size() == 0&&flag==1) ;
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        if(flag==0){
            FragmentTransaction transaction = FragManager.beginTransaction();
            mSuccessorLayout.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putString("data","No");
            taskFragment taskInfo = new taskFragment();
            taskInfo.setArguments(bundle);
            transaction.replace(R.id.taskInfoFragment, taskInfo);
            transaction.commit();

        }else {
            FragmentTransaction transaction = FragManager.beginTransaction();
            currentTask = 0;
            taskListSize = listOfTodayTask.size();
            Task firstTask = listOfTodayTask.get(0);
            mTaskTurn.setText((currentTask + 1) + "/" + taskListSize);
            Bundle bundle = new Bundle();
            bundle.putString("data","Yes");
            bundle.putString("taskId", firstTask.getTaskId());
            bundle.putString("status", firstTask.getStatus());
            bundle.putString("taskstatement", firstTask.getTaskstatement());
            bundle.putString("detail", firstTask.getDetail());
            taskFragment taskInfo = new taskFragment();
            taskInfo.setArguments(bundle);
            transaction.replace(R.id.taskInfoFragment, taskInfo);
            transaction.commit();
        }

    }

}


}
