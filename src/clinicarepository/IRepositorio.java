package clinicarepository;

import java.util.List;

public interface IRepositorio<T> {
    void guardar(T entidad);

    T buscarPorId(Long id);

    List<T> listarTodos();

    void actualizar(T entidad);

    void eliminar(Long id);
}