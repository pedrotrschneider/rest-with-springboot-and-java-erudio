package br.com.erudio.controllers;

import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Endpoints for authentication")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    @Operation(summary = "Authenticates an user and returns a token.")
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        if (areCredentialsNull(data))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");
        var token = authService.signin(data);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");
        return token;
    }

    private boolean areCredentialsNull(AccountCredentialsVO data) {
        return data == null || data.getUserName() == null || data.getUserName().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }
}
