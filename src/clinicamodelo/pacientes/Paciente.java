package clinicamodelo.pacientes;

import clinicamodelo.turnos.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente implements Comparable<Paciente> {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;
    private List<Turno> turnos;

    public Paciente() {
        this.turnos = new ArrayList<>();
    }

    public Paciente(Long id, String nombre, String apellido, String dni, String telefono, String email, LocalDate fechaIngreso, Domicilio domicilio) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.turnos = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public Domicilio getDomicilio() { return domicilio; }
    public void setDomicilio(Domicilio domicilio) { this.domicilio = domicilio; }

    public String getTelefono(){ return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono;}

    public List<Turno> getTurnos() { return turnos;}
    public void setTurnos(List<Turno> turnos) { this.turnos = turnos;}

    public void agregarTurno(Turno t) {
        if (t != null && !this.turnos.contains(t)) {
            this.turnos.add(t);
        }
    }

    @Override
    public int compareTo(Paciente otro) {
        return this.apellido.compareToIgnoreCase(otro.apellido);
    }

    @Override
    public String toString() {
        return "Paciente: " + apellido + ", " + nombre + " | Tel: " + telefono + " | Turnos: " + turnos.size();
    }
}