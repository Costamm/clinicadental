package clinicarepository;

import clinicamodelo.turnos.Turno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioTurno implements IRepositorio<Turno> {
    private final Map<Long, Turno> turnos = new HashMap<>();

    @Override
    public void guardar(Turno turno) {
        validarTurnoConId(turno);
        if (turnos.containsKey(turno.getId())) {
            throw new IllegalArgumentException("Ya existe un turno con id " + turno.getId() + ".");
        }
        turnos.put(turno.getId(), turno);
    }

    @Override
    public Turno buscarPorId(Long id) {
        return turnos.get(id);
    }

    @Override
    public List<Turno> listarTodos() {
        return new ArrayList<>(turnos.values());
    }

    @Override
    public void actualizar(Turno turno) {
        validarTurnoConId(turno);
        validarExistencia(turno.getId());
        turnos.put(turno.getId(), turno);
    }

    @Override
    public void eliminar(Long id) {
        turnos.remove(id);
    }

    private void validarTurnoConId(Turno turno) {
        if (turno == null || turno.getId() == null) {
            throw new IllegalArgumentException("El turno y su id son obligatorios.");
        }
    }

    private void validarExistencia(Long id) {
        if (!turnos.containsKey(id)) {
            throw new IllegalArgumentException("No existe un turno con id " + id + ".");
        }
    }
}