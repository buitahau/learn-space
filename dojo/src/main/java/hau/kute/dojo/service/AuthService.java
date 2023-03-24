package hau.kute.dojo.service;

import hau.kute.dojo.dto.auth.AuthDto;
import hau.kute.dojo.dto.auth.JWTDto;

public interface AuthService {
    JWTDto auth(AuthDto authDto);
}
