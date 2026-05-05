package clinicaservice;

import clinicamodelo.odontologos.Odontologo;
import clinicarepository.IRepositorio;
import java.util.List;

public class ServicioOdontologo {
    private final IRepositorio<Odontologo> repositorioOdontologo;

    public ServicioOdontologo(IRepositorio<Odontologo> repositorioOdontologo) {
        this.repositorioOdontologo = repositorioOdontologo;
    }

    public void guardar(Odontologo odontologo) {
        validarOdontologo(odontologo);
        validarMatriculaDisponible(odontologo);
        repositorioOdontologo.guardar(odontologo);
    }

    public Odontologo buscarPorId(Long id) {
        validarId(id);
        return repositorioOdontologo.buscarPorId(id);
    }

    public List<Odontologo> listarTodos() {
        return repositorioOdontologo.listarTodos();
    }

    public void actualizar(Odontologo odontologo) {
        validarOdontologo(odontologo);
        validarExistencia(odontologo.getId());
        validarMatriculaDisponible(odontologo);
        repositorioOdontologo.actualizar(odontologo);
    }

    public void eliminar(Long id) {
        validarId(id);
        validarExistencia(id);
        repositorioOdontologo.eliminar(id);
    }

    private void validarOdontologo(Odontologo odontologo) {
        if (odontologo == null) {
            throw new IllegalArgumentException("El odontologo es obligatorio.");
        }
        validarId(odontologo.getId());
        validarTexto(odontologo.getNombre(), "El nombre del odontologo es obligatorio.");
        validarTexto(odontologo.getApellido(), "El apellido del odontologo es obligatorio.");
        validarTexto(odontologo.getMatricula(), "La matricula del odontologo es obligatoria.");
    }

    private void validarMatriculaDisponible(Odontologo odontologo) {
        for (Odontologo odontologoRegistrado : repositorioOdontologo.listarTodos()) {
            boolean mismaMatricula = odontologoRegistrado.getMatricula().equals(odontologo.getMatricula());
            boolean distintoId = !odontologoRegistrado.getId().equals(odontologo.getId());
            if (mismaMatricula && distintoId) {
                throw new IllegalArgumentException("Ya existe un odontologo registrado con matricula " + odontologo.getMatricula() + ".");
            }
        }
    }

    private void validarExistencia(Long id) {
        if (repositorioOdontologo.buscarPorId(id) == null) {
            throw new IllegalArgumentException("No existe un odontologo con id " + id + ".");
        }
    }

    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del odontologo es obligatorio.");
        }
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}