package com.hdr.demo.javafx;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    private HBox timeConfigArea;

    @FXML
    private TextField workConfig;

    @FXML
    private TextField restConfig;

    @FXML
    private Button statusBtn;

    @FXML
    private Label promptArea;

    private boolean running = false;

    private ScheduledFuture<?> job;

    private Long workMins = 40L;

    private Long restMins = 3L;

    public void setWorkMins(Long workMins) {
        this.workMins = workMins;
    }

    public void setRestMins(Long restMins) {
        this.restMins = restMins;
    }

    @FXML
    protected void statusAction() {

        if (running) {
            job.cancel(true);
            running = false;
            statusBtn.setText("start");
            timeConfigArea.setVisible(true);
            promptArea.setText(StringUtils.EMPTY);
        } else {
            checkAndSetConfig(workConfig, restConfig);
            Runnable restJob = () -> {
                try {
                    Platform.runLater(() -> promptArea.setText("next work time: " + LocalDateTime.now().plusMinutes(restMins).format(DateTimeFormatter.ofPattern("HH:mm"))));
                    Runtime.getRuntime().exec(LockScreenCmd.getCommand());
                    TimeUnit.MINUTES.sleep(restMins);
                    Platform.runLater(() -> promptArea.setText("next rest time: " + LocalDateTime.now().plusMinutes(workMins).format(DateTimeFormatter.ofPattern("HH:mm"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            job = scheduler.scheduleWithFixedDelay(restJob, workMins, workMins, TimeUnit.MINUTES);
            running = true;
            statusBtn.setText("stop");
            timeConfigArea.setVisible(false);
            promptArea.setText("next rest time: " + LocalDateTime.now().plusMinutes(workMins).format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private void checkAndSetConfig(TextField workConfig, TextField restConfig) {
        Optional.ofNullable(workConfig)
                .map(TextInputControl::getText)
                .filter(StringUtils::isNumeric)
                .map(Long::parseLong)
                .filter(x -> x > 0)
                .ifPresent(this::setWorkMins);

        Optional.ofNullable(restConfig)
                .map(TextInputControl::getText)
                .filter(StringUtils::isNumeric)
                .map(Long::parseLong)
                .filter(x -> x > 0)
                .ifPresent(this::setRestMins);
    }
}