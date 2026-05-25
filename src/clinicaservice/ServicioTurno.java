package clinicaservice;

import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.turnos.EstadoTurno;
import clinicamodelo.turnos.Turno;
import clinicamodelo.turnos.TurnoControl;
import clinicamodelo.turnos.TurnoUrgente;
import clinicarepository.IRepositorio;
import clinicaexception.ClinicaException;
import clinicaexception.DatoInvalidoException;
import clinicaexception.TurnoYaReservadoException;
import clinicaexception.PacienteNoEncontradoException;
import clinicaexception.OdontologoNoEncontradoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public Turno crearTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) throws ClinicaException {
        return crearTurno(id, pacienteId, odontologoId, fecha, hora, false);
    }

    public Turno crearTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) throws ClinicaException {
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        Turno turno = new Turno(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
        return turno;
    }

    public TurnoControl crearTurnoControl(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) throws ClinicaException {
        return crearTurnoControl(id, pacienteId, odontologoId, fecha, hora, false);
    }

    public TurnoControl crearTurnoControl(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) throws ClinicaException {
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        TurnoControl turno = new TurnoControl(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
        return turno;
    }

    public TurnoUrgente crearTurnoUrgente(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, String motivoUrgencia) throws ClinicaException {
        return crearTurnoUrgente(id, pacienteId, odontologoId, fecha, hora, motivoUrgencia, false);
    }

    public TurnoUrgente crearTurnoUrgente(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, String motivoUrgencia, boolean autoAsAssignado) throws ClinicaException {
        if (motivoUrgencia == null || motivoUrgencia.trim().isEmpty()) {
            throw new DatoInvalidoException("El motivo de urgencia es obligatorio.");
        }
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsAssignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        TurnoUrgente turno = new TurnoUrgente(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE, motivoUrgencia);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
        return turno;
    }

    public Turno buscarPorId(Long id) throws ClinicaException {
        if (id == null) {
            throw new DatoInvalidoException("El id del turno es obligatorio.");
        }
        Turno turno = repositorioTurno.buscarPorId(id);
        if (turno == null) {
            throw new DatoInvalidoException("No existe un turno con id " + id + ".");
        }
        return turno;
    }

    public List<Turno> listarTodos() {
        return repositorioTurno.listarTodos();
    }

    public List<Turno> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws DatoInvalidoException {
        if (fechaInicio == null || fechaFin == null) {
            throw new DatoInvalidoException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new DatoInvalidoException("La fecha de inicio no puede ser posterior a la fecha fin.");
        }
        return repositorioTurno.listarTodos().stream()
                .filter(t -> !t.getFecha().isBefore(fechaInicio) && !t.getFecha().isAfter(fechaFin))
                .sorted((a, b) -> {
                    int cmpFecha = a.getFecha().compareTo(b.getFecha());
                    return cmpFecha != 0 ? cmpFecha : a.getHora().compareTo(b.getHora());
                })
                .collect(Collectors.toList());
    }

    public List<Turno> filtrarPorOdontologo(Long odontologoId) throws DatoInvalidoException {
        if (odontologoId == null) {
            throw new DatoInvalidoException("El id del odontologo es obligatorio.");
        }
        return repositorioTurno.listarTodos().stream()
                .filter(t -> t.getOdontologo().getId().equals(odontologoId))
                .collect(Collectors.toList());
    }

    public List<Turno> filtrarPorPaciente(Long pacienteId) throws DatoInvalidoException {
        if (pacienteId == null) {
            throw new DatoInvalidoException("El id del paciente es obligatorio.");
        }
        return repositorioTurno.listarTodos().stream()
                .filter(t -> t.getPaciente() != null && t.getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    public boolean actualizar(Turno turno) throws ClinicaException {
        validarTurno(turno);
        if (repositorioTurno.buscarPorId(turno.getId()) == null) {
            throw new DatoInvalidoException("No existe un turno con id " + turno.getId() + ".");
        }
        validarDisponibilidad(turno.getId(), turno.getPaciente(), turno.getOdontologo(), turno.getFecha(), turno.getHora());

        sincronizarPacienteTurno(turno, turno.getPaciente());
        repositorioTurno.actualizar(turno);
        repositorioPaciente.actualizar(turno.getPaciente());
        return true;
    }

    public boolean cancelar(Long id) throws ClinicaException {
        Turno turno = buscarTurnoExistente(id);
        turno.cancelarTurno();
        return repositorioTurno.actualizar(turno);
    }

    public boolean eliminar(Long id) throws ClinicaException {
        if (id == null) {
            throw new DatoInvalidoException("El id del turno es obligatorio.");
        }
        Turno turno = buscarTurnoExistente(id);
        if (turno.getPaciente() != null) {
            turno.getPaciente().getTurnos().remove(turno);
            repositorioPaciente.actualizar(turno.getPaciente());
        }
        return repositorioTurno.eliminar(id);
    }

    private void validarTurno(Turno turno) throws ClinicaException {
        if (turno == null) {
            throw new DatoInvalidoException("El turno es obligatorio.");
        }
        if (turno.getId() == null) {
            throw new DatoInvalidoException("El id del turno es obligatorio.");
        }
        if (turno.getPaciente() == null || turno.getPaciente().getId() == null) {
            throw new DatoInvalidoException("El paciente del turno es obligatorio.");
        }
        if (turno.getOdontologo() == null || turno.getOdontologo().getId() == null) {
            throw new DatoInvalidoException("El odontologo del turno es obligatorio.");
        }

        buscarPacienteExistente(turno.getPaciente().getId());
        buscarOdontologoExistente(turno.getOdontologo().getId());
        validarDatosFechaHora(turno.getFecha(), turno.getHora());

        if (turno.getEstado() == null) {
            turno.setEstado(EstadoTurno.PENDIENTE);
        }
    }

    private void validarDatosFechaHora(LocalDate fecha, LocalTime hora) throws DatoInvalidoException {
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha del turno es obligatoria.");
        }
        if (hora == null) {
            throw new DatoInvalidoException("La hora del turno es obligatoria.");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("No se puede crear un turno en una fecha pasada.");
        }
    }

    private void validarDisponibilidad(Long turnoIdActual, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora) throws TurnoYaReservadoException {
        for (Turno turnoRegistrado : repositorioTurno.listarTodos()) {
            boolean mismoTurno = turnoIdActual != null && turnoIdActual.equals(turnoRegistrado.getId());
            boolean turnoCancelado = turnoRegistrado.getEstado() == EstadoTurno.CANCELADO;
            boolean mismoHorario = turnoRegistrado.getFecha().equals(fecha) && turnoRegistrado.getHora().equals(hora);

            if (!mismoTurno && !turnoCancelado && mismoHorario) {
                if (turnoRegistrado.getPaciente().getId().equals(paciente.getId())) {
                    throw new TurnoYaReservadoException("El paciente ya tiene un turno asignado el día " + fecha + " a las " + hora + ".");
                }
                if (turnoRegistrado.getOdontologo().getId().equals(odontologo.getId())) {
                    throw new TurnoYaReservadoException("El odontólogo ya tiene un turno reservado el día " + fecha + " a las " + hora + ".");
                }
            }
        }
    }

    private void sincronizarPacienteTurno(Turno turno, Paciente paciente) {
        turno.setPaciente(paciente);
    }

    private Turno buscarTurnoExistente(Long id) throws ClinicaException {
        if (id == null) {
            throw new DatoInvalidoException("El id del turno es obligatorio.");
        }
        Turno turno = repositorioTurno.buscarPorId(id);
        if (turno == null) {
            throw new DatoInvalidoException("No existe un turno con id " + id + ".");
        }
        return turno;
    }

    private Paciente buscarPacienteExistente(Long id) throws ClinicaException {
        if (id == null) {
            throw new DatoInvalidoException("El id del paciente es obligatorio.");
        }
        Paciente paciente = repositorioPaciente.buscarPorId(id);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No existe un paciente con id " + id + ".");
        }
        return paciente;
    }

    private Odontologo buscarOdontologoExistente(Long id) throws ClinicaException {
        if (id == null) {
            throw new DatoInvalidoException("El id del odontologo es obligatorio.");
        }
        Odontologo odontologo = repositorioOdontologo.buscarPorId(id);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No existe un odontologo con id " + id + ".");
        }
        return odontologo;
    }

    private Paciente validarDatosNuevoTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) throws ClinicaException {
        if (autoAsignado) {
            throw new DatoInvalidoException("Un paciente no puede autoasignarse turnos.");
        }
        if (id == null) {
            throw new DatoInvalidoException("El id del turno es obligatorio.");
        }
        validarDatosFechaHora(fecha, hora);

        if (repositorioTurno.buscarPorId(id) != null) {
            throw new DatoInvalidoException("Ya existe un turno con id " + id + ".");
        }

        Paciente paciente = buscarPacienteExistente(pacienteId);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);

        validarDisponibilidad(null, paciente, odontologo, fecha, hora);
        return paciente;
    }
}