package com.epmind.smc_pc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.epmind.smc_pc.R;
import com.epmind.smc_pc.connection.Comands;
import com.epmind.smc_pc.connection.ConnectSocket;
import com.epmind.smc_pc.connection.ServerCloseReceiver;

public class SlideActivity extends Activity {
    ImageButton ibUp, ibDown, ibLeft, ibRight;
    ConnectSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        initComponents();
        initEvents();

    }




    private void initComponents(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ibUp = (ImageButton) findViewById(R.id.ib_up);
        ibDown = (ImageButton) findViewById(R.id.ib_down);
        ibLeft = (ImageButton) findViewById(R.id.ib_left);
        ibRight = (ImageButton) findViewById(R.id.ib_right);

        socket = (ConnectSocket) getIntent().getSerializableExtra(MainActivity.KEY_SOCKET);

    }

    private void initEvents(){
        ibUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.sendMessage(Comands.getClickKey("up"));
            }
        });

        ibDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.sendMessage(Comands.getClickKey("down"));

            }
        });

        ibRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.sendMessage(Comands.getClickKey("right"));
            }
        });

        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.sendMessage(Comands.getClickKey("left"));
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
