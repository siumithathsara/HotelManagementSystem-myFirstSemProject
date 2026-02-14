package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.dto.RoomDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDetailsModel {

    public boolean saveRoom(RoomDetailsDTO dto) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO Room_Details (room_id, room_type, price_per_room, status) " +
                "VALUES (?,?,?,?)";

        return CrudUtil.execute(
                sql,
                dto.getRoomId(),
                dto.getRoomType(),
                dto.getPricePerRoom(),
                dto.getStatus()
        );
    }

    public boolean updateRoom(RoomDetailsDTO dto) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Room_Details SET room_type=?, price_per_room=?, status=? " +
                "WHERE room_id=?";

        return CrudUtil.execute(
                sql,
                dto.getRoomType(),
                dto.getPricePerRoom(),
                dto.getStatus(),
                dto.getRoomId()
        );
    }

    public boolean deleteRoom(String roomId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Room_Details WHERE room_id=?";
        return CrudUtil.execute(sql, roomId);
    }

    public RoomDetailsDTO searchRoom(String roomId) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Room_Details WHERE room_id=?";
        ResultSet rs = CrudUtil.execute(sql, roomId);

        if (rs.next()) {
            return new RoomDetailsDTO(
                    rs.getString("room_id"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_room"),
                    rs.getString("status")
            );
        }
        return null;
    }

    public List<RoomDetailsDTO> getAllRooms() throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Room_Details ORDER BY room_id DESC");
        List<RoomDetailsDTO> roomList = new ArrayList<>();

        while (rs.next()) {
            roomList.add(
                    new RoomDetailsDTO(
                            rs.getString("room_id"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_room"),
                            rs.getString("status")
                    )
            );
        }
        return roomList;
    }



    public boolean updateRoomStatus(String roomId, String status)
            throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Room_Details SET status=? WHERE room_id=?";
        return CrudUtil.execute(sql, status, roomId);
    }

    public List<RoomDetailsDTO> getAvailableRooms()
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Room_Details WHERE status='Available'";
        ResultSet rs = CrudUtil.execute(sql);

        List<RoomDetailsDTO> availableRooms = new ArrayList<>();
        while (rs.next()) {
            availableRooms.add(
                    new RoomDetailsDTO(
                            rs.getString("room_id"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_room"),
                            rs.getString("status")
                    )
            );
        }
        return availableRooms;
    }

    public String generateNextEmployeeId() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT room_id FROM Room_Details ORDER BY room_id DESC LIMIT 1"
        );

        if (rs.next()) {
            String lastId = rs.getString(1); // E.g., C005
            int number = Integer.parseInt(lastId.substring(1));
            number++;
            return String.format("R%03d", number);
        }
        return "R001";
    }
}