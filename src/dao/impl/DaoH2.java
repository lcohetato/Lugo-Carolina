package dao.impl;

import dB.H2Connection;
import dao.IDao;
import model.Tratamiento;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoH2 implements IDao<Tratamiento> {

    public  static final Logger logger= Logger.getLogger(DaoH2.class);
    public static final String INSERT ="INSERT INTO TRATAMIENTOS VALUES (DEFAULT,?,?,?)";
    public static String SELECT_ALL = "SELECT * FROM TRATAMIENTOS";

    @Override
    public Tratamiento guardar(Tratamiento tratamiento) {
        Connection connection=null;
        Tratamiento tratamientoARetornar=null;

        try {
            connection= H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement=connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,tratamiento.getCodigo());
            preparedStatement.setString(2, tratamiento.getDescripcion());
            preparedStatement.setDouble(3,tratamiento.getPrecio());
            preparedStatement.executeUpdate();

            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                Integer idDesdeDB =resultSet.getInt(1);
                tratamientoARetornar= new Tratamiento(idDesdeDB, tratamiento.getCodigo(), tratamiento.getDescripcion(), tratamiento.getPrecio());
            }

            logger.info("tratamiento guardado correctamente en base de datos: " + tratamientoARetornar);

            connection.commit();

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return tratamientoARetornar;
    }

    @Override
    public List<Tratamiento> listarTodos() {
        Connection connection=null;
        List<Tratamiento> tratamientos= new ArrayList<>();
        Tratamiento tratamientoDesdeDb= null;
        try {
            connection= H2Connection.getConnection();
            Statement statement= connection.createStatement();
            ResultSet resultSet= statement.executeQuery(SELECT_ALL);

            while(resultSet.next()) {
                Integer id =resultSet.getInt(1);
                Integer codigo =resultSet.getInt(2);
                String descripcion=resultSet.getString(3);
                double  precio=resultSet.getDouble(4);
                tratamientoDesdeDb= new Tratamiento(id, codigo,descripcion,precio);
                tratamientos.add(tratamientoDesdeDb);
                logger.info("tratamiento"+ tratamientoDesdeDb);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return tratamientos;
    }
}
