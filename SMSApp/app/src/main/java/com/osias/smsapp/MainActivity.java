package com.osias.smsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SmsManager smsManager = null;
    EditText destEditText = null;
    EditText messageEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(smsManager == null)
             smsManager = SmsManager.getDefault();

        destEditText = findViewById(R.id.destEditText);
        messageEditText = findViewById(R.id.messageEditText);
    }

    public void SendSMSButton_OnClick(View view){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            String phoneNo = "", message = "";

            phoneNo = destEditText.getText().toString();
            message = messageEditText.getText().toString();

            if (phoneNo == null || phoneNo.isEmpty())
                return;
            if (message == null || message.isEmpty())
                return;
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }
    }
}
