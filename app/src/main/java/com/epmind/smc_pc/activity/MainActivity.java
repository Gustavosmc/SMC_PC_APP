package com.epmind.smc_pc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.epmind.smc_pc.R;
import com.epmind.smc_pc.connection.ConnectSocket;
import com.epmind.smc_pc.connection.NoInformationException;
import com.epmind.smc_pc.connection.ServerInformation;
import com.epmind.smc_pc.controll.DealMessage;
import com.epmind.smc_pc.scan.ScanCode;


public class MainActivity extends Activity {
     public static final String KEY_SOCKET = "socket";

     private Menu menu;
     private MenuItem itConnect;
     private LinearLayout llSlide, llGame, llMouse, llMedia;
     private ConnectSocket connectSocket;
     private ServerInformation serverInformation;
     private ScanCode scanCode = new ScanCode(this);
     private DealMessage dealMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        initEvents();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(menu != null){
            if(this.connectSocket.isConnected())
                this.itConnect.setIcon(R.drawable.connect);
            else this.itConnect.setIcon(R.drawable.desconnect);
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        connectSocket.desconnect();
        dealMessage.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ScanCode.RESULT_READ_CODE) {
            if (data != null) {
                scanCode.setCode(data.getExtras().getString(ScanCode.FLAG_SCAN_RESULT));
                try {
                    serverInformation = new ServerInformation(scanCode.getCode());
                    connectSocket = new ConnectSocket(serverInformation.getIp(), serverInformation.getPort());
                    connect();
                } catch (NoInformationException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.invalid_information, Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void initComponents(){
        llSlide = (LinearLayout) findViewById(R.id.ll_slide);
        llGame = (LinearLayout) findViewById(R.id.ll_game);
        llMouse = (LinearLayout) findViewById(R.id.ll_mouse);
        llMedia = (LinearLayout) findViewById(R.id.ll_midia);

        connectSocket = new ConnectSocket("localhost", 7000);
        dealMessage = new DealMessage(this, connectSocket);
    }


    private void verifyConnection(Intent intent){
        boolean debug = true; // todo retirar condição
        if (connectSocket.isConnected() || debug ) {
            intent.putExtra(KEY_SOCKET, connectSocket);
            MainActivity.this.startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.msg_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private void initEvents(){

        dealMessage.start();

        llSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SlideActivity.class);
                verifyConnection(intent);
            }
        });


        llGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llMouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MouseActivity.class);
                verifyConnection(intent);
            }
        });

        llMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MediaActivity.class);
                verifyConnection(intent);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.itConnect = menu.findItem(R.id.it_connect);
        this.menu = menu;
        if(this.connectSocket.isConnected())
            this.itConnect.setIcon(R.drawable.connect);
        else this.itConnect.setIcon(R.drawable.desconnect);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Toast.makeText(this, "Funfou", Toast.LENGTH_LONG).show();
                return true;
            case R.id.it_connect:
                 connectAndDesconnect();
                 return  true;


        }

        return super.onOptionsItemSelected(item);
    }


    private void connect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectSocket.connect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(connectSocket.isConnected()) {
                            itConnect.setIcon(R.drawable.connect);
                            Toast.makeText(MainActivity.this, R.string.connection_sucess, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }


    private void desconnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectSocket.desconnect();
                dealMessage.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!connectSocket.isConnected()) {
                            itConnect.setIcon(R.drawable.desconnect);
                            Toast.makeText(MainActivity.this, R.string.conection_canceled, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }




    private void connectAndDesconnect(){
        if(connectSocket.isConnected()) {
            desconnect();
        }else scanCode.scan();


    }



}
