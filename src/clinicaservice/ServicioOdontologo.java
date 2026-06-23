package clinicaservice;

import clinicamodelo.odontologos.Odontologo;
import clinicarepository.IRepositorio;
import clinicaexception.OdontologoNoEncontradoException;
import clinicaexception.DatoInvalidoException;
import java.util.List;

public class ServicioOdontologo {
    private final IRepositorio<Odontologo> repositorioOdontologo;

    public ServicioOdontologo(IRepositorio<Odontologo> repositorioOdontologo) {
        this.repositorioOdontologo = repositorioOdontologo;
    }

    public boolean guardar(Odontologo odontologo) throws DatoInvalidoException {
        validarOdontologo(odontologo);
        validarMatriculaDisponible(odontologo);
        return repositorioOdontologo.guardar(odontologo);
    }

    public Odontologo buscarPorId(Long id) throws OdontologoNoEncontradoException, DatoInvalidoException {
        if (id == null) {
            throw new DatoInvalidoException("El id del odontologo es obligatorio.");
        }
        Odontologo odontologo = repositorioOdontologo.buscarPorId(id);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con id " + id + ".");
        }
        return odontologo;
    }

    public boolean existePorId(Long id) {
        if (id == null) {
            return false;
        }
        return repositorioOdontologo.buscarPorId(id) != null;
    }

    public List<Odontologo> listarTodos() {
        return repositorioOdontologo.listarTodos();
    }

    public boolean actualizar(Odontologo odontologo) throws OdontologoNoEncontradoException, DatoInvalidoException {
        validarOdontologo(odontologo);

        if (repositorioOdontologo.buscarPorId(odontologo.getId()) == null) {
            throw new OdontologoNoEncontradoException("No se puede actualizar: No existe un odontologo con id " + odontologo.getId() + ".");
        }

        validarMatriculaDisponible(odontologo);
        return repositorioOdontologo.actualizar(odontologo);
    }

    public boolean eliminar(Long id) throws OdontologoNoEncontradoException, DatoInvalidoException {
        if (id == null) {
            throw new DatoInvalidoException("El id del odontologo es obligatorio.");
        }
        if (repositorioOdontologo.buscarPorId(id) == null) {
            throw new OdontologoNoEncontradoException("No se puede eliminar: No existe un odontologo con id " + id + ".");
        }
        return repositorioOdontologo.eliminar(id);
    }

    private void validarOdontologo(Odontologo odontologo) throws DatoInvalidoException {
        if (odontologo == null) {
            throw new DatoInvalidoException("El odontologo es obligatorio.");
        }
        if (odontologo.getId() == null) {
            throw new DatoInvalidoException("El id del odontologo es obligatorio.");
        }
        if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del odontologo es obligatorio.");
        }
        if (odontologo.getApellido() == null || odontologo.getApellido().trim().isEmpty()) {
            throw new DatoInvalidoException("El apellido del odontologo es obligatorio.");
        }
        if (odontologo.getMatricula() == null || odontologo.getMatricula().trim().isEmpty()) {
            throw new DatoInvalidoException("La matricula del odontologo es obligatoria.");
        }
    }

    private void validarMatriculaDisponible(Odontologo odontologo) throws DatoInvalidoException {
        for (Odontologo odontologoRegistrado : repositorioOdontologo.listarTodos()) {
            boolean mismaMatricula = odontologoRegistrado.getMatricula().equals(odontologo.getMatricula());
            boolean distintoId = !odontologoRegistrado.getId().equals(odontologo.getId());
            if (mismaMatricula && distintoId) {
                throw new DatoInvalidoException("Ya existe un odontologo registrado con matricula " + odontologo.getMatricula() + ".");
            }
        }
    }
}