package pe.edu.vallegrande.project.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 60)
    private String name;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "highest_price", nullable = false)
    private Integer highestPrice;

    @Column(name = "category", nullable = false, length = 60)
    private String category;

    @Column(name = "brand", nullable = false, length = 60)
    private String brand;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "exit_date", nullable = false)
    private LocalDateTime exitDate;

    @Column(name = "period", nullable = false)
    private LocalDateTime period;

   @Column(name = "state")
   private String state;

}
