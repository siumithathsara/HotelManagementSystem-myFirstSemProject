package lk.ijse.hotelmanagementsystem_ijse.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lk.ijse.hotelmanagementsystem_ijse.HelloApplication;
import lk.ijse.hotelmanagementsystem_ijse.dto.Session;
import lk.ijse.hotelmanagementsystem_ijse.dto.UserDTO;
import lk.ijse.hotelmanagementsystem_ijse.model.UserModel;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {
    @FXML
    private AnchorPane mainContent;
    @FXML
    private AnchorPane customerContent;
    @FXML
    private AnchorPane mainUserView;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    private UserModel userModel = new UserModel();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("User view is loaded");

        loadDashBoard();
        updateDateTime();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void clickDashBoardNav() throws IOException {
        loadDashBoard();
    }
    private void loadDashBoard() throws IOException {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
    }
    @FXML
    private void clickCustomerNav() throws IOException {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/customer.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    @FXML
    private void clickEmployeeNav() throws IOException {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/employee.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    void clickBookingNav() {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/reservationPopUp.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void clickRoomDetailsNav() throws IOException {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/roomDetails.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clickReservationNav() throws IOException {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/reservation.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clickPaymentNav() throws IOException {
        try {
            mainContent.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/payment.fxml"));
            anchorPane.prefHeightProperty().bind(mainContent.heightProperty());
            anchorPane.prefWidthProperty().bind(mainContent.widthProperty());
            mainContent.getChildren().add(anchorPane);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void clickLogOutNav() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to logout?",
                ButtonType.YES, ButtonType.NO);

        alert.initStyle(StageStyle.UNDECORATED);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {

                UserDTO currentUser = Session.getCurrentUser();

                if (currentUser != null) {
                    try {
                        userModel.updateUserStatus(currentUser.getUser_id(), "Inactive");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Session.clear();
                }
                try {
                    mainUserView.getChildren().clear();
                    AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                    anchorPane.prefHeightProperty().bind(mainUserView.heightProperty());
                    anchorPane.prefWidthProperty().bind(mainUserView.widthProperty());
                    mainUserView.getChildren().add(anchorPane);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateDateTime() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        lblTime.setText(currentTime.format(timeFormatter));
        lblDate.setText(currentDate.format(dateFormatter));
    }
}
