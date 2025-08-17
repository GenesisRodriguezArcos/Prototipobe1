package pe.edu.vallegrande.project.service;

import pe.edu.vallegrande.project.model.Supplier;
import org.springframework.stereotype.Service; // Para marcar como servicio
import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un servicio de Spring
public interface SupplierService {
    List<Supplier> findAll();
    Optional<Supplier> findById(Integer id);
    Supplier save(Supplier supplier);
    Supplier update(Supplier supplier);
    void deleteById(Integer id);       // Lógica: cambio de estado
    Supplier restore(Integer id);      // Restaura un proveedor
    List<Supplier> findByState(String state); // Listado por estado: "A" o "I"
    byte[] generateJasperPdfReport() throws Exception; //GEneración de reporte PDF con JasperReports
}



