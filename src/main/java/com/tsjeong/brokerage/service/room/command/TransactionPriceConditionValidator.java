package com.tsjeong.brokerage.service.room.command;

import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ErrorCode;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionPriceConditionValidator {

    public static void validate(TransactionType type, BigDecimal rentMonthly, BigDecimal deposit) {

        if (Objects.isNull(rentMonthly) && Objects.isNull(deposit)) {
            throw ErrorCode.ENTITY_CONFLICT
                    .build("거래유형 '%s'에 대한 임대료 와 보증금이 모두 누락되었습니다.".formatted(type.getName()));
        }

        if (!type.getIsDepositOnly() && (rentMonthly == null || rentMonthly.signum() == 0)) {
            throw ErrorCode.ENTITY_CONFLICT.build("거래유형 '%s'에 대한 임대료가 누락되었습니다.".formatted(type.getName()));
        }

        if (type.getIsDepositOnly()) {
            if (deposit == null || deposit.signum() == 0) {
                throw ErrorCode.ENTITY_CONFLICT.build("거래유형 '%s'에 대한 보증금이 누락되었습니다.".formatted(type.getName()));
            }
            if (rentMonthly != null) {
                throw ErrorCode.ENTITY_CONFLICT.build("거래유형 '%s'에 대한 임대료를 지정할 수 없지만 임대료 '%s'를 부여하려고 합니다."
                        .formatted(type.getName(),rentMonthly));
            }
        }
    }
}
