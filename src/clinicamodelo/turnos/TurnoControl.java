package clinicamodelo.turnos;

import clinicamodelo.pacientes.Paciente;
import clinicamodelo.odontologos.Odontologo;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoControl extends Turno {

    public TurnoControl(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado) {
        super(id, paciente, odontologo, fecha, hora, estado);
    }

    @Override
    public String toString() {
        return super.toString() + " | [TIPO: Control - Duración: 30 min]";
    }
}