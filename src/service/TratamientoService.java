package service;

import dao.IDao;
import model.Tratamiento;
import java.util.List;

public class TratamientoService{
    private IDao<Tratamiento> tratamientoIDao;

    public TratamientoService(IDao<Tratamiento> tratamientoIDao){
        this.tratamientoIDao= tratamientoIDao;
    }
    public Tratamiento guardarTratamiento(Tratamiento tratamiento){
        return tratamientoIDao.guardar(tratamiento);
    }

    public List<Tratamiento> listarTodos(){
        return tratamientoIDao.listarTodos();
    }

}
