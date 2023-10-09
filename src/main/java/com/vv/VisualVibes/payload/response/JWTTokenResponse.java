package com.vv.VisualVibes.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenResponse {
    private boolean success;
    private String token;
}
