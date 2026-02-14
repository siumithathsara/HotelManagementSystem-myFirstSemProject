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
import lk.ijse.hotelmanagementsystem_ijse.model.CustomerModel;
import lk.ijse.hotelmanagementsystem_ijse.model.EmployeeModel;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {


    @FXML
    private TextField addressField;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField employeeIdField;

    @FXML
    private Label joinDateField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> jobRoleBox;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colContact;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colEmployeeId;

    @FXML
    private TableColumn colJobRole;

    @FXML
    private TableColumn colJoinDate;

    @FXML
    private TableColumn colName;

    @FXML
    private TableView employeeView;

    private final EmployeeModel employeeModel = new EmployeeModel();


    private final String EMPLOYEE_ID_REGEX = "^E\\d{3}$";
    private final String EMPLOYEE_NAME_REGEX = "^[A-Za-z]{3,}(?:\\s[A-Za-z]{3,})+$";
    private final String EMPLOYEE_ADDRESS_REGEX = "^[A-Za-z.-]{2,}$";
    private final String EMPLOYEE_EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String CONTACT_REGEX = "^0[7][0-9]{8}$";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Employee FXML is loaded");

        jobRoleBox.getItems().addAll("Admin","Manager","Receptionist","Room Attendant","Kitchen Staff");

        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("job_role"));
        colJoinDate.setCellValueFactory(new PropertyValueFactory<>("joining_date"));

        loadEmployeeTable();
        generateEmployeeId();

    }

    @FXML
   private void handleDeleteEmployee() {

        String employeeId = employeeIdField.getText().trim();

        if (!employeeId.matches(EMPLOYEE_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID").show();
        } else {

            try {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure to delete this Employee?");
                confirmAlert.setContentText("Employee ID: " + employeeId);

                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean result1 = EmployeeModel.deleteEmployee(employeeId);

                    if (result1) {
                        new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
                        clearFields();
                        loadEmployeeTable();
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
   private void handleEmployeeSave() {

        String  employeeId = employeeIdField.getText().trim();
        String  name = nameField.getText().trim();
        String  email = EmailField.getText().trim();
        String contact = contactField.getText().trim();
        String  address = addressField.getText().trim();
        String  joinDate = joinDateField.getText().trim();
        String  jobRolle = jobRoleBox.getValue().trim();

        if (!contact.matches(CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Contact Number").show();
        } else if (! employeeId.matches(EMPLOYEE_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID").show();
        } else if (!email.matches(EMPLOYEE_EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Email").show();
        } else if (!name.matches(EMPLOYEE_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Name").show();
        } else if (!address.matches(EMPLOYEE_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Address").show();
        } else if (jobRolle.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Job Role").show();
        } else {

            try {

                EmployeeDTO employeeDTO = new EmployeeDTO(address, contact, email, employeeId, jobRolle, name);
                boolean result = employeeModel.saveEmployee(employeeDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully!").show();
                    clearFields();
                    generateEmployeeId();
                    loadEmployeeTable();
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
   private void handleResetEmployee() {
        clearFields();
    }

    @FXML
   private void handleUpdateEmployee() {

        try {

            String employeeId = employeeIdField.getText().trim();
            String name = nameField.getText().trim();
            String email = EmailField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();
            String jobRole = jobRoleBox.getValue().trim();


            if (!contact.matches(CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Employee Contact Number").show();
            } else if (! employeeId.matches(EMPLOYEE_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Employee ID").show();
            } else if (!email.matches(EMPLOYEE_EMAIL_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Employee Email").show();
            } else if (!name.matches(EMPLOYEE_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Employee Name").show();
            } else if (!address.matches(EMPLOYEE_ADDRESS_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Employee Address").show();
            } else if (jobRole.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Invalid Employee Job Role").show();
            } else {

                EmployeeDTO employeeDTO = new EmployeeDTO(address, contact, email, employeeId, jobRole, name);
                boolean result = employeeModel.updateEmployee(employeeDTO);

                if(result) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!").show();
                    clearFields();
                    loadEmployeeTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }

            }

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }


    }

    public void loadEmployeeTable() {
        try {

            List<EmployeeDTO> empList = employeeModel.getAllEmployees();

            ObservableList<EmployeeDTO> obList = FXCollections.observableArrayList();

            for (EmployeeDTO employeeDTO : empList) {
                obList.add(employeeDTO);
            }

            employeeView.setItems(obList);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void clearFields() {
        employeeIdField.setText("");
        nameField.setText("");
        joinDateField.setText("");
        addressField.setText("");
        EmailField.setText("");
        contactField.setText("");
        jobRoleBox.setValue("");

    }

    @FXML
    private void handleSearchEmployee(KeyEvent event) {

        try {

            if(event.getCode() == KeyCode.ENTER) {

                String id = employeeIdField.getText();

                if(!id.matches(EMPLOYEE_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                } else {

                    EmployeeDTO employeeDTO = employeeModel.searchEmployee(id);

                    if(employeeDTO!=null) {
                        nameField.setText(employeeDTO.getName());
                        contactField.setText(employeeDTO.getContact());
                        EmailField.setText(employeeDTO.getEmail());
                        joinDateField.setText(employeeDTO.getJoining_date());
                        addressField.setText(employeeDTO.getAddress());
                        jobRoleBox.setValue(employeeDTO.getJob_role());
                        employeeIdField.setText(employeeDTO.getEmployeeId());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Customer not found!").show();
                    }

                }

            }

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    private void generateEmployeeId() {
        try {
            String nextId = employeeModel.generateNextEmployeeId();
            employeeIdField.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Employee ID").show();
        }
    }
}