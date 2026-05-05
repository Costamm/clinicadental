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

    public Turno crearTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) {
        return crearTurno(id, pacienteId, odontologoId, fecha, hora, false);
    }

    public Turno crearTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) {
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        Turno turno = new Turno(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
        return turno;
    }

    public TurnoControl crearTurnoControl(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) {
        return crearTurnoControl(id, pacienteId, odontologoId, fecha, hora, false);
    }

    public TurnoControl crearTurnoControl(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) {
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        TurnoControl turno = new TurnoControl(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
        return turno;
    }

    public TurnoUrgente crearTurnoUrgente(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, String motivoUrgencia) {
        return crearTurnoUrgente(id, pacienteId, odontologoId, fecha, hora, motivoUrgencia, false);
    }

    public TurnoUrgente crearTurnoUrgente(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, String motivoUrgencia, boolean autoAsignado) {
        validarTexto(motivoUrgencia, "El motivo de urgencia es obligatorio.");
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        TurnoUrgente turno = new TurnoUrgente(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE, motivoUrgencia);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
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
        repositorioPaciente.actualizar(turno.getPaciente());
    }

    public void cancelar(Long id) {
        Turno turno = buscarTurnoExistente(id);
        turno.cancelarTurno();
        repositorioTurno.actualizar(turno);
    }

    public void eliminar(Long id) {
        validarId(id, "El id del turno es obligatorio.");
        Turno turno = buscarTurnoExistente(id);
        if (turno.getPaciente() != null) {
            turno.getPaciente().getTurnos().remove(turno);
            repositorioPaciente.actualizar(turno.getPaciente());
        }
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
        if (turno.getEstado() == null) {
            turno.setEstado(EstadoTurno.PENDIENTE);
        }
    }

    private void validarDatosFechaHora(LocalDate fecha, LocalTime hora) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del turno es obligatoria.");
        }
        if (hora == null) {
            throw new IllegalArgumentException("La hora del turno es obligatoria.");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede crear un turno en una fecha pasada.");
        }
    }

    private void validarDisponibilidad(Long turnoIdActual, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        for (Turno turnoRegistrado : repositorioTurno.listarTodos()) {
            boolean mismoTurno = turnoIdActual != null && turnoIdActual.equals(turnoRegistrado.getId());
            boolean turnoCancelado = turnoRegistrado.getEstado() == EstadoTurno.CANCELADO;
            boolean mismoHorario = turnoRegistrado.getFecha().equals(fecha) && turnoRegistrado.getHora().equals(hora);
            if (!mismoTurno && !turnoCancelado && mismoHorario && turnoRegistrado.getPaciente().getId().equals(paciente.getId())) {
                throw new IllegalArgumentException("El paciente ya tiene un turno en ese dia y horario.");
            }
            if (!mismoTurno && !turnoCancelado && mismoHorario && turnoRegistrado.getOdontologo().getId().equals(odontologo.getId())) {
                throw new IllegalArgumentException("El odontologo ya tiene un turno en ese dia y horario.");
            }
        }
    }

    private void sincronizarPacienteTurno(Turno turno, Paciente paciente) {
        turno.setPaciente(paciente);
    }

    private Turno buscarTurnoExistente(Long id) {
        validarId(id, "El id del turno es obligatorio.");
        Turno turno = repositorioTurno.buscarPorId(id);
        if (turno == null) {
            throw new IllegalArgumentException("No existe un turno con id " + id + ".");
        }
        return turno;
    }

    private Paciente buscarPacienteExistente(Long id) {
        validarId(id, "El id del paciente es obligatorio.");
        Paciente paciente = repositorioPaciente.buscarPorId(id);
        if (paciente == null) {
            throw new IllegalArgumentException("No existe un paciente con id " + id + ".");
        }
        return paciente;
    }

    private Odontologo buscarOdontologoExistente(Long id) {
        validarId(id, "El id del odontologo es obligatorio.");
        Odontologo odontologo = repositorioOdontologo.buscarPorId(id);
        if (odontologo == null) {
            throw new IllegalArgumentException("No existe un odontologo con id " + id + ".");
        }
        return odontologo;
    }

    private Paciente validarDatosNuevoTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) {
        validarNoAutoAsignacion(autoAsignado);
        validarId(id, "El id del turno es obligatorio.");
        validarDatosFechaHora(fecha, hora);
        if (repositorioTurno.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un turno con id " + id + ".");
        }

        Paciente paciente = buscarPacienteExistente(pacienteId);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);
        validarDisponibilidad(null, paciente, odontologo, fecha, hora);
        return paciente;
    }

    private void validarNoAutoAsignacion(boolean autoAsignado) {
        if (autoAsignado) {
            throw new IllegalArgumentException("Un paciente no puede autoasignarse turnos.");
        }
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private void validarExistencia(Long id) {
        if (repositorioTurno.buscarPorId(id) == null) {
            throw new IllegalArgumentException("No existe un turno con id " + id + ".");
        }
    }

    private void validarId(Long id, String mensaje) {
        if (id == null) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}