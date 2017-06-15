package com.grampower.admin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAttendanceCheck extends AppCompatActivity {

    LinearLayout mWholeDayAttendance;
    RelativeLayout mMorningAttendance,mEveningAttendance,mAttendanceScreen;
    TextView mStartTime,mStartLocation,mEndTime,mEndLocation,mEmployeeName;
    CircleImageView mMorningSelfie,mEveningSelfie,mProfile;
    ImageButton mBack;
    int flag,flag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_attendance_check);
         flag=1;
        flag2=1;
        Intent intent=getIntent();
        String email=intent.getExtras().getString("email");
        String name=intent.getExtras().getString("name");
        String date=intent.getExtras().getString("date");
        String profileUrl=intent.getExtras().getString("profileUrl");
       final Context context=this;
        mWholeDayAttendance=(LinearLayout)findViewById(R.id.wholeDayAttendance);
        mMorningAttendance=(RelativeLayout)findViewById(R.id.morningAttendance);
        mEveningAttendance=(RelativeLayout)findViewById(R.id.eveningAttendance);
        mAttendanceScreen=(RelativeLayout)findViewById(R.id.AttendanceScreen);

        mBack=(ImageButton)findViewById(R.id.backAttendanceCheck);
        mEmployeeName=(TextView)findViewById(R.id.employeeName_attendanceCheck);
        mStartTime=(TextView)findViewById(R.id.timeStartAttendance);
        mEndTime=(TextView)findViewById(R.id.timeEndAttendance);
        mStartLocation=(TextView)findViewById(R.id.locationStartAttendance);
        mEndLocation=(TextView)findViewById(R.id.locationStartAttendance);
        mMorningSelfie=(CircleImageView)findViewById(R.id.selfieStartAttendance);
        mEveningSelfie=(CircleImageView)findViewById(R.id.selfieEndAttendance);
        mProfile=(CircleImageView)findViewById(R.id.profile_attendance_check);
        Picasso.with(EmployeeAttendanceCheck.this).load(profileUrl).into(mProfile);
        mEmployeeName.setText(name);

        DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("users");
        databaseRef.child(email).child("tasks").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    flag=0;
                    mWholeDayAttendance.setVisibility(View.GONE);
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.notdatafound);
                    mAttendanceScreen.setBackground(drawable);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(flag==1){
            databaseRef.child(email).child("tasks").child(date).child("endAttendance").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        flag2=0;

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
       if(flag==1){
               databaseReference.child(email).child("tasks").child(date).addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       if(dataSnapshot.getKey().equals("startAttendance")) {
                           mStartTime.setText(dataSnapshot.child("currentTime").getValue().toString());
                           mStartLocation.setText(dataSnapshot.child("currentLocation").getValue().toString());
                           Picasso.with(context).load(dataSnapshot.child("selfieUrl").getValue().toString()).into(mMorningSelfie);
                       }
                       if(dataSnapshot.getKey().equals("endAttendance")){
                           mEndTime.setText(dataSnapshot.child("currentTime").getValue().toString());
                           mEndLocation.setText(dataSnapshot.child("currentLocation").getValue().toString());
                           Picasso.with(context).load(dataSnapshot.child("selfieUrl").getValue().toString()).into(mEveningSelfie);
                       }
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
       }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        });

    }
}
