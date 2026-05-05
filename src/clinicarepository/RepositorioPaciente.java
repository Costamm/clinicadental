package clinicarepository;

import clinicamodelo.pacientes.Paciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioPaciente implements IRepositorio<Paciente> {
    private final Map<Long, Paciente> pacientes = new HashMap<>();

    @Override
    public void guardar(Paciente paciente) {
        validarPacienteConId(paciente);
        if (pacientes.containsKey(paciente.getId())) {
            throw new IllegalArgumentException("Ya existe un paciente con id " + paciente.getId() + ".");
        }
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) {
        return pacientes.get(id);
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }

    @Override
    public void actualizar(Paciente paciente) {
        validarPacienteConId(paciente);
        validarExistencia(paciente.getId());
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public void eliminar(Long id) {
        pacientes.remove(id);
    }

    private void validarPacienteConId(Paciente paciente) {
        if (paciente == null || paciente.getId() == null) {
            throw new IllegalArgumentException("El paciente y su id son obligatorios.");
        }
    }

    private void validarExistencia(Long id) {
        if (!pacientes.containsKey(id)) {
            throw new IllegalArgumentException("No existe un paciente con id " + id + ".");
        }
    }
}