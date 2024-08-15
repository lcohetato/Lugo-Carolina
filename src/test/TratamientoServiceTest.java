package test;

import dao.impl.DaoH2;
import model.Tratamiento;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.TratamientoService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TratamientoServiceTest {
    private static final Logger logger = Logger.getLogger(TratamientoServiceTest.class);
    private static TratamientoService tratamientoService = new TratamientoService(new DaoH2());

    @BeforeAll
    static void crearTabla() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./tratamientosDb;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }

    }

    @Test
    @DisplayName("Guardar correctamente un tratamiento")
    void caso1() {
        //DADO
        Tratamiento tratamiento = new Tratamiento(4325, "CITA ODONTOLOGICA", 310);
        //CUANDO
        Tratamiento tratamientoDesdeBD = tratamientoService.guardarTratamiento(tratamiento);
        //ENTONCES
        assertNotNull(tratamientoDesdeBD);
    }
    @Test
    @DisplayName("Testear que se listen todos los tratamientos")
    void caso2() {
        //DADO
        List<Tratamiento> tratamientos;
        //CUANDO
        tratamientos = tratamientoService.listarTodos();
        //ENTONCES
        assertNotNull(tratamientos);
    }
}