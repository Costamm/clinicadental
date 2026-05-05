package clinicaApp;

import clinicamodelo.grupos.GrupoFamiliar;
import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Domicilio;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.planes.PlanPremium;
import clinicamodelo.turnos.EstadoTurno;
import clinicamodelo.turnos.Turno;
import clinicamodelo.turnos.TurnoControl;
import clinicamodelo.turnos.TurnoUrgente;

import java.time.LocalDate; //manejar fechas
import java.time.LocalTime; //manejar hora
import java.util.ArrayList; //Lista Dinamica

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CLINICA SONRISA FELIZ - Sistema de Gestión ===");
        System.out.println("--------------------------------------------------");

        Domicilio dom1 = new Domicilio(1L, "Av. Rivadavia", 742, "Liniers", "Buenos Aires");
        Paciente pac1 = new Paciente(101L,"Juan","Perez","47038907","11-1223-3849","juan@gmail.com",LocalDate.now(), dom1);
        Odontologo odon1 = new Odontologo(501L, "Julian", "Lopez", "MN-88342");

        ArrayList<Paciente> listaMiembros = new ArrayList<>();

        GrupoFamiliar familia = new GrupoFamiliar("Familia Pérez", new PlanPremium(), new ArrayList<>());;
        familia.agregarMiembros(pac1);

        //Turno
        Turno turno1 = new Turno(1001L, pac1, odon1, LocalDate.of(2026, 4, 14), LocalTime.of(15, 30), EstadoTurno.PENDIENTE);

        System.out.println("\n>>> DATOS DEL TURNO CREADO:");
        System.out.println(turno1.toString());

        System.out.println("\n>>> GESTIÓN FAMILIAR:");
        System.out.println("--- Miembros de la " + familia.getNombreFamilia() + " ---");
        familia.listarMiembros();
        familia.aplicarDescuento(5000.0);

        System.out.println("\n>>> CAMBIO DE ESTADO DEL TURNO:");
        turno1.setEstado(EstadoTurno.CONFIRMADO);
        System.out.println("Nuevo estado: " + turno1.getEstado());

        // --- PRUEBA DE JERARQUÍA DE TURNOS ---
        // Turno de Control (Uso de polimorfismo)
        TurnoControl turnoControl = new TurnoControl(
                1002L, pac1, odon1, LocalDate.now(), LocalTime.of(10, 0), EstadoTurno.PENDIENTE
        );

        // Turno de Urgencia (Uso de polimorfismo con parámetro extra 'motivo')
        TurnoUrgente turnoUrgente = new TurnoUrgente(
                1003L, pac1, odon1, LocalDate.now(), LocalTime.of(11, 30), EstadoTurno.PENDIENTE, "Dolor agudo de muela"
        );

        System.out.println("\n>>> PRUEBA DE TIPOS DE TURNO:");
        System.out.println(turnoControl.toString());
        System.out.println(turnoUrgente.toString());

        System.out.println("--------------------------------------------------");
        System.out.println("PROCESO FINALIZADO CON ÉXITO");
    }
}