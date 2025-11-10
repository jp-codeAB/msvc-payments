package com.springcloud.msvc_payments.infrastructure.web.controller;

import com.springcloud.msvc_payments.domain.model.Payment;
import com.springcloud.msvc_payments.domain.port.in.IProcessPaymentUseCase;
import com.springcloud.msvc_payments.infrastructure.mapper.IPaymentMapper;
import com.springcloud.msvc_payments.infrastructure.web.dto.request.PaymentRequest;
import com.springcloud.msvc_payments.infrastructure.web.dto.response.PaymentResponse;
import com.springcloud.msvc_payments.shared.exception.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.springcloud.msvc_payments.infrastructure.security.AuthUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IProcessPaymentUseCase processPaymentUseCase;
    private final IPaymentMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT') and @paymentAccessGuard.isOrderOwner(#request.orderId, authentication.principal.id)")
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody PaymentRequest request,
            @AuthenticationPrincipal AuthUser authUser) {

        log.info("Processing payment for order ID: {} by customer ID: {}. Email: {}", request.getOrderId(), authUser.getId(), authUser.getEmail());
        if (authUser.getEmail() == null || authUser.getEmail().isBlank()) {
            throw new BusinessException("User email not available from authentication context. Authentication flow error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Payment payment = processPaymentUseCase.processPayment(
                request.getOrderId(),
                request.getPaymentMethod(),
                request.getAmount(),
                authUser.getEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(payment));
    }
}