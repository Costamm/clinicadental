package clinicasonrisafeliz.modelo;

import java.time.LocalDate; //manejar fechas
import java.time.LocalTime; //manejar hora
import java.util.ArrayList; //Lista Dinamica

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CLINICA SONRISA FELIZ - Sistema de Gestión ===");
        System.out.println("--------------------------------------------------");

        Domicilio dom1 = new Domicilio(1L, "Av. Rivadavia", 742, "Liniers", "Buenos Aires");
        Paciente pac1 = new Paciente(101L, "Juan", "Perez", "47034598", "juan@gmail.com", LocalDate.now(), dom1);
        Odontologo odon1 = new Odontologo(501L, "Julian", "Lopez", "MN-88342");

        ArrayList<Paciente> listaMiembros = new ArrayList<>();

        GrupoFamiliar familia = new GrupoFamiliar("Familia Pérez", "Plan Oro", listaMiembros);
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

        System.out.println("--------------------------------------------------");
        System.out.println("PROCESO FINALIZADO CON ÉXITO");
    }
}