package altera.food.ordering.domain.dto;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
    private boolean active;
    private String role;
}
