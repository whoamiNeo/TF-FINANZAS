package pe.edu.upc.TFFinanzas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.upc.TFFinanzas.dtos.auth.AuthRequestDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.RegisterRequestDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        ResponseDTO response = authService.login(request);
        if(response.getMessage().equals("Credenciales incorrectas."))
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        else 
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        ResponseDTO response = authService.register(request);
        if(response.getMessage().equals("El usuario ya existe."))
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        else 
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
