package clinicaservice;

import clinicamodelo.pacientes.Paciente;
import clinicarepository.IRepositorio;
import java.util.List;

public class ServicioPaciente {
    private final IRepositorio<Paciente> repositorioPaciente;

    public ServicioPaciente(IRepositorio<Paciente> repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public boolean guardar(Paciente paciente) {
        if (!validarPaciente(paciente)) {
            return false;
        }
        if (!validarDniDisponible(paciente)) {
            return false;
        }
        return repositorioPaciente.guardar(paciente);
    }

    public Paciente buscarPorId(Long id) {
        if (!validarId(id)) {
            return null;
        }
        return repositorioPaciente.buscarPorId(id);
    }

    public List<Paciente> listarTodos() {
        return repositorioPaciente.listarTodos();
    }

    public boolean actualizar(Paciente paciente) {
        if (!validarPaciente(paciente)) {
            return false;
        }
        if (!validarExistencia(paciente.getId())) {
            return false;
        }
        if (!validarDniDisponible(paciente)) {
            return false;
        }
        return repositorioPaciente.actualizar(paciente);
    }

    public boolean eliminar(Long id) {
        if (!validarId(id)) {
            return false;
        }
        if (!validarExistencia(id)) {
            return false;
        }
        return repositorioPaciente.eliminar(id);
    }

    private boolean validarPaciente(Paciente paciente) {
        if (paciente == null) {
            System.out.println("El paciente es obligatorio.");
            return false;
        }
        if (!validarId(paciente.getId())) {
            return false;
        }
        if (!validarTexto(paciente.getNombre(), "El nombre del paciente es obligatorio.")) {
            return false;
        }
        if (!validarTexto(paciente.getApellido(), "El apellido del paciente es obligatorio.")) {
            return false;
        }
        if (!validarTexto(paciente.getDni(), "El DNI del paciente es obligatorio.")) {
            return false;
        }
        if (!validarTexto(paciente.getTelefono(), "El telefono del paciente es obligatorio.")) {
            return false;
        }
        return validarTexto(paciente.getEmail(), "El email del paciente es obligatorio.");
    }

    private boolean validarDniDisponible(Paciente paciente) {
        for (Paciente pacienteRegistrado : repositorioPaciente.listarTodos()) {
            boolean mismoDni = pacienteRegistrado.getDni().equals(paciente.getDni());
            boolean distintoId = !pacienteRegistrado.getId().equals(paciente.getId());
            if (mismoDni && distintoId) {
                System.out.println("Ya existe un paciente registrado con DNI " + paciente.getDni() + ".");
                return false;
            }
        }
        return true;
    }

    private boolean validarExistencia(Long id) {
        if (repositorioPaciente.buscarPorId(id) == null) {
            System.out.println("No existe un paciente con id " + id + ".");
            return false;
        }
        return true;
    }

    private boolean validarId(Long id) {
        if (id == null) {
            System.out.println("El id del paciente es obligatorio.");
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
