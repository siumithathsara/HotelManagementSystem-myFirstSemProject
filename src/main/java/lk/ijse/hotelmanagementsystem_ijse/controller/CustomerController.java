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
import lk.ijse.hotelmanagementsystem_ijse.model.CustomerModel;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    private final String CUSTOMER_ID_REGEX = "^C\\d{3}$";
    private final String CUSTOMER_FIRST_NAME_REGEX = "^[A-Za-z]{3,}(?:\\s[A-Za-z]{3,})+$";
    private final String CUSTOMER_ADDRESS_REGEX = "^[A-Za-z.-]{2,}$";
    private final String CUSTOMER_EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String NIC_OR_PASSPORT_REGEX = "^([0-9]{9}[vVxX]|[0-9]{12}|[A-Za-z][A-Za-z0-9]{5,8})$";
    private final String CONTACT_REGEX = "^\\+[1-9][0-9]{7,14}$";
    private final CustomerModel customerModel = new CustomerModel();
    @FXML
    private TextField contactField;
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField nicField;
    @FXML
    private TextField addressField;
    @FXML
    private TableColumn colAddress;
    @FXML
    private TableColumn colContact;
    @FXML
    private TableColumn colCustomerId;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colNic;
    @FXML
    private TableView customerView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Customer FXML is loaded");

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic_passport"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadCustomerTable();
        generateCustomerId();


    }


    @FXML
    private void handleCustomerSave() {
        String Sid = customerIdField.getText().trim();
        String name = firstNameField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String nic = nicField.getText().trim();
        String address = addressField.getText().trim();

        if (!contact.matches(CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Contact Number").show();
        } else if (!Sid.matches(CUSTOMER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
        } else if (!email.matches(CUSTOMER_EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Email").show();
        } else if (!name.matches(CUSTOMER_FIRST_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").show();
        } else if (!address.matches(CUSTOMER_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Address").show();
        } else if (!nic.matches(NIC_OR_PASSPORT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer NIC").show();
        } else {

            try {


                CustomerDTO customerDTO = new CustomerDTO(Sid, name, contact, email, nic, address);
                boolean result = customerModel.saveCustomer(customerDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully!").show();
                    clearFields();
                    generateCustomerId();
                    loadCustomerTable();
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
    private void handleUpdateCustomer() {
        try {

            String id = customerIdField.getText().trim();
            String name = firstNameField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();
            String nic = nicField.getText().trim();
            String address = addressField.getText().trim();

            if (!id.matches(CUSTOMER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer id").show();
            } else if (!name.matches(CUSTOMER_FIRST_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer name").show();
            } else if (!contact.matches(CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer contact").show();
            } else if (!email.matches(CUSTOMER_EMAIL_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer email").show();
            } else if (!nic.matches(NIC_OR_PASSPORT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer nic").show();
            } else if (!address.matches(CUSTOMER_ADDRESS_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer address").show();
            } else {

                CustomerDTO customerDTO = new CustomerDTO(id, name, contact, email, nic, address);
                boolean result = customerModel.updateCustomer(customerDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
                    cleanFields();
                    loadCustomerTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    private void cleanFields() {
        customerIdField.setText("");
        firstNameField.setText("");
        contactField.setText("");
        emailField.setText("");
        addressField.setText("");
        nicField.setText("");
    }

    @FXML
    private void handleDeleteCustomer() {

        String customerId = customerIdField.getText().trim();

        if (!customerId.matches(CUSTOMER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
        } else {

            try {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure to delete this customer?");
                confirmAlert.setContentText("Customer ID: " + customerId);

                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean result1 = customerModel.deleteCustomer(customerId);

                    if (result1) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                        clearFields();
                        loadCustomerTable();
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
    private void handleResetCustomer() {
        clearFields();
    }

    private void clearFields() {
        customerIdField.setText("");
        firstNameField.setText("");
        // lastNameField.setText("");
        nicField.setText("");
        addressField.setText("");
        emailField.setText("");
        contactField.setText("");

    }


    public void loadCustomerTable() {
        try {

            List<CustomerDTO> customerList = customerModel.getCustomers();

            ObservableList<CustomerDTO> obList = FXCollections.observableArrayList();

            for (CustomerDTO customerDTO : customerList) {
                obList.add(customerDTO);
            }

            customerView.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void handleSearchCustomer(KeyEvent event) {

        try {

            if (event.getCode() == KeyCode.ENTER) {

                String id = customerIdField.getText();

                if (!id.matches(CUSTOMER_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                } else {

                    CustomerDTO customerDTO = customerModel.searchCustomer(id);

                    if (customerDTO != null) {
                        firstNameField.setText(customerDTO.getName());
                        contactField.setText(customerDTO.getContact());
                        emailField.setText(customerDTO.getEmail());
                        nicField.setText(customerDTO.getNic_passport());
                        addressField.setText(customerDTO.getAddress());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Customer not found!").show();
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    private void generateCustomerId() {
        try {
            String nextId = customerModel.generateNextCustomerId();
            customerIdField.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Customer ID").show();
        }
    }


}

