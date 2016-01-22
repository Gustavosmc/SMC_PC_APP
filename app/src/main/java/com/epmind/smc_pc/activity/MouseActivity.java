package com.epmind.smc_pc.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.epmind.smc_pc.R;
import com.epmind.smc_pc.connection.Comands;
import com.epmind.smc_pc.connection.ConnectSocket;

public class MouseActivity extends Activity implements SensorEventListener {
    private LinearLayout llMouse;
    Button btLeft, btRight, btFisicalLeft, btFisicalRight;
    private ConnectSocket connectSocket;
    Sensor sensor;
    SensorManager sensorManager;


    private boolean modeDirection = false;
    private int x;
    private int y;
    private long lastTime = 0;
    private long time = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        initComponents();
        initEvents();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finalizeFisicalMouse();
    }

    private void initComponents(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        llMouse = (LinearLayout) findViewById(R.id.ll_touch);
        connectSocket = (ConnectSocket) getIntent().getSerializableExtra(MainActivity.KEY_SOCKET);
        btLeft = (Button) findViewById(R.id.bt_left);
        btRight = (Button) findViewById(R.id.bt_right);

    }



    private void initEvents(){

       btLeft.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               connectSocket.sendMessage(Comands.getLongClickMouse("left"));
               return false;
           }
       });

        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSocket.sendMessage(Comands.getClickMouse("left"));
            }
        });

        btRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSocket.sendMessage(Comands.getClickMouse("right"));
            }
        });

        llMouse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int auxX;
                int auxY;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = Math.round(event.getX());
                        y = Math.round(event.getY());
                        lastTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(lastTime + time > System.currentTimeMillis())
                            connectSocket.sendMessage(Comands.getClickMouse("left"));

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int fatMult =  event.getHistorySize();
                        auxX = Math.round(event.getX()) - x;
                        auxY = Math.round(event.getY()) - y;
                        x = Math.round(event.getX());
                        y = Math.round(event.getY());
                        connectSocket.sendMessage(Comands.getMoveMouse(auxX * fatMult, auxY * fatMult));
                        break;
                }
                return true;
            }
        });

    }


    private void initFisicalMouse(){
        btFisicalRight = (Button) findViewById(R.id.bt_right_f);
        btFisicalLeft = (Button) findViewById(R.id.bt_left_f);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(MouseActivity.this, sensor, SensorManager.SENSOR_DELAY_GAME);

        btFisicalRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSocket.sendMessage(Comands.getClickMouse("right"));

            }
        });

        btFisicalLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSocket.sendMessage(Comands.getClickMouse("left"));
            }
        });

        btFisicalLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                connectSocket.sendMessage(Comands.getLongClickMouse("left"));
                return false;
            }
        });

    }

    private void finalizeFisicalMouse(){
        try {
            this.sensorManager.unregisterListener(this);
        }catch (Exception e){
            // TODO tratar
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mouse, menu);

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        MenuItem itemMouse = menu.findItem(R.id.it_mouse);
        if(modeDirection){
            itemMouse.setTitle(R.string.mode_touchpad);
        }else{
            itemMouse.setTitle(R.string.mode_mouse_direction);
        }
        return super.onMenuOpened(featureId, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                break;
            case R.id.it_mouse:
                if(!modeDirection) {
                    setContentView(R.layout.mouse_fisical);
                    initFisicalMouse();
                    modeDirection = true;
                }else {
                    setContentView(R.layout.activity_mouse);
                    finalizeFisicalMouse();
                    initComponents();
                    initEvents();
                    modeDirection = false;

                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }


    int contStop;
    int INTERVAL = 3;
    int CONT = 2;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        int intX = ((int) (x * 100));
        int intY = ((int) (y * 100));
        int intZ = ((int) (z * 100));
        if((intX < INTERVAL && intX > -INTERVAL) && (intZ < INTERVAL && intZ > -INTERVAL)){
            contStop++;
            if(contStop > CONT) contStop = CONT;
        }else contStop = 0;


        if(contStop < CONT)
            connectSocket.sendMessage(Comands.getMoveMouse(intZ * -1, intX * -1));
        System.out.println(contStop + "  x = " + intX + " " + "y = " + intY + " z = " + intZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
