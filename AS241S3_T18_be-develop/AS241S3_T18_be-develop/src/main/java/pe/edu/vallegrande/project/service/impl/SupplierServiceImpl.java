package pe.edu.vallegrande.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.vallegrande.project.model.Supplier;
import pe.edu.vallegrande.project.repository.SupplierRepository;
import pe.edu.vallegrande.project.service.SupplierService;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final DataSource dataSource; 

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, DataSource dataSource) {
        this.supplierRepository = supplierRepository;
        this.dataSource = dataSource; 
    }

    @Override
    public List<Supplier> findAll() {
        log.info("Listando todos los proveedores");
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(Integer id) {
        log.info("Buscando proveedor con ID: {}", id);
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier save(Supplier supplier) {
        log.info("Guardando proveedor: {}", supplier);
        supplier.setState("A");
        validarCampos(supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Supplier supplier) {
        log.info("Actualizando proveedor: {}", supplier);
        supplier.setState("A");
        validarCampos(supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteById(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));
        supplier.setState("I");
        supplierRepository.save(supplier);
    }

    @Override
    public Supplier restore(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));
        supplier.setState("A");
        return supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> findByState(String state) {
        log.info("Listando proveedores con estado: {}", "A".equalsIgnoreCase(state) ? "Activo" : "Inactivo");
        return supplierRepository.findByState(state.toUpperCase());
    }

    private void validarCampos(Supplier supplier) {
        if (supplier.getRuc() == null || !supplier.getRuc().matches("\\d{11}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC debe tener exactamente 11 dígitos numéricos.");
        }
        if (supplier.getProductQuantity() == null || supplier.getProductQuantity() < 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad mínima de productos debe ser 50.");
        }
    }

    @Override
    public byte[] generateJasperPdfReport() throws Exception {
        // Cargar archivo .jasper en src/main/resources/reports (SIN USAR IMÁGENES EN EL JASPER)
        InputStream jasperStream = new ClassPathResource("report/supplier.jasper").getInputStream();
        // Sin parámetros
        HashMap<String, Object> params = new HashMap<>();
        // Llenar reporte con conexión a Oracle Cloud con application.yml | aplicación.properties
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource.getConnection());
        // Exportar a PDF
        log.info("Reporte Jasper en PDF");
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
