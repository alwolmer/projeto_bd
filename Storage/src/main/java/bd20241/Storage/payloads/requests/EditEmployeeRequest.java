package bd20241.Storage.payloads.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditEmployeeRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private boolean isManager;

    public boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }
}
