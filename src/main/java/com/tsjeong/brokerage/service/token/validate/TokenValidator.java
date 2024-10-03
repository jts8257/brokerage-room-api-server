package com.tsjeong.brokerage.service.token.validate;

import java.util.Map;

public interface TokenValidator {

    Map<String, Object> validate(String token);
}
