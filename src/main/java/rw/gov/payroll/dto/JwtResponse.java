package rw.gov.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String code;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;

    public JwtResponse(String token, String code, String email, String firstName, String lastName, Set<String> roles) {
        this.token = token;
        this.code = code;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}