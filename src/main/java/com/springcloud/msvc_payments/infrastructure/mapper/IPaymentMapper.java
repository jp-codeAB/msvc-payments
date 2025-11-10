package com.springcloud.msvc_payments.infrastructure.mapper;

import com.springcloud.msvc_payments.domain.model.Payment;
import com.springcloud.msvc_payments.infrastructure.web.dto.response.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IPaymentMapper {

    IPaymentMapper INSTANCE = Mappers.getMapper(IPaymentMapper.class);

    @Mapping(source = "status", target = "status")
    PaymentResponse toResponse(Payment domainModel);

}