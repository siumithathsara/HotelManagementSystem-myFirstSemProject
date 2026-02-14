package lk.ijse.hotelmanagementsystem_ijse.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private AnchorPane rootPane;

    private Timeline timeline;
    private double progress = 0;

    @FXML
    public void initialize() {

        timeline = new Timeline(
                new KeyFrame(Duration.millis(50), e -> {
                    progress += 0.01;
                    progressBar.setProgress(progress);

                    if (progress >= 1.0) {
                        timeline.stop();
                        loadLogin();
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadLogin() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/login.fxml")
            );

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
