package clinicamodelo.turnos;

import clinicamodelo.pacientes.Paciente;
import clinicamodelo.odontologos.Odontologo;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoUrgente extends Turno {
    private String motivoUrgencia;

    public TurnoUrgente(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado, String motivoUrgencia) {
        super(id, paciente, odontologo, fecha, hora, estado);
        this.motivoUrgencia = motivoUrgencia;
    }

    @Override
    public int obtenerDuracionMinutos() {
        return 60; // Las urgencias tardan más
    }

    @Override
    public double calcularCosto() {
        return 18000.0; // Las urgencias son más caras
    }

    public String getMotivoUrgencia() { return motivoUrgencia; }
    public void setMotivoUrgencia(String motivoUrgencia) { this.motivoUrgencia = motivoUrgencia; }

    @Override
    public String toString() {
        return super.toString() + " | TIPO: URGENCIA - Motivo: " + motivoUrgencia;
    }
}