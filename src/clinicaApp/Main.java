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
import clinicarepository.*;
import clinicaservice.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CLINICA SONRISA FELIZ - Sistema de Gestión ===");
        System.out.println("--------------------------------------------------");

        IRepositorio<Paciente> repoPaciente = new RepositorioPaciente();
        IRepositorio<Odontologo> repoOdontologo = new RepositorioOdontologo();
        IRepositorio<Turno> repoTurno = new RepositorioTurno();

        ServicioPaciente servicioPaciente = new ServicioPaciente(repoPaciente);
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo(repoOdontologo);
        ServicioTurno servicioTurno = new ServicioTurno(repoTurno, repoPaciente, repoOdontologo);

        Domicilio dom1 = new Domicilio(1L, "Av. Rivadavia", 742, "Liniers", "Buenos Aires");
        Paciente pac1 = new Paciente(101L, "Juan", "Perez", "47038907", "11-1223-3849", "juan@gmail.com", LocalDate.now(), dom1);
        Odontologo odon1 = new Odontologo(501L, "Julian", "Lopez", "MN-88342");

        servicioPaciente.guardar(pac1);
        servicioOdontologo.guardar(odon1);


        servicioTurno.crearTurnoControl(1001L, pac1.getId(), odon1.getId(), LocalDate.now().plusDays(2), LocalTime.of(10, 0), false);
        servicioTurno.crearTurnoUrgente(1002L, pac1.getId(), odon1.getId(), LocalDate.now().plusDays(3), LocalTime.of(11, 30), "Dolor agudo", false);

        System.out.println("\n>>> PRUEBA DE VALIDACIONES DE NEGOCIO:");
        try {
            System.out.println("Intentando duplicar turno en mismo horario...");
            servicioTurno.crearTurnoControl(1003L, pac1.getId(), odon1.getId(), LocalDate.now().plusDays(2), LocalTime.of(10, 0), false);
        } catch (IllegalArgumentException e) {
            System.out.println("VALIDACIÓN EXITOSA: " + e.getMessage());
        }

        System.out.println("\n>>> GESTIÓN FAMILIAR Y PLANES:");
        GrupoFamiliar familia = new GrupoFamiliar("Familia Pérez", new PlanPremium(), new ArrayList<>());
        familia.agregarMiembros(pac1);
        familia.listarMiembros();
        familia.aplicarDescuento(5000.0);

        System.out.println("\n>>> DETALLE DE TURNOS Y SINCRONIZACIÓN:");
        List<Turno> turnosEnSistema = servicioTurno.listarTodos();
        System.out.println("Turnos en repositorio: " + turnosEnSistema.size());
        System.out.println("Turnos en la lista del Paciente: " + pac1.getTurnos().size());

        for (Turno t : turnosEnSistema) {
            System.out.println("-----------------------");
            System.out.println(t.toString());
        }

        System.out.println("--------------------------------------------------");
        System.out.println("SISTEMA FUNCIONANDO: Etapas 1, 2 y 3 verificadas.");
        System.out.println("PROCESO FINALIZADO CON ÉXITO");
    }
}