package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity mapPaymentDtoToEntity(PaymentDto paymentDto){
        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setId(paymentDto.getId());
        paymentEntity.setPaymentDate(paymentDto.getPaymentDate());
        paymentEntity.setPaymentMethod(paymentDto.getPaymentMethod());
        paymentEntity.setPaymentStatus(paymentDto.getPaymentStatus());
        paymentEntity.setAmountPaid(paymentDto.getAmountPaid());

        return paymentEntity;
    }

    public PaymentDto mapPaymentEntityToDto(PaymentEntity paymentEntity){
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setId(paymentEntity.getId());
        paymentDto.setAmountPaid(paymentEntity.getAmountPaid());
        paymentDto.setPaymentDate(paymentEntity.getPaymentDate());
        paymentDto.setPaymentMethod(paymentEntity.getPaymentMethod());
        paymentDto.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentDto.setOrderId(paymentEntity.getOrder().getId());

        return paymentDto;
    }
}