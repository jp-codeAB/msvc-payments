package com.springcloud.msvc_payments.infrastructure.integration.messaging;

import com.springcloud.msvc_payments.domain.model.PaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface IPaymentEventMapper {

    @Mapping(target = "eventId", expression = "java(generateUuid())")
    @Mapping(target = "timestamp", expression = "java(getTimestamp())")
    @Mapping(source = "status", target = "paymentStatus", qualifiedByName = "mapStatusToString")
    PaymentStatusEvent toPaymentStatusEvent(
            Long orderId,
            Long customerId,
            PaymentStatus status,
            String customerEmail
    );

    default String generateUuid() {
        return UUID.randomUUID().toString();
    }

    default LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }

    @Named("mapStatusToString")
    default String mapStatusToString(PaymentStatus status) {
        return status != null ? status.name() : null;
    }
}