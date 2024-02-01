package ru.maksimov.ItemsService.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "rental_items")
@Data
@NoArgsConstructor
@Getter
@Setter
public class RentalItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "cost_hour")
    private Integer costPerHour;

    @Column(name = "cost_day")
    private Integer costPerDay;

    @OneToMany(mappedBy = "rentalItem")
    private List<RentContract> rentContracts;
}
