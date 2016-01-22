package com.epmind.smc_pc.scan;

import android.app.Activity;
import android.content.Intent;

import com.epmind.smc_pc.R;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by gustavosmc on 16/12/15.
 */
public class ScanCode{
    public static final int RESULT_READ_CODE = 101;
    public static final String FLAG_SCAN_RESULT = "SCAN_RESULT";
    private final Activity activity;
    private Intent intent;
    private String code;

    public ScanCode(Activity activity) {
        this.activity = activity;
        intent = null;
    }

    public void scan(){
        if(activity == null) return;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(activity.getString(R.string.text_cam));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        Intent t = integrator.createScanIntent();
        activity.startActivityForResult(t, RESULT_READ_CODE);
    }


    public Intent getIntent() {
        return intent;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
