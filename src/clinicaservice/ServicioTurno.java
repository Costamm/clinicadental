package clinicaservice;

import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.turnos.EstadoTurno;
import clinicamodelo.turnos.Turno;
import clinicamodelo.turnos.TurnoControl;
import clinicamodelo.turnos.TurnoUrgente;
import clinicarepository.IRepositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ServicioTurno {
    private final IRepositorio<Turno> repositorioTurno;
    private final IRepositorio<Paciente> repositorioPaciente;
    private final IRepositorio<Odontologo> repositorioOdontologo;

    public ServicioTurno(IRepositorio<Turno> repositorioTurno,
                         IRepositorio<Paciente> repositorioPaciente,
                         IRepositorio<Odontologo> repositorioOdontologo) {
        this.repositorioTurno = repositorioTurno;
        this.repositorioPaciente = repositorioPaciente;
        this.repositorioOdontologo = repositorioOdontologo;
    }

    public Turno crearTurnoControl(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) {
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        TurnoControl turno = new TurnoControl(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        return turno;
    }

    public TurnoUrgente crearTurnoUrgente(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, String motivoUrgencia, boolean autoAsignado) {
        validarTexto(motivoUrgencia, "El motivo de urgencia es obligatorio.");
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        TurnoUrgente turno = new TurnoUrgente(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE, motivoUrgencia);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        return turno;
    }

    public Turno buscarPorId(Long id) {
        validarId(id, "El id del turno es obligatorio.");
        return repositorioTurno.buscarPorId(id);
    }

    public List<Turno> listarTodos() {
        return repositorioTurno.listarTodos();
    }

    public void actualizar(Turno turno) {
        validarTurno(turno);
        validarExistencia(turno.getId());
        validarDisponibilidad(turno.getId(), turno.getPaciente(), turno.getOdontologo(), turno.getFecha(), turno.getHora());
        sincronizarPacienteTurno(turno, turno.getPaciente());
        repositorioTurno.actualizar(turno);
    }

    public void eliminar(Long id) {
        validarId(id, "El id del turno es obligatorio.");
        validarExistencia(id);
        repositorioTurno.eliminar(id);
    }

    private void validarTurno(Turno turno) {
        if (turno == null) {
            throw new IllegalArgumentException("El turno es obligatorio.");
        }
        validarId(turno.getId(), "El id del turno es obligatorio.");
        if (turno.getPaciente() == null || turno.getPaciente().getId() == null) {
            throw new IllegalArgumentException("El paciente del turno es obligatorio.");
        }
        if (turno.getOdontologo() == null || turno.getOdontologo().getId() == null) {
            throw new IllegalArgumentException("El odontologo del turno es obligatorio.");
        }
        buscarPacienteExistente(turno.getPaciente().getId());
        buscarOdontologoExistente(turno.getOdontologo().getId());
        validarDatosFechaHora(turno.getFecha(), turno.getHora());
    }

    private void validarDatosFechaHora(LocalDate fecha, LocalTime hora) {
        if (fecha == null || hora == null) {
            throw new IllegalArgumentException("Fecha y hora son obligatorias.");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede crear un turno en una fecha pasada.");
        }
    }

    private void validarDisponibilidad(Long turnoIdActual, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        for (Turno t : repositorioTurno.listarTodos()) {
            boolean mismoTurno = turnoIdActual != null && turnoIdActual.equals(t.getId());
            boolean mismoHorario = t.getFecha().equals(fecha) && t.getHora().equals(hora);
            if (!mismoTurno && mismoHorario && t.getOdontologo().getId().equals(odontologo.getId())) {
                throw new IllegalArgumentException("El odontologo ya tiene un turno en ese horario.");
            }
        }
    }

    private void sincronizarPacienteTurno(Turno turno, Paciente paciente) {
        turno.setPaciente(paciente); // Esto dispara la sincronización en Turno.java
    }

    private Paciente buscarPacienteExistente(Long id) {
        Paciente p = repositorioPaciente.buscarPorId(id);
        if (p == null) throw new IllegalArgumentException("Paciente no encontrado.");
        return p;
    }

    private Odontologo buscarOdontologoExistente(Long id) {
        Odontologo o = repositorioOdontologo.buscarPorId(id);
        if (o == null) throw new IllegalArgumentException("Odontologo no encontrado.");
        return o;
    }

    private Paciente validarDatosNuevoTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) {
        if (autoAsignado) throw new IllegalArgumentException("Un paciente no puede autoasignarse turnos.");
        validarId(id, "Id obligatorio.");
        validarDatosFechaHora(fecha, hora);
        Paciente p = buscarPacienteExistente(pacienteId);
        Odontologo o = buscarOdontologoExistente(odontologoId);
        validarDisponibilidad(null, p, o, fecha, hora);
        return p;
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) throw new IllegalArgumentException(mensaje);
    }

    private void validarExistencia(Long id) {
        if (repositorioTurno.buscarPorId(id) == null) throw new IllegalArgumentException("Turno no existe.");
    }

    private void validarId(Long id, String mensaje) {
        if (id == null) throw new IllegalArgumentException(mensaje);
    }
}