package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.dto.CustomerDTO;
import lk.ijse.hotelmanagementsystem_ijse.dto.EmployeeDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    public boolean saveEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {


        String sql = "INSERT INTO Employee (employee_id, name, email, contact, address, job_role) VALUES (?,?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                dto.getEmployeeId(),
                dto.getName(),
                dto.getEmail(),
                dto.getContact(),
                dto.getAddress(),
                dto.getJob_role()
        );
    }

    public boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {



        String sql = "UPDATE Employee SET name=?, email=?, contact=?, address=?, job_role=? " +
                "WHERE employee_id=?";

        return CrudUtil.execute(
                sql,
                dto.getName(),
                dto.getEmail(),
                dto.getContact(),
                dto.getAddress(),
                dto.getJob_role(),
                dto.getEmployeeId()
        );
    }

    public static boolean deleteEmployee(String employeeId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE employee_id=?";
        return CrudUtil.execute(sql, employeeId);
    }



    public EmployeeDTO searchEmployee(String employeeId) throws SQLException, ClassNotFoundException {

    String sql = "SELECT * FROM Employee WHERE employee_id=?";
    ResultSet rs = CrudUtil.execute(sql, employeeId);

        if (rs.next()) {

        return new EmployeeDTO(
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("contact"),
                rs.getString("address"),
                String.valueOf(rs.getDate("join_date")),
                rs.getString("job_role")
        );
    }
        return null;
}

    public List<EmployeeDTO> getAllEmployees() throws SQLException {

    ResultSet rs = CrudUtil.execute("SELECT * FROM Employee ORDER BY employee_id DESC");
    List<EmployeeDTO> employeeList = new ArrayList<>();

    while (rs.next()) {
        String[] nameParts = rs.getString("name").split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        employeeList.add(
                new EmployeeDTO(
                        rs.getString("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("address"),
                        String.valueOf(rs.getDate("join_date")),
                        rs.getString("job_role")
                )
        );
    }
    return employeeList;
}


    public String generateNextEmployeeId() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT employee_id FROM Employee ORDER BY employee_id DESC LIMIT 1"
        );

        if (rs.next()) {
            String lastId = rs.getString(1); // E.g., C005
            int number = Integer.parseInt(lastId.substring(1));
            number++;
            return String.format("E%03d", number);
        }
        return "E001";

    }
}
