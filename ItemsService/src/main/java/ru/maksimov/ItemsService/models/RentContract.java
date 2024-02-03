package ru.maksimov.ItemsService.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;

@Entity
@Table(name = "rent_contracts")
@Data
public class RentContract {
    @Column(name = "contract_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private RentalItem rentalItem;

    @Column(name = "borrower_id")
    private Integer borrowerId;

    @Column(name = "borrower_name")
    private String borrowerName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reserved_from")
    private LocalDateTime reservedFrom;

    @Column(name = "reserved_to")
    private LocalDateTime reservedTo;

    @Column(name = "comment")
    private String comment;

    @Column(name = "status")
    private Integer status;
}
