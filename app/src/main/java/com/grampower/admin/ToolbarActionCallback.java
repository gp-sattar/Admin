package com.grampower.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by samdroid on 13/6/17.
 */

public class ToolbarActionCallback implements ActionMode.Callback {

    Context context;
    public ToolbarActionCallback(Context con) {
        context=con;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.multi_select_delete, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.delete_notification), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.delete_notification).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_notification:
                NotificationAdapter.deleteRows();
                context.startActivity(new Intent(context,MainActivity.class));
             break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        NotificationAdapter.setNullToActionMode();
    }

}
