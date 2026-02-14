package lk.ijse.hotelmanagementsystem_ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.hotelmanagementsystem_ijse.dto.BookingDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.BookingDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.CustomerDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.RoomDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.tm.RoomReservationTM;
import lk.ijse.hotelmanagementsystem_ijse.model.BookingDetailsModel;
import lk.ijse.hotelmanagementsystem_ijse.model.BookingModel;
import lk.ijse.hotelmanagementsystem_ijse.model.CustomerModel;
import lk.ijse.hotelmanagementsystem_ijse.model.RoomDetailsModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    @FXML
    private TextField checkOutField;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblRoomId;

    @FXML
    private Label lblRoomType;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private ComboBox<String> reservationBox;

    @FXML
    private AnchorPane loadPaymentToBooking;


    @FXML
    private TableColumn<RoomReservationTM, String> colBookingId;

    @FXML
    private TableColumn<RoomReservationTM, Date> colCheckIn;

    @FXML
    private TableColumn<RoomReservationTM, Date> colCheckOut;

    @FXML
    private TableColumn<RoomReservationTM, String> colRoomId;

    @FXML
    private TableColumn<RoomReservationTM, Double> colPrice;

    @FXML
    private TableColumn<RoomReservationTM, String> colCustomerName;

    @FXML
    private TableColumn<RoomReservationTM, String> colRoomType;


    @FXML
    private TableColumn<RoomReservationTM, Double> colTotalAmount;



    @FXML
    private TableView<RoomReservationTM> reservationView;


    private CustomerModel customerModel = new CustomerModel();
    private RoomDetailsModel roomDetailsModel = new RoomDetailsModel();
    private BookingModel bookingModel = new BookingModel();
    private BookingDetailsModel bookingDetailsModel = new BookingDetailsModel();
    private ObservableList<RoomReservationTM> roomReservationObList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        loadBookingIds();

    }
    private void loadBookingIds() {

        try {

            List<BookingDTO> bookingDTOList = bookingModel.getAllBookings();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (BookingDTO bookingDTO : bookingDTOList) {
                obList.add(String.valueOf(bookingDTO.getBooking_id()));
            }

            reservationBox.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong");
        }



    }
    @FXML
    private void handleSelectBookingIds(ActionEvent event) {

        try {
            String bookingId = reservationBox.getValue();
            if (bookingId == null) return;

            BookingDTO booking = bookingModel.searchBooking(bookingId);
            BookingDetailsDTO details = bookingDetailsModel.getBookingDetails(bookingId);
            CustomerDTO customer = customerModel.searchCustomer(booking.getCustomer_id());
            RoomDetailsDTO room = roomDetailsModel.searchRoom(details.getRoomId());

            lblDate.setText(String.valueOf(booking.getBooking_date()));
            lblCustomerId.setText(customer.getCustomer_id());
            lblCustomerName.setText(customer.getName());
            lblRoomId.setText(room.getRoomId());
            lblRoomType.setText(room.getRoomType());
            lblPrice.setText(String.valueOf(room.getPricePerRoom()));

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load booking details").show();
        }
    }
    @FXML
    private void handleAddToCart(ActionEvent event) {

        String bookingId = reservationBox.getSelectionModel().getSelectedItem();
        String customerName = lblCustomerName.getText();
        String roomId = lblRoomId.getText();
        String roomType = lblRoomType.getText();
        String priceText = lblPrice.getText();
        String checkInText = lblDate.getText();
        String checkOutText = checkOutField.getText();
        String totalAmountText = lblTotalAmount.getText();

        if (bookingId == null) {
            new Alert(Alert.AlertType.ERROR, "Select Booking ID").show();
            return;
        }

        if (checkOutText == null || checkOutText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Enter Check-out Date").show();
            return;
        }

        if (isDuplicate(bookingId, roomId)) {
            new Alert(Alert.AlertType.ERROR,
                    "This booking already added to table").show();
            return;
        }


        try {

            LocalDate checkInLocal = LocalDate.parse(checkInText);
            LocalDate checkOutLocal = LocalDate.parse(checkOutText);

            if (checkOutLocal.isBefore(checkInLocal)) {
                new Alert(Alert.AlertType.ERROR, "Check-out date is invalid").show();
                return;
            }


            long nights = ChronoUnit.DAYS.between(checkInLocal, checkOutLocal);
            if (nights == 0) nights = 1;

            double pricePerNight = Double.parseDouble(priceText);
            double totalPrice = nights * pricePerNight;



            Date checkInDate = java.sql.Date.valueOf(checkInLocal);
            Date checkOutDate = java.sql.Date.valueOf(checkOutLocal);

            RoomReservationTM tm = new RoomReservationTM(
                    bookingId,
                    customerName,
                    roomId,
                    roomType,
                    pricePerNight,
                    checkInDate,
                    checkOutDate,
                    totalPrice
            );

            roomReservationObList.add(tm);
            reservationView.setItems(roomReservationObList);
            reservationView.refresh();

            updateGrandTotal();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Invalid data").show();
        }
    }
    private void updateGrandTotal() {
        double total = 0;

        for (RoomReservationTM tm : roomReservationObList) {
            total += tm.getTotalPrice();
        }

        lblTotalAmount.setText(String.format("%.2f", total));
    }

    @FXML
    private void handleRemoveItem(ActionEvent event) {

        RoomReservationTM selected =
                reservationView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.ERROR, "Select a row to remove").show();
            return;
        }

        roomReservationObList.remove(selected);
        reservationView.refresh();

        clearFields();
        updateGrandTotal();
    }
    private boolean isDuplicate(String bookingId, String roomId) {
        for (RoomReservationTM tm : roomReservationObList) {
            if (tm.getBookingId().equals(bookingId)
                    && tm.getRoomId().equals(roomId)) {
                return true;
            }
        }
        return false;
    }

    private void clearFields() {
        lblCustomerName.setText("");
        lblRoomId.setText("");
        lblRoomType.setText("");
        lblPrice.setText("");
        lblDate.setText("");
        lblCustomerId.setText("");
        reservationBox.getItems().clear();
        checkOutField.setText("");

    }

    @FXML
    private void handlePlaceBooking(ActionEvent event) {
        if (roomReservationObList.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "No bookings added to place!").show();
            return;
        }

        try {

            for (RoomReservationTM tm : roomReservationObList) {
                String bookingId = tm.getBookingId();
                String customerId = lblCustomerId.getText();
                String roomId = tm.getRoomId();
                Date checkInDate = tm.getCheckInDate();
                Date checkOutDate = tm.getCheckOutDate();
                double totalPrice = tm.getTotalPrice();



                boolean isPlaced = bookingModel.placeBooking(
                        bookingId,
                        customerId,
                        roomId,
                        checkInDate,
                        checkOutDate,
                        totalPrice
                );

                if (!isPlaced) {
                    new Alert(Alert.AlertType.ERROR, "Failed to place booking: " + bookingId).show();
                    return;
                }
            }

            String customerName = lblCustomerName.getText();
            double totalAmount = Double.parseDouble(lblTotalAmount.getText());

            openPaymentUI(customerName,totalAmount, roomReservationObList);

            reservationView.refresh();
            clearFields();
            updateGrandTotal();

            new Alert(Alert.AlertType.INFORMATION, "All bookings placed successfully!").show();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong while placing bookings!").show();
        }
    }
    private void openPaymentUI(String customerName, double totalAmount, ObservableList<RoomReservationTM> roomReservationObList) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/payment.fxml")
            );

            AnchorPane paymentPane = loader.load();

            // get controller & pass data
            PaymentController controller = loader.getController();
            controller.setPaymentData(customerName, totalAmount, roomReservationObList);

            // IMPORTANT: load into reservation AnchorPane
            loadPaymentToBooking.getChildren().clear();
            loadPaymentToBooking.getChildren().add(paymentPane);

            // make it fit parent
            AnchorPane.setTopAnchor(paymentPane, 0.0);
            AnchorPane.setBottomAnchor(paymentPane, 0.0);
            AnchorPane.setLeftAnchor(paymentPane, 0.0);
            AnchorPane.setRightAnchor(paymentPane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Unable to load payment screen").show();
        }
    }


}









