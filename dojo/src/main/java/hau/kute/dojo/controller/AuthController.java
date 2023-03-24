package hau.kute.dojo.controller;

import hau.kute.dojo.dto.auth.AuthDto;
import hau.kute.dojo.dto.auth.JWTDto;
import hau.kute.dojo.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public JWTDto auth(@RequestBody AuthDto authDto) {
        logger.info("Authenticating with keycloak ...");
        return authService.auth(authDto);
    }
}
