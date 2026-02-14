package lk.ijse.hotelmanagementsystem_ijse.dto;

import lombok.Getter;
import lombok.Setter;

public class Session {

    @Getter
    @Setter
    private static UserDTO currentUser;

    public static void clear() {
        currentUser = null;
    }
}
