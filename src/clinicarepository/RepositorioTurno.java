package clinicarepository;

import clinicamodelo.turnos.Turno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioTurno implements IRepositorio<Turno> {
    private final Map<Long, Turno> turnos = new HashMap<>();

    @Override
    public boolean guardar(Turno turno) {
        if (!validarTurnoConId(turno)) {
            return false;
        }
        if (turnos.containsKey(turno.getId())) {
            System.out.println("Ya existe un turno con id " + turno.getId() + ".");
            return false;
        }
        turnos.put(turno.getId(), turno);
        return true;
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
    public boolean actualizar(Turno turno) {
        if (!validarTurnoConId(turno)) {
            return false;
        }
        if (!validarExistencia(turno.getId())) {
            return false;
        }
        turnos.put(turno.getId(), turno);
        return true;
    }

    @Override
    public boolean eliminar(Long id) {
        if (!validarExistencia(id)) {
            return false;
        }
        turnos.remove(id);
        return true;
    }

    private boolean validarTurnoConId(Turno turno) {
        if (turno == null || turno.getId() == null) {
            System.out.println("El turno y su id son obligatorios.");
            return false;
        }
        return true;
    }

    private boolean validarExistencia(Long id) {
        if (!turnos.containsKey(id)) {
            System.out.println("No existe un turno con id " + id + ".");
            return false;
        }
        return true;
    }
}
