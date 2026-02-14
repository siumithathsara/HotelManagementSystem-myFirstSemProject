package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.dto.CustomerDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public boolean saveCustomer(CustomerDTO customerDTO) throws Exception {
        return CrudUtil.execute(
                "INSERT INTO Customer (customer_id, name, contact, email, nic_passport, address) VALUES (?,?,?,?,?,?)",
                customerDTO.getCustomer_id(),
                customerDTO.getName(),
                customerDTO.getContact(),
                customerDTO.getEmail(),
                customerDTO.getNic_passport(),
                customerDTO.getAddress()
        );
    }

    public boolean updateCustomer(CustomerDTO customerDTO) throws Exception {
        String sql = "UPDATE Customer SET name=?, contact=?, email=?, nic_passport=?, address=? WHERE customer_id=?";

        return CrudUtil.execute(
                sql,
                customerDTO.getName(),
                customerDTO.getContact(),
                customerDTO.getEmail(),
                customerDTO.getNic_passport(),
                customerDTO.getAddress(),
                customerDTO.getCustomer_id()
        );
    }

    public boolean deleteCustomer(String customer_id) throws Exception {
        return CrudUtil.execute(
                "DELETE FROM Customer WHERE customer_id=?",
                customer_id
        );
    }

    public CustomerDTO searchCustomer(String customerId) throws Exception {
        ResultSet rs = CrudUtil.execute(
                "SELECT * FROM Customer WHERE customer_id=?",
                customerId
        );

        if (rs.next()) {
            return new CustomerDTO(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("nic_passport"),
                    rs.getString("address")
            );
        }
        return null;
    }

    public List<CustomerDTO> getCustomers() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT * FROM Customer ORDER BY customer_id DESC"
        );

        List<CustomerDTO> customerList = new ArrayList<>();
        while (rs.next()) {
            customerList.add(new CustomerDTO(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("nic_passport"),
                    rs.getString("address")
            ));
        }
        return customerList;
    }

    public String generateNextCustomerId() throws Exception {
        ResultSet rs = CrudUtil.execute(
                "SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1"
        );

        if (rs.next()) {
            String lastId = rs.getString(1); // E.g., C005
            int number = Integer.parseInt(lastId.substring(1));
            number++;
            return String.format("C%03d", number);
        }
        return "C001";
    }
}