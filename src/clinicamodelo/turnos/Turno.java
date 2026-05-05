package clinicamodelo.turnos;

import clinicamodelo.pacientes.Paciente;
import clinicamodelo.odontologos.Odontologo;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private Long id;
    private Paciente paciente;    // Relación con objeto Paciente
    private Odontologo odontologo; // Relación con objeto Odontologo
    private LocalDate fecha;       // Java Time API
    private LocalTime hora;        // Java Time API
    private EstadoTurno estado;    // Uso del Enum

    public Turno() {
    }

    public Turno(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // --- MÉTODOS DE LÓGICA EXPERT ---
    public int obtenerDuracionMinutos() {
        return 45; // Duración estándar
    }

    public double calcularCosto() {
        return 10000.0; // Costo base
    }

    public void cancelarTurno() {
        this.estado = EstadoTurno.CANCELADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        // Lógica de sincronización: si el paciente no tiene este turno, se lo agregamos
        if (paciente != null && !paciente.getTurnos().contains(this)) {
            paciente.agregarTurno(this);
        }
    }

    public Odontologo getOdontologo() { return odontologo; }
    public void setOdontologo(Odontologo odontologo) { this.odontologo = odontologo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public EstadoTurno getEstado() { return estado; }
    public void setEstado(EstadoTurno estado) { this.estado = estado; }

    @Override
    public String toString() {
        String fechaFormateada = fecha.getDayOfMonth() + "-" + fecha.getMonthValue() + "-" + fecha.getYear();
        return "Turno N°" + id + " [" + estado + "]" +
                "\n  Paciente: " + paciente.getNombre() + " " + paciente.getApellido() +
                "\n  Odontologo: " + odontologo.getApellido() +
                "\n  Fecha: " + fechaFormateada + " a las " + hora +
                "\n  Duración: " + obtenerDuracionMinutos() + " min | Costo: $" + calcularCosto();
    }
}