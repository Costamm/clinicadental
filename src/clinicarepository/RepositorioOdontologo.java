package clinicarepository;

import clinicamodelo.odontologos.Odontologo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioOdontologo implements IRepositorio<Odontologo> {
    private final Map<Long, Odontologo> odontologos = new HashMap<>();

    @Override
    public void guardar(Odontologo odontologo) {
        validarOdontologoConId(odontologo);
        if (odontologos.containsKey(odontologo.getId())) {
            throw new IllegalArgumentException("Ya existe un odontologo con id " + odontologo.getId() + ".");
        }
        odontologos.put(odontologo.getId(), odontologo);
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
    public void actualizar(Odontologo odontologo) {
        validarOdontologoConId(odontologo);
        validarExistencia(odontologo.getId());
        odontologos.put(odontologo.getId(), odontologo);
    }

    @Override
    public void eliminar(Long id) {
        odontologos.remove(id);
    }

    private void validarOdontologoConId(Odontologo odontologo) {
        if (odontologo == null || odontologo.getId() == null) {
            throw new IllegalArgumentException("El odontologo y su id son obligatorios.");
        }
    }

    private void validarExistencia(Long id) {
        if (!odontologos.containsKey(id)) {
            throw new IllegalArgumentException("No existe un odontologo con id " + id + ".");
        }
    }
}