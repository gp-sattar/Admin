package com.grampower.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.grampower.admin.R.id.marquee;

public class MainActivity extends AppCompatActivity {

    List<NotificationWrapper> listOfNotification;
    CardView mAssignTask,mAttendance;
    WebView mWebView;
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView=(WebView) findViewById(marquee);
        final LinearLayout mEditlayout=(LinearLayout)findViewById(R.id.editlayout);
        mAssignTask=(CardView)findViewById(R.id.assignTask);
        mAttendance=(CardView)findViewById(R.id.attendance);
        ImageButton mUpdate=(ImageButton)findViewById(R.id.update);
        ImageButton mAddLine=(ImageButton)findViewById(R.id.addLine);
        ImageButton mDelete=(ImageButton)findViewById(R.id.delete);
        mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayoutAdmin);
        final TextInputEditText mNewLine=(TextInputEditText)findViewById(R.id.newLine);

        listOfNotification=new ArrayList<>();

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditlayout.getVisibility()==View.VISIBLE){
                    mEditlayout.setVisibility(View.GONE);
                }else{
                    mEditlayout.setVisibility(View.VISIBLE);
                }

            }
        });

        if(isNetworkAvailable()){
            new waitForList().execute();
        }else{
           /* Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Please Connect to Network", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();*/
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("No Internet Connection!").show();
        }
        mAddLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newline= mNewLine.getText().toString().trim();
                if(newline.length()>0&&!newline.equals("")){
                    addNewline(newline);

                }else{
                    Toast.makeText(MainActivity.this, "Enter valid notification", Toast.LENGTH_SHORT).show();
                }
                mEditlayout.setVisibility(View.GONE);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DeleteNotification.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list",(ArrayList<NotificationWrapper>)listOfNotification);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mAssignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,employeeListing.class));
            }
        });

        mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TodayAttendance.class));
            }
        });
    }

    void addNewline(String newNotification){
        long date=System.currentTimeMillis();
        SimpleDateFormat timeStampFormat=new SimpleDateFormat("ddMMyy_HHmmss");
         String timeStamp=timeStampFormat.format(date);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Notifications");
        Map<String,Object> notify=new  HashMap<>();
        notify.put(timeStamp,newNotification);
        databaseReference.updateChildren(notify);
        Toast.makeText(MainActivity.this,"New Notification have sent to server!",Toast.LENGTH_SHORT).show();
        if(isNetworkAvailable()){
            new waitForList().execute();
        }
    }


    class waitForList extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading Notifications...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Notifications");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key=dataSnapshot.getKey();
                    String notifyLine=dataSnapshot.getValue().toString();
                    NotificationWrapper notificationWrapper=new NotificationWrapper(key,notifyLine,false);
                    listOfNotification.add(notificationWrapper);
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
        @Override
        protected Void doInBackground(Void... params) {
            while (listOfNotification.size() == 0) ;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(progressDialog!=null){
                progressDialog.dismiss();
            }

            String notifications="";
            for(int i=0;i<listOfNotification.size();i++){
                NotificationWrapper note=listOfNotification.get(i);
                notifications=notifications+"<marquee  style='margin-bottom:10px; 'behavior='scroll' direction='left' scrollamount=3><span style='font-size:100%;color:red;'>&starf;</span>"
                        + note.getNotification() + "</marquee>";
            }
            String summary = "<html><div style='border:3px solid #ffa500;padding-right: 10px'><table><tr><th style='background-color:#ffa500' ><div style='padding-left:-10px;writing-mode:tb-rl;-webkit-transform:rotate(-90deg); -moz-transform:rotate(90deg);-o-transform: rotate(90deg); -ms-transform:rotate(90deg);margin-bottom: -50px; transform: rotate(90deg); width:30px; white-space:nowrap;'><text gravity='center' style='padding-bottom:-20px' >Current </text></div></th><th style='padding-top:10px;padding-bottom:10px'><FONT color='#000000'>"+notifications+"</FONT></th></tr></table></div></html>";
            mWebView.loadData(summary, "text/html", "utf-8");

        }

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
