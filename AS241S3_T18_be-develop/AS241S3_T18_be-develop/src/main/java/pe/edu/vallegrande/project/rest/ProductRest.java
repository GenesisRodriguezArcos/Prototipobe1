package pe.edu.vallegrande.project.rest;

import pe.edu.vallegrande.project.model.Product;
import pe.edu.vallegrande.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/product")
public class ProductRest {

    private final ProductService productService;

    @Autowired
    public ProductRest(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Actualizar producto por id
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> existingProduct = productService.findById(id);
        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);  // Asegurar que el id viene de la ruta
        Product updatedProduct = productService.update(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Buscar por estado
    @GetMapping("/state/{state}")
    public List<Product> findByState(@PathVariable String state) {
        return productService.findAllByState(state);
    }

    // Eliminar producto (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable Long id) {
        Optional<Product> existingProduct = productService.findById(id);
        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Product deletedProduct = productService.delete(id);
        return ResponseEntity.ok(deletedProduct);
    }

    
// Restaurar producto (soft restore)
@PutMapping("/restore/{id}")
public ResponseEntity<Product> restore(@PathVariable Long id) {
    Optional<Product> productOpt = productService.findById(id);
    if (productOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    Product product = productOpt.get();
    product.setState("A");  // Cambiar a activo
    Product restored = productService.save(product);
    return ResponseEntity.ok(restored);
}

@GetMapping("/pdf")
    public ResponseEntity<byte[]> generateJasperPdfReport() {
        try {
            byte[] pdf = productService.generateJasperPdfReport();
            return ResponseEntity.ok()
                    //Renombrar el archivo PDF al descargar
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_clientes.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


}
