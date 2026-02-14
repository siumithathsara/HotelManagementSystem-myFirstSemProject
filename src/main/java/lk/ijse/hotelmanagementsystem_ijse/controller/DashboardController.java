package lk.ijse.hotelmanagementsystem_ijse.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import lk.ijse.hotelmanagementsystem_ijse.dto.RoomDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.tm.RoomTM;
import lk.ijse.hotelmanagementsystem_ijse.model.DashboardModel;

import java.net.URL;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label lblCustomer;
    @FXML
    private Label lblAvalible;
    @FXML
    private Label lblBooking;
    @FXML
    private Label lblRevenue;

    @FXML
    private TableView<RoomTM> employeeView;
    @FXML
    private TableColumn<RoomTM, String> colRoomId;
    @FXML
    private TableColumn<RoomTM, String> colRoomType;
    @FXML
    private TableColumn<RoomTM, Double> colPricePerRoom;
    @FXML
    private TableColumn<RoomTM, String> colStatus;

    @FXML
    private BarChart<String, Number> revenueBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPricePerRoom.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadCounts();
        loadRoomTable();
        loadWeeklyChart();
    }


    private void loadCounts() {
        try {
            lblCustomer.setText(String.valueOf(DashboardModel.getCustomerCount()));
            lblAvalible.setText(String.valueOf(DashboardModel.getAvailableRoomCount()));
            lblBooking.setText(String.valueOf(DashboardModel.getTodayBookingCount()));
            lblRevenue.setText(String.format("%.2f", DashboardModel.getTodayRevenue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadRoomTable() {
        ObservableList<RoomTM> list = FXCollections.observableArrayList();

        try {
            List<RoomDetailsDTO> rooms = DashboardModel.getRoomDetails();

            for (RoomDetailsDTO dto : rooms) {
                list.add(new RoomTM(dto.getRoomId(), dto.getRoomType(), dto.getPricePerRoom(), dto.getStatus()));
            }

            employeeView.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadWeeklyChart() {
        revenueBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weekly Bookings");

        try {
            ResultSet rs = DashboardModel.getWeeklyBookings();

            while (rs.next()) {
                String day = rs.getString("day");
                int count = rs.getInt("count");

                XYChart.Data<String, Number> data = new XYChart.Data<>(day, 0);

                series.getData().add(data);

                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(data.YValueProperty(), count)));
                        timeline.play();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        revenueBarChart.getData().add(series);
    }
}
