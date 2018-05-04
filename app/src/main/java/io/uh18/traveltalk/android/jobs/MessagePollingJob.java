package io.uh18.traveltalk.android.jobs;

import android.os.Handler;

import timber.log.Timber;

import static io.uh18.traveltalk.android.ConstantsKt.JOB_MESSAGE_POLLING;

/**
 * Created by samuel.hoelzl on 04.05.18.
 */

public class MessagePollingJob {

    Handler handler = new Handler();
    Runnable job = new Runnable() {
        @Override
        public void run() {
            Timber.d("Message polling ...");
            //TODO Samuel.Hoelzl poll server

            handler.postDelayed(job, JOB_MESSAGE_POLLING); //wait to run again
        }
    };

    public void stopTest() {
        handler.removeCallbacks(job);
    }

    public void startTest() {
        handler.postDelayed(job,JOB_MESSAGE_POLLING); //wait 0 ms and run
    }

}
