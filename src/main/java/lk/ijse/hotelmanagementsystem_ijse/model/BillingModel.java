package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.dto.BillingDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillingModel {

    public boolean saveBill(BillingDTO dto)
            throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO Billing " +
                "(bill_id, booking_id, total_amount, paid_amount, billing_date, status) " +
                "VALUES (?,?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                dto.getBill_id(),
                dto.getReservation_id(),
                dto.getTotal_amount(),
                dto.getPaid_amount(),
                dto.getBilling_date(),
                dto.getStatus()
        );
    }

    public boolean updateBill(BillingDTO dto)
            throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Billing SET " +
                "booking_id=?, total_amount=?, paid_amount=?, billing_date=?, status=? " +
                "WHERE bill_id=?";

        return CrudUtil.execute(
                sql,
                dto.getReservation_id(),
                dto.getTotal_amount(),
                dto.getPaid_amount(),
                dto.getBilling_date(),
                dto.getStatus(),
                dto.getBill_id()
        );
    }

    public boolean deleteBill(String billId)
            throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM Billing WHERE bill_id=?";
        return CrudUtil.execute(sql, billId);
    }

    public BillingDTO searchBill(String billId)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Billing WHERE bill_id=?";
        ResultSet rs = CrudUtil.execute(sql, billId);

        if (rs.next()) {
            return new BillingDTO(
                    rs.getString("bill_id"),
                    rs.getString("booking_id"),
                    rs.getDouble("total_amount"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("balance"),
                    rs.getDate("billing_date").toLocalDate(),
                    rs.getString("status")
            );
        }
        return null;
    }

    public List<BillingDTO> getAllBills()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Billing");
        List<BillingDTO> billList = new ArrayList<>();

        while (rs.next()) {
            billList.add(
                    new BillingDTO(
                            rs.getString("bill_id"),
                            rs.getString("booking_id"),
                            rs.getDouble("total_amount"),
                            rs.getDouble("paid_amount"),
                            rs.getDouble("balance"),
                            rs.getDate("billing_date").toLocalDate(),
                            rs.getString("status")
                    )
            );
        }
        return billList;
    }


    public boolean updatePayment(String billId, double paidAmount)
            throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Billing SET paid_amount=? WHERE bill_id=?";
        return CrudUtil.execute(sql, paidAmount, billId);
    }

    public BillingDTO getBillByBookingId(String bookingId)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Billing WHERE booking_id=?";
        ResultSet rs = CrudUtil.execute(sql, bookingId);

        if (rs.next()) {
            return new BillingDTO(
                    rs.getString("bill_id"),
                    rs.getString("booking_id"),
                    rs.getDouble("total_amount"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("balance"),
                    rs.getDate("billing_date").toLocalDate(),
                    rs.getString("status")
            );
        }
        return null;
    }
}