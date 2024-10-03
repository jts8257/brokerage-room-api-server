package com.tsjeong.brokerage.service.token;

import java.util.Map;

public interface TokenValidator {

    Map<String, Object> validate(String token);
}
