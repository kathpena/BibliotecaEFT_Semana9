package dao;

import java.util.List;

public interface CrudDAO <T>{

    boolean insertar(T objeto);
    List<T> listar();
    T buscarPorId(int id);
    boolean actualizar(T objeto);
    boolean eliminar(int id);
}
