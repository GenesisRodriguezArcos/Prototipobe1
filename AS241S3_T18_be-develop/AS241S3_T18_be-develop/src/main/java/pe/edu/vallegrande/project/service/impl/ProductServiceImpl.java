package pe.edu.vallegrande.project.service.impl;

import pe.edu.vallegrande.project.model.Product;
import pe.edu.vallegrande.project.repository.ProductRepository;
import pe.edu.vallegrande.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

@Slf4j
@Service

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    

    @Autowired
    private DataSource dataSource;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        log.info("Listando Datos");
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        log.info("Buscando producto por ID: {}", id);
        return productRepository.findById(id);
    }

    @Override
public Product save(Product productModel) {
    log.info("Registrando producto: {}", productModel);
    return productRepository.save(productModel);
}

@Override
public Product update(Product productModel) {
    log.info("Actualizando producto: {}", productModel);
    return productRepository.save(productModel);
}


    @Override
    public List<Product> findAllByState(String state) {
        String dbState = "A".equalsIgnoreCase(state) || "Activo".equalsIgnoreCase(state) ? "A" : "I";
        log.info("Listando productos con estado: {}", dbState);
        return productRepository.findAllByState(dbState);
    }

    @Override
    public Product delete(Long id) {
        log.info("Eliminando lógicamente producto ID: {}", id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        Product product = productOpt.get();
        product.setState("I");
        return productRepository.save(product);
    }

   @Override
    public Product restore(Long id) {
    log.info("Restaurando producto ID: {}", id);
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    product.setState("A");
    return productRepository.save(product);
}

@Override
    public byte[] generateJasperPdfReport() throws Exception {
        // Cargar archivo .jasper en src/main/resources/reports (SIN USAR IMÁGENES EN EL JASPER)
        InputStream jasperStream = new ClassPathResource("report/product.jasper").getInputStream();
        // Sin parámetros
        HashMap<String, Object> params = new HashMap<>();
        // Llenar reporte con conexión a Oracle Cloud con application.yml | aplicación.properties
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
        // Exportar a PDF
        log.info("Reporte Jasper en PDF");
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
