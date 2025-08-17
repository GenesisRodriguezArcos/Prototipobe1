package pe.edu.vallegrande.project.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonGetter;

@Entity
@Data
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", nullable = false, length = 9, unique = true)
    private String phone;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "ruc", nullable = false, length = 11, unique = true)
    private String ruc;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @Column(name = "state", nullable = false, length = 1)
    private String state;

    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    // Setters sin validación interna (se valida en SupplierServiceImpl)
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonGetter("stateText")
    public String getStateText() {
        return "A".equals(state) ? "Activo" : "Inactivo";
    }

    public boolean isActive() {
        return "A".equals(state);
    }

    public void setActive(boolean active) {
        this.state = active ? "A" : "I";
    }
}
