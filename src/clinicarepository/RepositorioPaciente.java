package clinicarepository;

import clinicamodelo.pacientes.Paciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioPaciente implements IRepositorio<Paciente> {
    private final Map<Long, Paciente> pacientes = new HashMap<>();

    @Override
    public boolean guardar(Paciente paciente) {
        if (!validarPacienteConId(paciente)) {
            return false;
        }
        if (pacientes.containsKey(paciente.getId())) {
            System.out.println("Ya existe un paciente con id " + paciente.getId() + ".");
            return false;
        }
        pacientes.put(paciente.getId(), paciente);
        return true;
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
    public boolean actualizar(Paciente paciente) {
        if (!validarPacienteConId(paciente)) {
            return false;
        }
        if (!validarExistencia(paciente.getId())) {
            return false;
        }
        pacientes.put(paciente.getId(), paciente);
        return true;
    }

    @Override
    public boolean eliminar(Long id) {
        if (!validarExistencia(id)) {
            return false;
        }
        pacientes.remove(id);
        return true;
    }

    private boolean validarPacienteConId(Paciente paciente) {
        if (paciente == null || paciente.getId() == null) {
            System.out.println("El paciente y su id son obligatorios.");
            return false;
        }
        return true;
    }

    private boolean validarExistencia(Long id) {
        if (!pacientes.containsKey(id)) {
            System.out.println("No existe un paciente con id " + id + ".");
            return false;
        }
        return true;
    }
}
