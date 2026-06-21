package clinicaservice;

import clinicamodelo.pacientes.Paciente;
import clinicarepository.IRepositorio;
import clinicaexception.PacienteNoEncontradoException;
import clinicaexception.DatoInvalidoException;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioPaciente {
    private final IRepositorio<Paciente> repositorioPaciente;

    public ServicioPaciente(IRepositorio<Paciente> repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    public boolean guardar(Paciente paciente) throws DatoInvalidoException {
        validarPaciente(paciente);
        validarDniDisponible(paciente);
        return repositorioPaciente.guardar(paciente);
    }

    public Paciente buscarPorId(Long id) throws PacienteNoEncontradoException, DatoInvalidoException {
        if (id == null) {
            throw new DatoInvalidoException("El id del paciente es obligatorio.");
        }
        Paciente paciente = repositorioPaciente.buscarPorId(id);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con id " + id + ".");
        }
        return paciente;
    }

    // Devuelve true si ya existe un paciente con ese ID, false si no.
    // Lo usamos en la GUI para decidir entre guardar (nuevo) o actualizar (existente),
    // sin lanzar excepción como buscarPorId.
    public boolean existePorId(Long id) {
        if (id == null) {
            return false;
        }
        return repositorioPaciente.buscarPorId(id) != null;
    }

    public List<Paciente> listarTodos() {
        return repositorioPaciente.listarTodos();
    }

    public Paciente buscarPorDni(String dni) throws PacienteNoEncontradoException, DatoInvalidoException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new DatoInvalidoException("El DNI es obligatorio.");
        }
        return repositorioPaciente.listarTodos().stream()
                .filter(p -> p.getDni().equals(dni.trim()))
                .findFirst()
                .orElseThrow(() -> new PacienteNoEncontradoException("No existe un paciente con DNI " + dni + "."));
    }

    public List<Paciente> listarOrdenadosPorApellido() {
        return repositorioPaciente.listarTodos().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean actualizar(Paciente paciente) throws PacienteNoEncontradoException, DatoInvalidoException {
        validarPaciente(paciente);

        if (repositorioPaciente.buscarPorId(paciente.getId()) == null) {
            throw new PacienteNoEncontradoException("No se puede actualizar: No existe un paciente con id " + paciente.getId() + ".");
        }

        validarDniDisponible(paciente);
        return repositorioPaciente.actualizar(paciente);
    }

    public boolean eliminar(Long id) throws PacienteNoEncontradoException, DatoInvalidoException {
        if (id == null) {
            throw new DatoInvalidoException("El id del paciente es obligatorio.");
        }
        if (repositorioPaciente.buscarPorId(id) == null) {
            throw new PacienteNoEncontradoException("No se puede eliminar: No existe un paciente con id " + id + ".");
        }
        return repositorioPaciente.eliminar(id);
    }

    private void validarPaciente(Paciente paciente) throws DatoInvalidoException {
        if (paciente == null) {
            throw new DatoInvalidoException("El paciente es obligatorio.");
        }
        if (paciente.getId() == null) {
            throw new DatoInvalidoException("El id del paciente es obligatorio.");
        }
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del paciente es obligatorio.");
        }
        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
            throw new DatoInvalidoException("El apellido del paciente es obligatorio.");
        }
        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            throw new DatoInvalidoException("El DNI del paciente es obligatorio.");
        }
        if (paciente.getTelefono() == null || paciente.getTelefono().trim().isEmpty()) {
            throw new DatoInvalidoException("El telefono del paciente es obligatorio.");
        }
        if (paciente.getEmail() == null || paciente.getEmail().trim().isEmpty()) {
            throw new DatoInvalidoException("El email del paciente es obligatorio.");
        }
    }

    private void validarDniDisponible(Paciente paciente) throws DatoInvalidoException {
        for (Paciente pacienteRegistrado : repositorioPaciente.listarTodos()) {
            boolean mismoDni = pacienteRegistrado.getDni().equals(paciente.getDni());
            boolean distintoId = !pacienteRegistrado.getId().equals(paciente.getId());
            if (mismoDni && distintoId) {
                throw new DatoInvalidoException("Ya existe un paciente registrado con DNI " + paciente.getDni() + ".");
            }
        }
    }

}