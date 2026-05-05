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
        if (paciente == null || odontologo == null) {
            return null;
        }

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
        if (paciente == null || odontologo == null) {
            return null;
        }

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
        if (!validarTexto(motivoUrgencia, "El motivo de urgencia es obligatorio.")) {
            return null;
        }
        Paciente paciente = validarDatosNuevoTurno(id, pacienteId, odontologoId, fecha, hora, autoAsignado);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);
        if (paciente == null || odontologo == null) {
            return null;
        }

        TurnoUrgente turno = new TurnoUrgente(id, null, odontologo, fecha, hora, EstadoTurno.PENDIENTE, motivoUrgencia);
        sincronizarPacienteTurno(turno, paciente);
        repositorioTurno.guardar(turno);
        repositorioPaciente.actualizar(paciente);
        return turno;
    }

    public Turno buscarPorId(Long id) {
        if (!validarId(id, "El id del turno es obligatorio.")) {
            return null;
        }
        return repositorioTurno.buscarPorId(id);
    }

    public List<Turno> listarTodos() {
        return repositorioTurno.listarTodos();
    }

    public boolean actualizar(Turno turno) {
        if (!validarTurno(turno)) {
            return false;
        }
        if (!validarExistencia(turno.getId())) {
            return false;
        }
        if (!validarDisponibilidad(turno.getId(), turno.getPaciente(), turno.getOdontologo(), turno.getFecha(), turno.getHora())) {
            return false;
        }
        sincronizarPacienteTurno(turno, turno.getPaciente());
        repositorioTurno.actualizar(turno);
        repositorioPaciente.actualizar(turno.getPaciente());
        return true;
    }

    public boolean cancelar(Long id) {
        Turno turno = buscarTurnoExistente(id);
        if (turno == null) {
            return false;
        }
        turno.cancelarTurno();
        return repositorioTurno.actualizar(turno);
    }

    public boolean eliminar(Long id) {
        if (!validarId(id, "El id del turno es obligatorio.")) {
            return false;
        }
        Turno turno = buscarTurnoExistente(id);
        if (turno == null) {
            return false;
        }
        if (turno.getPaciente() != null) {
            turno.getPaciente().getTurnos().remove(turno);
            repositorioPaciente.actualizar(turno.getPaciente());
        }
        return repositorioTurno.eliminar(id);
    }

    private boolean validarTurno(Turno turno) {
        if (turno == null) {
            System.out.println("El turno es obligatorio.");
            return false;
        }
        if (!validarId(turno.getId(), "El id del turno es obligatorio.")) {
            return false;
        }
        if (turno.getPaciente() == null || turno.getPaciente().getId() == null) {
            System.out.println("El paciente del turno es obligatorio.");
            return false;
        }
        if (turno.getOdontologo() == null || turno.getOdontologo().getId() == null) {
            System.out.println("El odontologo del turno es obligatorio.");
            return false;
        }
        if (buscarPacienteExistente(turno.getPaciente().getId()) == null) {
            return false;
        }
        if (buscarOdontologoExistente(turno.getOdontologo().getId()) == null) {
            return false;
        }
        if (!validarDatosFechaHora(turno.getFecha(), turno.getHora())) {
            return false;
        }
        if (turno.getEstado() == null) {
            turno.setEstado(EstadoTurno.PENDIENTE);
        }
        return true;
    }

    private boolean validarDatosFechaHora(LocalDate fecha, LocalTime hora) {
        if (fecha == null) {
            System.out.println("La fecha del turno es obligatoria.");
            return false;
        }
        if (hora == null) {
            System.out.println("La hora del turno es obligatoria.");
            return false;
        }
        if (fecha.isBefore(LocalDate.now())) {
            System.out.println("No se puede crear un turno en una fecha pasada.");
            return false;
        }
        return true;
    }

    private boolean validarDisponibilidad(Long turnoIdActual, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        for (Turno turnoRegistrado : repositorioTurno.listarTodos()) {
            boolean mismoTurno = turnoIdActual != null && turnoIdActual.equals(turnoRegistrado.getId());
            boolean turnoCancelado = turnoRegistrado.getEstado() == EstadoTurno.CANCELADO;
            boolean mismoHorario = turnoRegistrado.getFecha().equals(fecha) && turnoRegistrado.getHora().equals(hora);
            if (!mismoTurno && !turnoCancelado && mismoHorario && turnoRegistrado.getPaciente().getId().equals(paciente.getId())) {
                System.out.println("El paciente ya tiene un turno en ese dia y horario.");
                return false;
            }
            if (!mismoTurno && !turnoCancelado && mismoHorario && turnoRegistrado.getOdontologo().getId().equals(odontologo.getId())) {
                System.out.println("El odontologo ya tiene un turno en ese dia y horario.");
                return false;
            }
        }
        return true;
    }

    private void sincronizarPacienteTurno(Turno turno, Paciente paciente) {
        turno.setPaciente(paciente);
    }

    private Turno buscarTurnoExistente(Long id) {
        if (!validarId(id, "El id del turno es obligatorio.")) {
            return null;
        }
        Turno turno = repositorioTurno.buscarPorId(id);
        if (turno == null) {
            System.out.println("No existe un turno con id " + id + ".");
        }
        return turno;
    }

    private Paciente buscarPacienteExistente(Long id) {
        if (!validarId(id, "El id del paciente es obligatorio.")) {
            return null;
        }
        Paciente paciente = repositorioPaciente.buscarPorId(id);
        if (paciente == null) {
            System.out.println("No existe un paciente con id " + id + ".");
        }
        return paciente;
    }

    private Odontologo buscarOdontologoExistente(Long id) {
        if (!validarId(id, "El id del odontologo es obligatorio.")) {
            return null;
        }
        Odontologo odontologo = repositorioOdontologo.buscarPorId(id);
        if (odontologo == null) {
            System.out.println("No existe un odontologo con id " + id + ".");
        }
        return odontologo;
    }

    private Paciente validarDatosNuevoTurno(Long id, Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora, boolean autoAsignado) {
        if (!validarNoAutoAsignacion(autoAsignado)) {
            return null;
        }
        if (!validarId(id, "El id del turno es obligatorio.")) {
            return null;
        }
        if (!validarDatosFechaHora(fecha, hora)) {
            return null;
        }
        if (repositorioTurno.buscarPorId(id) != null) {
            System.out.println("Ya existe un turno con id " + id + ".");
            return null;
        }

        Paciente paciente = buscarPacienteExistente(pacienteId);
        Odontologo odontologo = buscarOdontologoExistente(odontologoId);
        if (paciente == null || odontologo == null) {
            return null;
        }
        if (!validarDisponibilidad(null, paciente, odontologo, fecha, hora)) {
            return null;
        }
        return paciente;
    }

    private boolean validarNoAutoAsignacion(boolean autoAsignado) {
        if (autoAsignado) {
            System.out.println("Un paciente no puede autoasignarse turnos.");
            return false;
        }
        return true;
    }

    private boolean validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            System.out.println(mensaje);
            return false;
        }
        return true;
    }

    private boolean validarExistencia(Long id) {
        if (repositorioTurno.buscarPorId(id) == null) {
            System.out.println("No existe un turno con id " + id + ".");
            return false;
        }
        return true;
    }

    private boolean validarId(Long id, String mensaje) {
        if (id == null) {
            System.out.println(mensaje);
            return false;
        }
        return true;
    }
}
