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
import java.util.List;

import java.time.LocalDate; //manejar fechas
import java.time.LocalTime; //manejar hora
import java.util.ArrayList; //Lista Dinamica

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CLINICA SONRISA FELIZ - Sistema de Gestión ===");
        System.out.println("--------------------------------------------------");

        IRepositorio<Paciente> repoPaciente = new RepositorioPaciente();
        IRepositorio<Odontologo> repoOdontologo = new RepositorioOdontologo();
        IRepositorio<Turno> repoTurno = new RepositorioTurno();

        Domicilio dom1 = new Domicilio(1L, "Av. Rivadavia", 742, "Liniers", "Buenos Aires");
        Paciente pac1 = new Paciente(101L,"Juan","Perez","47038907","11-1223-3849","juan@gmail.com",LocalDate.now(), dom1);
        Odontologo odon1 = new Odontologo(501L, "Julian", "Lopez", "MN-88342");

        repoPaciente.guardar(pac1);
        repoOdontologo.guardar(odon1);

        TurnoControl turnoControl = new TurnoControl(
                1002L, pac1, odon1, LocalDate.now(), LocalTime.of(10, 0), EstadoTurno.PENDIENTE
        );
        TurnoUrgente turnoUrgente = new TurnoUrgente(
                1003L, pac1, odon1, LocalDate.now(), LocalTime.of(11, 30), EstadoTurno.PENDIENTE, "Dolor agudo de muela"
        );

        repoTurno.guardar(turnoControl);
        repoTurno.guardar(turnoUrgente);

        System.out.println("\n>>> PRUEBA DE REPOSITORIOS:");
        Paciente buscado = repoPaciente.buscarPorId(101L);
        System.out.println("Recuperado del Repo: " + buscado.getNombre() + " " + buscado.getApellido());

        List<Turno> todosLosTurnos = repoTurno.listarTodos();
        System.out.println("Total de turnos en memoria: " + todosLosTurnos.size());

        System.out.println("\n>>> GESTIÓN FAMILIAR:");

        GrupoFamiliar familia = new GrupoFamiliar("Familia Pérez", new PlanPremium(), new ArrayList<>());
        familia.agregarMiembros(buscado);
        familia.listarMiembros();
        familia.aplicarDescuento(5000.0);

        System.out.println("\n>>> DETALLE DE TURNOS (POLIMORFISMO):");
        for (Turno t : todosLosTurnos) {
            System.out.println(t.toString());
        }

        System.out.println("--------------------------------------------------");
        System.out.println("PROCESO FINALIZADO CON ÉXITO");
    }
}