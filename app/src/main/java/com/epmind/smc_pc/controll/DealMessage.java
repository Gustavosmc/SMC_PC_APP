package com.epmind.smc_pc.controll;

import android.content.Context;
import android.content.Intent;

import com.epmind.smc_pc.activity.MainActivity;
import com.epmind.smc_pc.connection.ConnectSocket;
import com.epmind.smc_pc.connection.ServerCloseReceiver;

/**
 * Created by gustavosmc on 25/12/15.
 */
public class DealMessage extends Thread {
    private  Context context;
    private  ConnectSocket connectSocket;
    private boolean running;


    public DealMessage(Context context, ConnectSocket connectSocket){
        this.context = context;
        this.connectSocket = connectSocket;
    }

    private void server_closed(){
        Intent intent = new Intent(ServerCloseReceiver.ACTION_CLOSE_SERVER);
        intent.putExtra(MainActivity.KEY_SOCKET, connectSocket);
        this.context.sendBroadcast(intent);
    }


    public void run(){
        running = true;
        while (running){
            if(connectSocket.getSizeMessages() > 0){
                String msg = connectSocket.getFirstMessage();
                if(msg.equals("EXITIN"))
                    server_closed();
            }
        }
    }

    public void close(){
        this.running = false;
    }




}
