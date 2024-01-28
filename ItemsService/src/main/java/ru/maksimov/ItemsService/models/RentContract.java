package ru.maksimov.ItemsService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rent_contracts")
@NoArgsConstructor
@Getter
@Setter
public class RentContract {
    @Column(name = "contract_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private RentalItem rentalItem;

    @Column(name = "borrower_id")
    private Integer borrowerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reserved_to")
    private LocalDateTime reservedTo;

    public RentContract(RentalItem rentalItem, Integer borrowerId) {
        this.rentalItem = rentalItem;
        this.borrowerId = borrowerId;
    }
}
