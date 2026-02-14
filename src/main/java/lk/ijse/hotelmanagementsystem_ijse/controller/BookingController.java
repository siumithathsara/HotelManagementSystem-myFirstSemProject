package lk.ijse.hotelmanagementsystem_ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.hotelmanagementsystem_ijse.dto.*;
import lk.ijse.hotelmanagementsystem_ijse.dto.tm.BookingTM;
import lk.ijse.hotelmanagementsystem_ijse.model.BookingDetailsModel;
import lk.ijse.hotelmanagementsystem_ijse.model.BookingModel;
import lk.ijse.hotelmanagementsystem_ijse.model.CustomerModel;
import lk.ijse.hotelmanagementsystem_ijse.model.RoomDetailsModel;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookingController implements Initializable {
    @FXML
    private ComboBox<String> actionStatusBox;

    @FXML
    private TextField bookingIdField;

    @FXML
    private TextField checkInField;

    @FXML
    private TableColumn<BookingTM, String> colBookingId;

    @FXML
    private TableColumn<BookingTM, LocalDate> colCheckIn;

    @FXML
    private TableColumn<BookingTM, String> colCustomerName;

    @FXML
    private TableColumn<BookingTM, Double> colPrice;

    @FXML
    private TableColumn<BookingTM, String> colRoomId;

    @FXML
    private TableColumn<BookingTM, String> colRoomType;

    @FXML
    private TableColumn<BookingTM, String> colStatus;

    @FXML
    private ComboBox<String> customerIdBox;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblRoomType;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<BookingTM> reservationView;

    @FXML
    private ComboBox<String> roomIdBox;

    private CustomerModel customerModel = new CustomerModel();
    private RoomDetailsModel roomDetailsModel = new RoomDetailsModel();
    private BookingModel bookingModel = new BookingModel();
    private final BookingDetailsModel bookingDetailsModel = new BookingDetailsModel();

    private final String BOOKING_ID_REGEX = "^B\\d{3}$";
    private final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        actionStatusBox.getItems().setAll("Pending","Confirmed","Cancelled");
        loadCustomerIds();
        loadRoomIds();
        generateBookingId();
        loadBookingTable();

    }

    private void loadCustomerIds() {
        try {
            List<CustomerDTO> customerList = customerModel.getCustomers();

            ObservableList<String> obList = FXCollections.observableArrayList();

            for (CustomerDTO customerDTO : customerList) {
                obList.add(String.valueOf(customerDTO.getCustomer_id()));
            }

            customerIdBox.setItems(obList);

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    private void loadRoomIds() {
        try {

            List<RoomDetailsDTO> roomDetailsDTOList = roomDetailsModel.getAllRooms();

            ObservableList<String> obList = FXCollections.observableArrayList();

            for (RoomDetailsDTO roomDetailsDTO : roomDetailsDTOList) {
                obList.add(String.valueOf(roomDetailsDTO.getRoomId()));
            }

            roomIdBox.setItems(obList);

        }catch (Exception e) {

        }
    }

    @FXML
    void handleSelectCustomerId(ActionEvent event) {

        try {
            String selectedCusId =
                    customerIdBox.getSelectionModel().getSelectedItem();

            if (selectedCusId == null || selectedCusId.isBlank()) {
                lblCustomerName.setText("");
                return;
            }

            CustomerDTO customerDTO =
                    customerModel.searchCustomer(selectedCusId);

            if (customerDTO == null) {
                lblCustomerName.setText("");
                return;
            }

            lblCustomerName.setText(customerDTO.getName());

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Failed to load customer details").show();
        }
    }


    @FXML
    void handleSelectRoomId(ActionEvent event) {
        try {
            String selectedRoomId = roomIdBox.getSelectionModel().getSelectedItem();


            if (selectedRoomId == null || selectedRoomId.isBlank()) {
                lblRoomType.setText("");
                lblStatus.setText("");
                return;
            }
            RoomDetailsDTO roomDetailsDTO =
                    roomDetailsModel.searchRoom(selectedRoomId);

            if (roomDetailsDTO == null) {
                lblRoomType.setText("");
                lblStatus.setText("");
                return;
            }
            lblRoomType.setText(roomDetailsDTO.getRoomType());
            lblStatus.setText(roomDetailsDTO.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load room details").show();
        }
    }


    private void generateBookingId() {
        try {
            String nextId = bookingModel.generateNextBookingId();
            bookingIdField.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Booking ID").show();
        }
    }

    public void loadBookingTable() {
        try {

            List<BookingDTO> bookingDTOList = bookingModel.getAllBookings();

            ObservableList<BookingTM> obList = FXCollections.observableArrayList();

            for (BookingDTO dto : bookingDTOList) {

                CustomerDTO customer = customerModel.searchCustomer(dto.getCustomer_id());
                BookingDetailsDTO bookingDetails = bookingDetailsModel.getBookingDetails(dto.getBooking_id());
                RoomDetailsDTO room = roomDetailsModel.searchRoom(bookingDetails.getRoomId());

                BookingTM tm = new BookingTM(
                        dto.getBooking_id(),
                        customer.getName(),
                        room.getRoomId(),
                        room.getRoomType(),
                        room.getPricePerRoom(),
                        dto.getBooking_date(),
                        dto.getStatus()
                );

                obList.add(tm);
            }

            reservationView.setItems(obList);
            reservationView.refresh();

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load booking table").show();
        }
    }

    @FXML
    private void handleBookingSave(ActionEvent event) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String bookingId = bookingIdField.getText();
        String customerId = customerIdBox.getValue();
        String customerName = lblCustomerName.getText();
        String roomId = roomIdBox.getValue();
        String roomType = lblRoomType.getText();
        String checkInDate = checkInField.getText();
        String status = actionStatusBox.getValue();

        if (!bookingId.matches(BOOKING_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Booking ID").show();
        } else if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
        } else if (customerName.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").show();
        } else if (roomId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room ID").show();
        } else if (roomType.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room Type").show();
        } else if (!checkInDate.matches(DATE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Check in Date").show();
        } else if (status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Status").show();
        } else {


            try {


                BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO(bookingId, roomId, checkInDate);

                BookingDTO bookingDTO = new BookingDTO(bookingId, customerId, sdf.parse(checkInDate), status, bookingDetailsDTO);

                boolean isSaved = bookingModel.saveBooking(bookingDTO);

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Booking Details Added Successfully").show();
                    clearFields();
                    generateBookingId();
                    loadBookingTable();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void handleDeleteBooking(ActionEvent event) {

        String bookingId = bookingIdField.getText().trim();

        if (!bookingId.matches(BOOKING_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Booking ID").show();
        } else {

            try {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure to delete this booking Details?");
                confirmAlert.setContentText("Booking ID: " + bookingId);

                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean result1 = bookingModel.deleteBooking(bookingId);

                    if (result1) {
                        new Alert(Alert.AlertType.INFORMATION, "Booking Details deleted successfully!").show();

                        loadBookingTable();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }


    @FXML
    private void handleResetBooking(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void handleUpdateBooking(ActionEvent event) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String bookingId = bookingIdField.getText();
        String customerId = customerIdBox.getValue();
        String customerName = lblCustomerName.getText();
        String roomId = roomIdBox.getValue();
        String roomType = lblRoomType.getText();
        String checkInDate = checkInField.getText();
        String status = actionStatusBox.getValue();

        if (!bookingId.matches(BOOKING_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Booking ID").show();
        } else if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
        } else if (customerName.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").show();
        } else if (roomId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room ID").show();
        } else if (roomType.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room Type").show();
        } else if (!checkInDate.matches(DATE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Check in Date").show();
        } else if (status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Status").show();
        } else {

            try {


                BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO(bookingId, roomId, checkInDate);

                BookingDTO bookingDTO = new BookingDTO(bookingId, customerId, sdf.parse(checkInDate), status, bookingDetailsDTO);

                boolean isSaved = bookingModel.updateBooking(bookingDTO);

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Booking Details Update Successfully").show();

                    clearFields();
                    loadBookingTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void clearFields() {
        bookingIdField.clear();
        customerIdBox.setValue(null);
        roomIdBox.setValue(null);
        checkInField.clear();
        actionStatusBox.setValue(null);

        lblCustomerName.setText("");
        lblRoomType.setText("");
        lblStatus.setText("");
    }
    @FXML
    private void handleSearchBooking(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.ENTER) {

                String bookingId = bookingIdField.getText().trim();

                if (!bookingId.matches(BOOKING_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Booking ID").show();
                    return;
                }

                BookingDTO bookingDTO = bookingModel.searchBooking(bookingId);

                if (bookingDTO == null) {
                    new Alert(Alert.AlertType.ERROR, "Booking not found!").show();
                    return;
                }

                bookingIdField.setText(bookingDTO.getBooking_id());
                customerIdBox.setValue(bookingDTO.getCustomer_id());
                actionStatusBox.setValue(bookingDTO.getStatus());


                checkInField.setText(String.valueOf(bookingDTO.getBooking_date()));


                CustomerDTO customerDTO =
                        customerModel.searchCustomer(bookingDTO.getCustomer_id());
                lblCustomerName.setText(customerDTO.getName());


                BookingDetailsDTO details =
                        bookingDetailsModel.getBookingDetails(bookingDTO.getBooking_id());

                if (details != null) {
                    roomIdBox.setValue(details.getRoomId());

                    RoomDetailsDTO room =
                            roomDetailsModel.searchRoom(details.getRoomId());

                    if (room != null) {
                        lblRoomType.setText(room.getRoomType());
                        lblStatus.setText(room.getStatus());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Search failed").show();
        }
    }
}
