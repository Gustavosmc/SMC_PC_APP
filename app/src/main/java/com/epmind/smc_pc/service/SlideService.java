package com.epmind.smc_pc.service;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.epmind.smc_pc.activity.MainActivity;
import com.epmind.smc_pc.connection.ConnectSocket;

/**
 * Created by gustavosmc on 21/12/15.
 */
public class SlideService extends IntentService {
    private boolean running;
    private ConnectSocket connectSocket;


    public SlideService() {
        super(SlideService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        this.running = true;
        Log.i("smc","SlideService Iniciado");


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        running = false;
        Log.i("smc","SlideService Finalizado");

    }





}
