package lk.ijse.hotelmanagementsystem_ijse.model;



import lk.ijse.hotelmanagementsystem_ijse.dto.UserDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {

    public boolean saveUser(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `User` (user_id, username, password, email, job_role, status) VALUES (?,?,?,?,?,?)";
        return CrudUtil.execute(
                sql,
                userDTO.getUser_id(),
                userDTO.getUser_name(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getJob_role(),
                userDTO.getStatus()
        );
    }

    public boolean updateUser(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE `User` SET username=?, password=?, email=?, job_role=?, status=? WHERE user_id=?";
        return CrudUtil.execute(
                sql,
                userDTO.getUser_name(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getJob_role(),
                userDTO.getStatus(),
                userDTO.getUser_id()
        );
    }

    public boolean deleteUser(String userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM `User` WHERE user_id=?";
        return CrudUtil.execute(sql, userId);
    }

    public UserDTO searchUser(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM `User` WHERE username=? AND password=?";
        ResultSet rs = CrudUtil.execute(sql, username, password);

        if (rs.next()) {
            return convertToDto(rs);
        }
        return null;
    }

    public boolean updateUserStatus(String userId, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE `User` SET status=? WHERE user_id=?";
        return CrudUtil.execute(sql, status, userId);
    }

    public String getLastUserId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(user_id) FROM `User`");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public ArrayList<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM `User`");
        ArrayList<UserDTO> list = new ArrayList<>();

        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    private UserDTO convertToDto(ResultSet rs) throws SQLException {
        return new UserDTO(
                rs.getString("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("job_role"),
                rs.getString("status")
        );
    }
}