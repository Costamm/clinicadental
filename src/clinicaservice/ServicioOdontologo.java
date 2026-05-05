package clinicaservice;

import clinicamodelo.odontologos.Odontologo;
import clinicarepository.IRepositorio;
import java.util.List;

public class ServicioOdontologo {
    private final IRepositorio<Odontologo> repositorioOdontologo;

    public ServicioOdontologo(IRepositorio<Odontologo> repositorioOdontologo) {
        this.repositorioOdontologo = repositorioOdontologo;
    }

    public boolean guardar(Odontologo odontologo) {
        if (!validarOdontologo(odontologo)) {
            return false;
        }
        if (!validarMatriculaDisponible(odontologo)) {
            return false;
        }
        return repositorioOdontologo.guardar(odontologo);
    }

    public Odontologo buscarPorId(Long id) {
        if (!validarId(id)) {
            return null;
        }
        return repositorioOdontologo.buscarPorId(id);
    }

    public List<Odontologo> listarTodos() {
        return repositorioOdontologo.listarTodos();
    }

    public boolean actualizar(Odontologo odontologo) {
        if (!validarOdontologo(odontologo)) {
            return false;
        }
        if (!validarExistencia(odontologo.getId())) {
            return false;
        }
        if (!validarMatriculaDisponible(odontologo)) {
            return false;
        }
        return repositorioOdontologo.actualizar(odontologo);
    }

    public boolean eliminar(Long id) {
        if (!validarId(id)) {
            return false;
        }
        if (!validarExistencia(id)) {
            return false;
        }
        return repositorioOdontologo.eliminar(id);
    }

    private boolean validarOdontologo(Odontologo odontologo) {
        if (odontologo == null) {
            System.out.println("El odontologo es obligatorio.");
            return false;
        }
        if (!validarId(odontologo.getId())) {
            return false;
        }
        if (!validarTexto(odontologo.getNombre(), "El nombre del odontologo es obligatorio.")) {
            return false;
        }
        if (!validarTexto(odontologo.getApellido(), "El apellido del odontologo es obligatorio.")) {
            return false;
        }
        return validarTexto(odontologo.getMatricula(), "La matricula del odontologo es obligatoria.");
    }

    private boolean validarMatriculaDisponible(Odontologo odontologo) {
        for (Odontologo odontologoRegistrado : repositorioOdontologo.listarTodos()) {
            boolean mismaMatricula = odontologoRegistrado.getMatricula().equals(odontologo.getMatricula());
            boolean distintoId = !odontologoRegistrado.getId().equals(odontologo.getId());
            if (mismaMatricula && distintoId) {
                System.out.println("Ya existe un odontologo registrado con matricula " + odontologo.getMatricula() + ".");
                return false;
            }
        }
        return true;
    }

    private boolean validarExistencia(Long id) {
        if (repositorioOdontologo.buscarPorId(id) == null) {
            System.out.println("No existe un odontologo con id " + id + ".");
            return false;
        }
        return true;
    }

    private boolean validarId(Long id) {
        if (id == null) {
            System.out.println("El id del odontologo es obligatorio.");
            return false;
        }
        return true;
    }

    private boolean validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            System.out.println(mensaje);
            return false;
        }
        return true;
    }
}
