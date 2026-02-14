package lk.ijse.hotelmanagementsystem_ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.hotelmanagementsystem_ijse.dto.PaymentDto;
import lk.ijse.hotelmanagementsystem_ijse.dto.tm.PaymentTM;
import lk.ijse.hotelmanagementsystem_ijse.dto.tm.RoomReservationTM;
import lk.ijse.hotelmanagementsystem_ijse.model.PaymentModel;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class PaymentController implements Initializable {

    @FXML
    private ComboBox<String> cmbPaymentMethod;
    @FXML
    private TableView<PaymentTM> paymentTable;
    @FXML
    private TableColumn<PaymentTM, String> colPaymentId;
    @FXML
    private TableColumn<PaymentTM, String> colStatus;
    @FXML
    private TableColumn<PaymentTM, Date> colDate;
    @FXML
    private TableColumn<PaymentTM, Double> colAmount;
    @FXML
    private TableColumn<PaymentTM, String> colMethod;
    @FXML
    private Label lblGuestName;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private TextField paymentIdField;

    private final PaymentModel paymentModel = new PaymentModel();
    private final ObservableList<PaymentTM> paymentObList = FXCollections.observableArrayList();
    private ObservableList<RoomReservationTM> roomAllDetails = FXCollections.observableArrayList();;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cmbPaymentMethod.getItems().addAll("Cash", "Card");

        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));

        generatePaymentId();
        loadPaymentTable();
    }

    public void setPaymentData(String customerName, double totalAmount, ObservableList<RoomReservationTM> roomReservationList) {

        lblGuestName.setText(customerName);
        lblTotalAmount.setText(String.valueOf(totalAmount));
        this.roomAllDetails = roomReservationList;
    }

    @FXML
    private void handleCompletePayment(ActionEvent event) {

        try {
            String paymentId = paymentIdField.getText();
            String method = cmbPaymentMethod.getValue();

            if (paymentId.isEmpty() || method == null || roomAllDetails == null) {
                new Alert(Alert.AlertType.ERROR, "Invalid payment data").show();
                return;
            }

            double totalAmount = Double.parseDouble(lblTotalAmount.getText());

            boolean success = paymentModel.saveFullPayment(
                    paymentId,
                    totalAmount,
                    method,
                    roomAllDetails
            );

            if (!success) {
                new Alert(Alert.AlertType.ERROR, "Payment failed").show();
                return;
            }

            new Alert(Alert.AlertType.INFORMATION, "Payment completed successfully").show();

            generatePaymentId();
            loadPaymentTable();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "System error occurred").show();
        }
    }

    private void loadPaymentTable() {
        try {
            paymentObList.clear();

            List<PaymentDto> payments = paymentModel.getAllPayments();

            for (PaymentDto dto : payments) {
                paymentObList.add(
                        new PaymentTM(
                                dto.getPayment_id(),
                                dto.getPayment_status(),
                                Date.valueOf(dto.getPayment_date()),
                                dto.getAmount(),
                                dto.getPayment_method()
                        )
                );
            }

            paymentTable.setItems(paymentObList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatePaymentId() {
        try {
            paymentIdField.setText(paymentModel.generateNextPaymentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        cmbPaymentMethod.getSelectionModel().clearSelection();
    }
}
