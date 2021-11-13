package com.hdr.demo.demojavafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Hourglass extends Application {

    public static ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Hourglass.class.getResource("hourglass.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("番茄闹钟");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdownNow();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    private Button statusBtn;

    private boolean running = false;

    private ScheduledFuture<?> job;

    private Long restMins = 3L;

    private Long workMins = 40L;

    @FXML
    protected void statusAction() {

        if (running) {
            job.cancel(true);
            running = false;
            statusBtn.setText("start");
        } else {
            Runnable restJob = () -> {
                try {
                    System.err.println("start reset " + LocalDateTime.now());
                    Runtime.getRuntime().exec("loginctl lock-session");
                    TimeUnit.MINUTES.sleep(restMins);
                    System.err.println("end reset " + LocalDateTime.now());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            job = scheduler.scheduleWithFixedDelay(restJob, workMins, workMins, TimeUnit.MINUTES);
            running = true;
            statusBtn.setText("stop");
        }

    }


}