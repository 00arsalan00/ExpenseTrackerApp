package com.app.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {
	private String accessToken;
    private String refreshToken;
}
