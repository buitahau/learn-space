package hau.kute.dojo.service.impl;

import hau.kute.dojo.dto.auth.AuthDto;
import hau.kute.dojo.dto.auth.JWTDto;
import hau.kute.dojo.service.AuthService;
import hau.kute.dojo.service.keycloak.KeyCloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private KeyCloakService keyCloakService;

    @Override
    public JWTDto auth(AuthDto authDto) {
        return keyCloakService.auth(authDto);
    }
}
