package clinicarepository;

import clinicamodelo.odontologos.Odontologo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioOdontologo implements IRepositorio<Odontologo> {
    private final Map<Long, Odontologo> odontologos = new HashMap<>();

    @Override
    public boolean guardar(Odontologo odontologo) {
        if (!validarOdontologoConId(odontologo)) {
            return false;
        }
        if (odontologos.containsKey(odontologo.getId())) {
            System.out.println("Ya existe un odontologo con id " + odontologo.getId() + ".");
            return false;
        }
        odontologos.put(odontologo.getId(), odontologo);
        return true;
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return odontologos.get(id);
    }

    @Override
    public List<Odontologo> listarTodos() {
        return new ArrayList<>(odontologos.values());
    }

    @Override
    public boolean actualizar(Odontologo odontologo) {
        if (!validarOdontologoConId(odontologo)) {
            return false;
        }
        if (!validarExistencia(odontologo.getId())) {
            return false;
        }
        odontologos.put(odontologo.getId(), odontologo);
        return true;
    }

    @Override
    public boolean eliminar(Long id) {
        if (!validarExistencia(id)) {
            return false;
        }
        odontologos.remove(id);
        return true;
    }

    private boolean validarOdontologoConId(Odontologo odontologo) {
        if (odontologo == null || odontologo.getId() == null) {
            System.out.println("El odontologo y su id son obligatorios.");
            return false;
        }
        return true;
    }

    private boolean validarExistencia(Long id) {
        if (!odontologos.containsKey(id)) {
            System.out.println("No existe un odontologo con id " + id + ".");
            return false;
        }
        return true;
    }
}
