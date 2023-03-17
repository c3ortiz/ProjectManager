package com.codingdojo.ProjectManager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class LoginUser {
    @NotEmpty(message = "Ingrese un email")
    @Email(message = "Ingrese un email valido")
    private String email;

    @NotEmpty(message = "Ingrese una contraseña")
    @Size(min = 6, max = 128, message = "Password debe tener entre 6 y 20 caracteres")
    private String password;
}
