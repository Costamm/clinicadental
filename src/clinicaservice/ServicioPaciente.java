package clinicaservice;

import clinicamodelo.pacientes.Paciente;
import clinicarepository.IRepositorio;
import java.util.List;

public class ServicioPaciente {
    private final IRepositorio<Paciente> repositorioPaciente;

    public ServicioPaciente(IRepositorio<Paciente> repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public void guardar(Paciente paciente) {
        validarPaciente(paciente);
        validarDniDisponible(paciente);
        repositorioPaciente.guardar(paciente);
    }

    public Paciente buscarPorId(Long id) {
        validarId(id);
        return repositorioPaciente.buscarPorId(id);
    }

    public List<Paciente> listarTodos() {
        return repositorioPaciente.listarTodos();
    }

    public void actualizar(Paciente paciente) {
        validarPaciente(paciente);
        validarExistencia(paciente.getId());
        validarDniDisponible(paciente);
        repositorioPaciente.actualizar(paciente);
    }

    public void eliminar(Long id) {
        validarId(id);
        validarExistencia(id);
        repositorioPaciente.eliminar(id);
    }

    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente es obligatorio.");
        }
        validarId(paciente.getId());
        validarTexto(paciente.getNombre(), "El nombre del paciente es obligatorio.");
        validarTexto(paciente.getApellido(), "El apellido del paciente es obligatorio.");
        validarTexto(paciente.getDni(), "El DNI del paciente es obligatorio.");
        validarTexto(paciente.getTelefono(), "El telefono del paciente es obligatorio.");
        validarTexto(paciente.getEmail(), "El email del paciente es obligatorio.");
    }

    private void validarDniDisponible(Paciente paciente) {
        for (Paciente pacienteRegistrado : repositorioPaciente.listarTodos()) {
            boolean mismoDni = pacienteRegistrado.getDni().equals(paciente.getDni());
            boolean distintoId = !pacienteRegistrado.getId().equals(paciente.getId());
            if (mismoDni && distintoId) {
                throw new IllegalArgumentException("Ya existe un paciente registrado con DNI " + paciente.getDni() + ".");
            }
        }
    }

    private void validarExistencia(Long id) {
        if (repositorioPaciente.buscarPorId(id) == null) {
            throw new IllegalArgumentException("No existe un paciente con id " + id + ".");
        }
    }

    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del paciente es obligatorio.");
        }
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}