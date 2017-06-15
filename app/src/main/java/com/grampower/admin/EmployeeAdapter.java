package com.grampower.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by samdroid on 29/5/17.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.viewHolder> {
      Context context;

    private RCVItemClcikListener rcvItemClcikListener;

     ArrayList<EmployeeWrapper> listOfEmployee;
    public EmployeeAdapter(Context con,ArrayList<EmployeeWrapper> list) {
       this.listOfEmployee=list;
        this.context=con;
    }

    void setRCVItemClickListener(RCVItemClcikListener rcv){
    this.rcvItemClcikListener=rcv;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
   View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_adapter_view,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
       final  EmployeeWrapper employee=listOfEmployee.get(position);
        List<String> Actions = new ArrayList<String>();
        Actions.add("Actions");
        Actions.add("Assign Task");
        Actions.add("Attendance Check");
        Actions.add("Reports Check");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Actions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         String nameEmployee=employee.getName();
          String emailEmployee=employee.getEmail();
         holder.mNameView.setText(nameEmployee);
        Picasso.with(context).load(employee.getProfileUrl()).into(holder.mProfile);
         holder.mActions.setAdapter(dataAdapter);
         holder.mActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 String item = parent.getItemAtPosition(position).toString();
                 if(item.equals("Assign Task")){

                     Intent intent=new Intent(context,AssignTask.class);
                     intent.putExtra("email",employee.getEmail());
                     intent.putExtra("name",employee.getName());
                     intent.putExtra("profileUrl",employee.getProfileUrl());
                     context.startActivity(intent);
                 }
                 if(item.equals("Attendance Check")){

                     Intent intentAttendance= new Intent(context,CalenderDatePicker.class);
                     intentAttendance.putExtra("intentFrom","Attendance");
                     intentAttendance.putExtra("email",employee.getEmail());
                     intentAttendance.putExtra("name",employee.getName());
                     intentAttendance.putExtra("profileUrl",employee.getProfileUrl());
                     context.startActivity(intentAttendance);
                 }
                 if(item.equals("Reports Check")){

                     Intent intentReport= new Intent(context,CalenderDatePicker.class);
                     intentReport.putExtra("intentFrom","Report");
                     intentReport.putExtra("email",employee.getEmail());
                     intentReport.putExtra("name",employee.getName());
                     intentReport.putExtra("profileUrl",employee.getProfileUrl());
                     context.startActivity(intentReport);
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
    }

    @Override
    public int getItemCount() {
        return listOfEmployee.size();
    }

    class viewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener{
        TextView mNameView;
        CircleImageView mProfile;
         Spinner mActions;
        public viewHolder(View itemView) {
            super(itemView);
            mProfile=(CircleImageView)itemView.findViewById(R.id.profile_employee_listing);
            mNameView=(TextView)itemView.findViewById(R.id.nameView);
            mActions=(Spinner)itemView.findViewById(R.id.actionSpinner);


        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(rcvItemClcikListener!=null){
                rcvItemClcikListener.OnItemSelected(parent,view,getAdapterPosition(),position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
