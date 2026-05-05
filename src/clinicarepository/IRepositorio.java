package clinicarepository;

import java.util.List;

public interface IRepositorio<T> {
    boolean guardar(T entidad);

    T buscarPorId(Long id);

    List<T> listarTodos();

    boolean actualizar(T entidad);

    boolean eliminar(Long id);
}
