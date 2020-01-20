package com.a8_bi_tech.www.afterplanbeta00;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

public class Home extends AppCompatActivity {

    Button btn_state, btn_this, btn_backup, btn_password;
    EditText edit_this, edit_backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_state = findViewById(R.id.btn_activity_state);
        btn_this = findViewById(R.id.btn_thisNo);
        btn_backup = findViewById(R.id.btn_backupNo);
        btn_password = findViewById(R.id.btn_pass);

        edit_this = findViewById(R.id.edit_ThisNo);
        edit_backup = findViewById(R.id.edit_backupNo);

        getDataStored();
        getPermission();

        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state_on_off();
            }
        });

        SharedPreferences sharedPreferences = Home.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        btn_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(getString(R.string.App_This_PhoneNo), edit_this.getText().toString());
                editor.commit();
            }
        });

        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(getString(R.string.App_Backup_PhoneNo), edit_backup.getText().toString());
                editor.commit();
            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_password = edit_backup.getText().toString();
                if(str_password.length()<1)
                    str_password = "1234";
                editor.putString(getString(R.string.App_Password), str_password);
                editor.commit();
            }
        });
    }

    void getPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(Home.this, new String[]{READ_PHONE_STATE}, 1);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(Home.this, new String[]{RECEIVE_BOOT_COMPLETED}, 1);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(Home.this, new String[]{SEND_SMS}, 1);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(Home.this, new String[]{RECEIVE_SMS}, 1);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(Home.this, new String[]{READ_SMS}, 1);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(Home.this, new String[]{ACCESS_FINE_LOCATION}, 1);
        }
    }

    void getDataStored(){
        String thisNo, backupNo,appState;

        SharedPreferences sharedPreferences = Home.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        thisNo = sharedPreferences.getString(formattedNumber(getString(R.string.App_This_PhoneNo)), "000000");
        backupNo = sharedPreferences.getString(formattedNumber(getString(R.string.App_Backup_PhoneNo)), "000000");
        appState = sharedPreferences.getString(getString(R.string.App_Activity_State), "OFF");

        if(!thisNo.equals("000000")){
            edit_this.setText(thisNo);
        }

        if(!backupNo.equals("000000")){
            edit_backup.setText(thisNo);
        }

        if(!appState.equals("OFF")){
            btn_state.setText("State OFF");
        }
        else{
            btn_state.setText("State ON");
        }
    }

    private void state_on_off() {
        String state;
        SharedPreferences sharedPreferences = Home.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        state = sharedPreferences.getString(getString(R.string.App_Activity_State), "OFF");
        JobScheduler scheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        if(state.equals("OFF")){
            editor.putString(getString(R.string.App_Activity_State), "ON");

            ComponentName componentName = new ComponentName(this, CustomJob.class);
            JobInfo info = new JobInfo.Builder(123, componentName).setPeriodic(60*60*1000).build();

            if(scheduler.schedule(info) == JobScheduler.RESULT_SUCCESS)
                Toast.makeText(getApplicationContext(), "Service started successfully.", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), "Service did not start successfully.", Toast.LENGTH_SHORT).show();
                editor.putString(getString(R.string.App_Activity_State), "OFF");
            }
        }
        if(state.equals("ON")){
            editor.putString(getString(R.string.App_Activity_State), "OFF");
            scheduler.cancel(123);
            Toast.makeText(getApplicationContext(), "Service cancellation was successful", Toast.LENGTH_SHORT).show();
        }

    }

    String formattedNumber(String str) {
        char ch[] = str.toCharArray();
        boolean flag = false;
        for(int i = 0; i < ch.length;++i) {
            if( !( ( (int)ch[i]>47 && (int)ch[i]<58 ) || ch[i]=='+' ) ){
                flag = true;
            }
        }
        if(flag) {
            str = "000000";
            Toast.makeText(this, "Phone number can not contain any space or character except Numbers and '+'.", Toast.LENGTH_SHORT).show();
        }
        return str;
    }
}
