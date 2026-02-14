package lk.ijse.hotelmanagementsystem_ijse.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.hotelmanagementsystem_ijse.dto.CustomerDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.EmployeeDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.RoomDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.model.RoomDetailsModel;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RoomDetailsController implements Initializable {

    @FXML
    private TextField pricePerNightField;

    @FXML
    private TextField roomIdField;

    @FXML
    private ComboBox<String> roomTypebox;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private TableColumn colPricePerNight;

    @FXML
    private TableColumn colRoomId;

    @FXML
    private TableColumn colRoomType;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableView<RoomDetailsDTO> roomView;

    private final String ROOM_ID_REGEX = "^R\\d{3}$";
    private final String PRICE_PER_NIGHT_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";

    private final RoomDetailsModel roomDetailsModel =  new RoomDetailsModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Employee FXML is loaded");

        roomTypebox.getItems().addAll("Single_room","Double_room","Family_room");
        statusField.getItems().addAll("Available","Booked");

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPricePerNight.setCellValueFactory(new PropertyValueFactory<>("pricePerRoom"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadRoomDetailsTable();
        generateRoomId();

    }

    @FXML
    private void handleDeleteRoom() {

        String roomId = roomIdField.getText().trim();

        if (!roomId.matches(ROOM_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room ID").show();
        } else {

            try {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure to delete this Romm Details?");
                confirmAlert.setContentText("Room ID: " + roomId);

                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean result1 = roomDetailsModel.deleteRoom(roomId);

                    if (result1) {
                        new Alert(Alert.AlertType.INFORMATION, "Room Details deleted successfully!").show();
                        cleanFields();
                        loadRoomDetailsTable();
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
    private void handleResetRoom() {
     cleanFields();
    }

    @FXML
    private void handleSaveRoom() {

        String  roomId = roomIdField.getText().trim();
        String roomType = roomTypebox.getValue().trim();
        String  price = pricePerNightField.getText().trim();
        String  status = statusField.getValue().trim();

        if (!roomId.matches(ROOM_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room Id ").show();
        } else if (! price.matches(PRICE_PER_NIGHT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room Price").show();
        } else if (roomType.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room Type").show();
        } else if (status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Room Status").show();
        } else {

            try {

                double pricePerNight = Double.parseDouble(price);

                RoomDetailsDTO roomDetailsDTO = new RoomDetailsDTO(roomId,roomType,pricePerNight,status);
                boolean result = roomDetailsModel.saveRoom(roomDetailsDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Room Details saved successfully!").show();
                    cleanFields();
                    generateRoomId();
                    loadRoomDetailsTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();

            }
        }

    }

    @FXML
    private void handleUpdateRoom() {

        try {

            String  roomId = roomIdField.getText().trim();
            String  price = pricePerNightField.getText().trim();
            String roomType = roomTypebox.getValue().trim();
            String  status = statusField.getValue().trim();

            if (!roomId.matches(ROOM_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Room Id ").show();
            } else if (! price.matches(PRICE_PER_NIGHT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Room Price").show();
            } else if (roomType.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Invalid Room Type").show();
            } else if (status.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Invalid Room Status").show();
            } else {


                double pricePerNight = Double.parseDouble(price);

                RoomDetailsDTO roomDetailsDTO = new RoomDetailsDTO(roomId,roomType,pricePerNight,status);
                boolean result = roomDetailsModel.updateRoom(roomDetailsDTO);
                if(result) {
                    new Alert(Alert.AlertType.INFORMATION, "Room Details updated successfully!").show();
                    cleanFields();
                    loadRoomDetailsTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }

            }

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }



    }

    public void loadRoomDetailsTable() {
        try {

            List<RoomDetailsDTO> roomDetailsList = roomDetailsModel.getAllRooms();

            ObservableList<RoomDetailsDTO> obList = FXCollections.observableArrayList();

            for (RoomDetailsDTO roomDetailsDTO : roomDetailsList) {
                obList.add(roomDetailsDTO);
            }

            roomView.setItems(obList);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void cleanFields() {
        roomIdField.setText("");
        roomTypebox.setValue("");
        statusField.setValue("");
        pricePerNightField.setText("");

    }

    private void generateRoomId() {
        try {
            String nextId = roomDetailsModel.generateNextEmployeeId();
            roomIdField.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Room ID").show();
        }
    }

    @FXML
    private void handleSearchRoomDetails(KeyEvent event) {

        try {

            if(event.getCode() == KeyCode.ENTER) {

                String id = roomIdField.getText();

                if(!id.matches(ROOM_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                } else {

                    RoomDetailsDTO roomDetailsDTO = roomDetailsModel.searchRoom(id);

                    if(roomDetailsDTO!=null) {
                        roomIdField.setText(roomDetailsDTO.getRoomId());
                        roomTypebox.setValue(roomDetailsDTO.getRoomType());
                        pricePerNightField.setText (String.valueOf(roomDetailsDTO.getPricePerRoom()));
                        statusField.setValue(roomDetailsDTO.getStatus());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Room Details not found!").show();
                    }

                }

            }

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

}
