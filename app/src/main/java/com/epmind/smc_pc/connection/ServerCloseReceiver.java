package com.epmind.smc_pc.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

import com.epmind.smc_pc.activity.MainActivity;

import java.io.IOException;

/**
 * Created by gustavosmc on 21/12/15.
 */
public class ServerCloseReceiver extends BroadcastReceiver{
    public static String ACTION_CLOSE_SERVER = "CLOSE_SERVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            ConnectSocket.getSocket().close();
            ConnectSocket.getInput().close();
            ConnectSocket.getOutput().close();
            ConnectSocket.setConnected(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);




    }
}
