package com.springcloud.msvc_payments.infrastructure.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusUpdateNotification {
    private String newStatus;
}
