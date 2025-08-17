package pe.edu.vallegrande.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.project.model.Supplier;
import pe.edu.vallegrande.project.service.SupplierService;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/supplier")
public class SupplierRest {

    private final SupplierService supplierService;

    @Autowired
    public SupplierRest(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<Supplier> findAll() {
        return supplierService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Supplier> findById(@PathVariable Integer id) {
        return supplierService.findById(id);
    }

    @GetMapping("/state/{state}")
    public List<Supplier> findByState(@PathVariable String state) {
        String upperState = state.toUpperCase();
        if (!upperState.equals("A") && !upperState.equals("I")) {
            throw new IllegalArgumentException("Estado inválido. Debe ser 'A' o 'I'.");
        }
        return supplierService.findByState(upperState);
    }

    @PostMapping("/save")
    public Supplier save(@RequestBody Supplier supplier) {
        return supplierService.save(supplier);
    }

    @PutMapping("/update")
    public Supplier update(@RequestBody Supplier supplier) {
        return supplierService.update(supplier);
    }

    @PutMapping("/restore/{id}")
    public Supplier restore(@PathVariable Integer id) {
        return supplierService.restore(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        supplierService.deleteById(id);
    }

    // 🧾 Descargar PDF con reporte de proveedores
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateJasperPdfReport() {
        try {
            byte[] pdf = supplierService.generateJasperPdfReport();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_proveedores.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
