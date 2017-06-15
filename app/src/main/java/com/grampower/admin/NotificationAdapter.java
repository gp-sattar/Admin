package com.grampower.admin;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samdroid on 12/6/17.
 */

public class NotificationAdapter  extends  RecyclerView.Adapter<NotificationAdapter.viewHolder> {

     Context context;
    static   List<NotificationWrapper> listOfNotification;
    private static ActionMode mActionMode;
    Activity myActivity;

    public NotificationAdapter(Context con, ArrayList<NotificationWrapper> list, Activity activity) {
        listOfNotification=list;
        context=con;
        myActivity=activity;

    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.notification_adapter_view,parent,false);
        return new viewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        NotificationWrapper notification=listOfNotification.get(position);
        holder.mCheck.setText(notification.getNotification());
       // holder.mCheck.setSelected(notification.getSelected());
        holder.mCheck.setTag(notification);
        holder.mCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                NotificationWrapper notificate = (NotificationWrapper) cb.getTag();
                notificate.setSelected(cb.isChecked());
                listOfNotification.get(position).setSelected(cb.isChecked());
                boolean hasCheckedItems = getSelectedCount() > 0;
                if (hasCheckedItems && mActionMode == null)

                  mActionMode= myActivity.startActionMode(new ToolbarActionCallback(context));

                else if (!hasCheckedItems && mActionMode != null)
                    mActionMode.finish();

                if (mActionMode != null)
                    mActionMode.setTitle(String.valueOf(getSelectedCount()) + " selected");
            }
        });
    }

    public static  void setNullToActionMode(){
        if (mActionMode != null) {
            mActionMode = null;
        }
    }


    public  static void deleteRows() {

        for(int i=0;i<listOfNotification.size();i++){
            if(listOfNotification.get(i).getSelected()){

                Log.d("az",listOfNotification.get(i).getKey());

                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Notifications");
                databaseReference.child(listOfNotification.get(i).getKey()).setValue(null);
            }
        }


    }

    int  getSelectedCount(){
        int count=0;

       for(int i=0;i<listOfNotification.size();i++){
            if(listOfNotification.get(i).getSelected()){
                count++;
            }
        }

        return count;
    }

    @Override
    public int getItemCount() {
        return listOfNotification.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheck;
        public viewHolder(View itemView) {
            super(itemView);
            mCheck=(CheckBox)itemView.findViewById(R.id.checkBox);
        }
    }
}
