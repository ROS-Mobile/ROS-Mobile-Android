package com.schneewittchen.rosandroid.utility;

import android.os.AsyncTask;


/**
 * TODO: Description
 * TODO: Edit to return values of called functions
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class LambdaTask extends AsyncTask<Void, Void, Void> {

    TaskRunnable taskRunnable;


    public LambdaTask(TaskRunnable taskRunnable){
        this.taskRunnable = taskRunnable;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        taskRunnable.run();
        return null;
    }

    public interface TaskRunnable {
        void run();
    }
}
