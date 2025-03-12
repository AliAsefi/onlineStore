package com.example.onlineStore.entity;

import com.example.onlineStore.enums.PaymentMethod;
import com.example.onlineStore.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double amountPaid;
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // CREDIT_CARD, PAYPAL, BANK_TRANSFER

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED, FAILED

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order; // Which order was paid

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity paymentEntity = (PaymentEntity) o;
        return Objects.equals(id, paymentEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
