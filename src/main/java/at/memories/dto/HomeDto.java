package at.memories.dto;

import java.io.Serializable;

public class HomeDto implements Serializable {
    private String homeName;
    private UserDto admin;
    private UserDto user;

    public HomeDto(String homeName, UserDto admin, UserDto user) {
        this.homeName = homeName;
        this.admin = admin;
        this.user = user;
    }

    public UserDto getAdmin() {
        return admin;
    }

    public void setAdmin(UserDto admin) {
        this.admin = admin;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }
}
