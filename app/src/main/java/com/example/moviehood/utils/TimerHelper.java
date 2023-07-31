package com.example.moviehood.utils;

public class TimerHelper {
    public interface TimerListener {
        void onTimerFinished();
    }
    public static void startTimer(int durationInMillis, final TimerListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(durationInMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    listener.onTimerFinished();
                }
            }
        }).start();
    }
}
