package com.grampower.admin;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by samdroid on 2/6/17.
 */

public class taskFragment extends Fragment {

    Context context;
    public taskFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
           this.context= (GetTaskReports) context;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   String taskStatement="Loading error",detail="";
         View view=null;
         Bundle args=getArguments();
            String dataAvailable=args.getString("data");
             if(dataAvailable.equals("Yes")) {
                 view = inflater.inflate(R.layout.task_fragment_view, container, false);
                 TextView statusView = (TextView) view.findViewById(R.id.frag_status);
                 TextView mStatement = (TextView) view.findViewById(R.id.taskStatement_fragment);
                 final TextView details = (TextView) view.findViewById(R.id.detail_fragment);
                 taskStatement = args.getString("taskstatement");
                 detail = args.getString("detail");
                 String taskId = args.getString("taskId");
                 String status = args.getString("status");
                 statusView.setText(status);
                 mStatement.setText(taskStatement);
                 details.setText(detail);
             }else{
                   view = inflater.inflate(R.layout.no_task_fragment_view, container, false);
             }

        return view;
    }


}
