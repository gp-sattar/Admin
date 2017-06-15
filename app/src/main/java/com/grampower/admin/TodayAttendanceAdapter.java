package com.grampower.admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by samdroid on 13/6/17.
 */

public class TodayAttendanceAdapter extends RecyclerView.Adapter<TodayAttendanceAdapter.viewHolder>{

  Context context;
    List<EmployeeWrapper> listOfEmployees;
    public TodayAttendanceAdapter(Context con, ArrayList<EmployeeWrapper> list) {
          this.context=con;
          this.listOfEmployees=list;
    }



    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.today_attendance_adapter_view,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
       EmployeeWrapper employee=listOfEmployees.get(position);
        Picasso.with(context).load(employee.getProfileUrl()).into(holder.mProfile);
        holder.mName.setText(employee.getName());
        holder.mEveningStatus.setText(employee.getEveningStatus());
        holder.mMorningStatus.setText(employee.getMorningStatus());
    }

    @Override
    public int getItemCount() {
        return listOfEmployees.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfile;
        TextView mName,mMorningStatus,mEveningStatus;
        public viewHolder(View itemView) {
            super(itemView);
            mProfile=(CircleImageView)itemView.findViewById(R.id.profile_today_attendance);
            mName=(TextView)itemView.findViewById(R.id.nameView_today_attendance);
            mMorningStatus=(TextView)itemView.findViewById(R.id.morningStatus);
            mEveningStatus=(TextView)itemView.findViewById(R.id.eveningStatus);
        }
    }
}
