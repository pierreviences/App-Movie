package com.example.moviehood.utils;

public class TimerHelper {
    public interface TimerListener {
        void onTimerFinished();
    }

    public static void startTimer(int durationInMillis, final TimerListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(durationInMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            listener.onTimerFinished();
        }).start();
    }
}
