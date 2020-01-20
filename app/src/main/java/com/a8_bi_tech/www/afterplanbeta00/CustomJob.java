package com.a8_bi_tech.www.afterplanbeta00;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;




public class CustomJob extends JobService {





    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(getApplicationContext(),"Task Started",Toast.LENGTH_SHORT).show();
        doBackgroundWork(params);
        return false;
    }





    public void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jobFinished(params, false);
            }
        }).start();
    }




    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(CustomJob.this,"Task Cancelled",Toast.LENGTH_LONG).show();
        return true;
    }
}
