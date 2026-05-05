package clinicaApp;

import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Domicilio;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.turnos.EstadoTurno;
import clinicamodelo.turnos.Turno;
import clinicaservice.ServicioOdontologo;
import clinicaservice.ServicioPaciente;
import clinicaservice.ServicioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {
    private final ServicioPaciente servicioPaciente;
    private final ServicioOdontologo servicioOdontologo;
    private final ServicioTurno servicioTurno;
    private final Scanner scanner;

    public MenuConsola(ServicioPaciente servicioPaciente, ServicioOdontologo servicioOdontologo, ServicioTurno servicioTurno) {
        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
        this.servicioTurno = servicioTurno;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1:
                        menuPacientes();
                        break;
                    case 2:
                        menuOdontologos();
                        break;
                    case 3:
                        menuTurnos();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== CLINICA SONRISA FELIZ ===");
        System.out.println("1. Pacientes");
        System.out.println("2. Odontologos");
        System.out.println("3. Turnos");
        System.out.println("0. Salir");
    }

    private void menuPacientes() {
        int opcion;
        do {
            System.out.println("\n--- PACIENTES ---");
            System.out.println("1. Crear paciente");
            System.out.println("2. Buscar paciente por ID");
            System.out.println("3. Listar pacientes");
            System.out.println("4. Actualizar paciente");
            System.out.println("5. Eliminar paciente");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> crearPaciente();
                    case 2 -> buscarPaciente();
                    case 3 -> listarPacientes();
                    case 4 -> actualizarPaciente();
                    case 5 -> eliminarPaciente();
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void menuOdontologos() {
        int opcion;
        do {
            System.out.println("\n--- ODONTOLOGOS ---");
            System.out.println("1. Crear odontologo");
            System.out.println("2. Buscar odontologo por ID");
            System.out.println("3. Listar odontologos");
            System.out.println("4. Actualizar odontologo");
            System.out.println("5. Eliminar odontologo");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> crearOdontologo();
                    case 2 -> buscarOdontologo();
                    case 3 -> listarOdontologos();
                    case 4 -> actualizarOdontologo();
                    case 5 -> eliminarOdontologo();
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void menuTurnos() {
        int opcion;
        do {
            System.out.println("\n--- TURNOS ---");
            System.out.println("1. Crear turno");
            System.out.println("2. Buscar turno por ID");
            System.out.println("3. Listar turnos");
            System.out.println("4. Actualizar turno");
            System.out.println("5. Eliminar turno");
            System.out.println("6. Cancelar turno");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> crearTurno();
                    case 2 -> buscarTurno();
                    case 3 -> listarTurnos();
                    case 4 -> actualizarTurno();
                    case 5 -> eliminarTurno();
                    case 6 -> cancelarTurno();
                    case 0 -> {}
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void crearPaciente() {
        Paciente paciente = leerPaciente();
        servicioPaciente.guardar(paciente);
        System.out.println("Paciente creado correctamente.");
    }

    private void buscarPaciente() {
        Long id = leerLong("ID del paciente: ");
        Paciente paciente = servicioPaciente.buscarPorId(id);
        System.out.println(paciente != null ? paciente : "No se encontro paciente con ese ID.");
    }

    private void listarPacientes() {
        List<Paciente> pacientes = servicioPaciente.listarTodos();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        for (Paciente paciente : pacientes) {
            System.out.println(paciente);
        }
    }

    private void actualizarPaciente() {
        Long id = leerLong("ID del paciente a actualizar: ");
        Paciente existente = servicioPaciente.buscarPorId(id);
        if (existente == null) {
            System.out.println("No se encontro paciente con ese ID.");
            return;
        }

        System.out.println("Ingrese los nuevos datos del paciente.");
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String dni = leerTextoObligatorio("DNI: ");
        String telefono = leerTextoObligatorio("Telefono: ");
        String email = leerTextoObligatorio("Email: ");
        Domicilio domicilio = leerDomicilio();
        validarDniDisponible(id, dni);

        existente.setNombre(nombre);
        existente.setApellido(apellido);
        existente.setDni(dni);
        existente.setTelefono(telefono);
        existente.setEmail(email);
        existente.setDomicilio(domicilio);
        servicioPaciente.actualizar(existente);
        System.out.println("Paciente actualizado correctamente.");
    }

    private void eliminarPaciente() {
        Long id = leerLong("ID del paciente a eliminar: ");
        Paciente paciente = servicioPaciente.buscarPorId(id);
        if (paciente == null) {
            System.out.println("No se encontro paciente con ese ID.");
            return;
        }
        if (!paciente.getTurnos().isEmpty()) {
            System.out.println("No se puede eliminar: el paciente tiene turnos asociados.");
            return;
        }
        servicioPaciente.eliminar(id);
        System.out.println("Paciente eliminado correctamente.");
    }

    private Paciente leerPaciente() {
        Long id = leerLong("ID: ");
        return leerPacienteConId(id);
    }

    private Paciente leerPacienteConId(Long id) {
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String dni = leerTextoObligatorio("DNI: ");
        String telefono = leerTextoObligatorio("Telefono: ");
        String email = leerTextoObligatorio("Email: ");
        Domicilio domicilio = leerDomicilio();
        return new Paciente(id, nombre, apellido, dni, telefono, email, LocalDate.now(), domicilio);
    }

    private Domicilio leerDomicilio() {
        System.out.println("Datos del domicilio");
        String calle = leerTextoObligatorio("Calle: ");
        int numero = leerEntero("Numero: ");
        String localidad = leerTextoObligatorio("Localidad: ");
        String provincia = leerTextoObligatorio("Provincia: ");
        return new Domicilio(calle, numero, localidad, provincia);
    }

    private void crearOdontologo() {
        Odontologo odontologo = leerOdontologo();
        servicioOdontologo.guardar(odontologo);
        System.out.println("Odontologo creado correctamente.");
    }

    private void buscarOdontologo() {
        Long id = leerLong("ID del odontologo: ");
        Odontologo odontologo = servicioOdontologo.buscarPorId(id);
        System.out.println(odontologo != null ? odontologo : "No se encontro odontologo con ese ID.");
    }

    private void listarOdontologos() {
        List<Odontologo> odontologos = servicioOdontologo.listarTodos();
        if (odontologos.isEmpty()) {
            System.out.println("No hay odontologos registrados.");
            return;
        }
        for (Odontologo odontologo : odontologos) {
            System.out.println(odontologo);
        }
    }

    private void actualizarOdontologo() {
        Long id = leerLong("ID del odontologo a actualizar: ");
        Odontologo existente = servicioOdontologo.buscarPorId(id);
        if (existente == null) {
            System.out.println("No se encontro odontologo con ese ID.");
            return;
        }

        System.out.println("Ingrese los nuevos datos del odontologo.");
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String matricula = leerTextoObligatorio("Matricula: ");
        validarMatriculaDisponible(id, matricula);

        existente.setNombre(nombre);
        existente.setApellido(apellido);
        existente.setMatricula(matricula);
        servicioOdontologo.actualizar(existente);
        System.out.println("Odontologo actualizado correctamente.");
    }

    private void eliminarOdontologo() {
        Long id = leerLong("ID del odontologo a eliminar: ");
        Odontologo odontologo = servicioOdontologo.buscarPorId(id);
        if (odontologo == null) {
            System.out.println("No se encontro odontologo con ese ID.");
            return;
        }
        if (odontologoTieneTurnos(id)) {
            System.out.println("No se puede eliminar: el odontologo tiene turnos asociados.");
            return;
        }
        servicioOdontologo.eliminar(id);
        System.out.println("Odontologo eliminado correctamente.");
    }

    private Odontologo leerOdontologo() {
        Long id = leerLong("ID: ");
        return leerOdontologoConId(id);
    }

    private Odontologo leerOdontologoConId(Long id) {
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String matricula = leerTextoObligatorio("Matricula: ");
        return new Odontologo(id, nombre, apellido, matricula);
    }

    private void crearTurno() {
        Long id = leerLong("ID del turno: ");
        Long pacienteId = leerLong("ID del paciente: ");
        Long odontologoId = leerLong("ID del odontologo: ");
        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
        LocalTime hora = leerHora("Hora (HH:MM): ");

        System.out.println("Tipo de turno");
        System.out.println("1. Comun");
        System.out.println("2. Control");
        System.out.println("3. Urgente");
        int tipo = leerEntero("Seleccione tipo: ");

        Turno turno;
        switch (tipo) {
            case 1 -> turno = servicioTurno.crearTurno(id, pacienteId, odontologoId, fecha, hora);
            case 2 -> turno = servicioTurno.crearTurnoControl(id, pacienteId, odontologoId, fecha, hora);
            case 3 -> {
                String motivo = leerTextoObligatorio("Motivo de urgencia: ");
                turno = servicioTurno.crearTurnoUrgente(id, pacienteId, odontologoId, fecha, hora, motivo);
            }
            default -> throw new IllegalArgumentException("Tipo de turno invalido.");
        }

        System.out.println("Turno creado correctamente.");
        System.out.println(turno);
    }

    private void buscarTurno() {
        Long id = leerLong("ID del turno: ");
        Turno turno = servicioTurno.buscarPorId(id);
        System.out.println(turno != null ? turno : "No se encontro turno con ese ID.");
    }

    private void listarTurnos() {
        List<Turno> turnos = servicioTurno.listarTodos();
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos registrados.");
            return;
        }
        for (Turno turno : turnos) {
            System.out.println(turno);
            System.out.println("--------------------");
        }
    }

    private void actualizarTurno() {
        Long id = leerLong("ID del turno a actualizar: ");
        Turno turno = servicioTurno.buscarPorId(id);
        if (turno == null) {
            System.out.println("No se encontro turno con ese ID.");
            return;
        }

        LocalDate fecha = leerFecha("Nueva fecha (AAAA-MM-DD): ");
        LocalTime hora = leerHora("Nueva hora (HH:MM): ");
        EstadoTurno estado = leerEstadoTurno();
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setEstado(estado);
        servicioTurno.actualizar(turno);
        System.out.println("Turno actualizado correctamente.");
    }

    private void eliminarTurno() {
        Long id = leerLong("ID del turno a eliminar: ");
        servicioTurno.eliminar(id);
        System.out.println("Turno eliminado correctamente.");
    }

    private void cancelarTurno() {
        Long id = leerLong("ID del turno a cancelar: ");
        servicioTurno.cancelar(id);
        System.out.println("Turno cancelado correctamente.");
    }

    private EstadoTurno leerEstadoTurno() {
        System.out.println("Estado del turno");
        System.out.println("1. PENDIENTE");
        System.out.println("2. CONFIRMADO");
        System.out.println("3. CANCELADO");
        System.out.println("4. COMPLETADO");
        int opcion = leerEntero("Seleccione estado: ");
        return switch (opcion) {
            case 1 -> EstadoTurno.PENDIENTE;
            case 2 -> EstadoTurno.CONFIRMADO;
            case 3 -> EstadoTurno.CANCELADO;
            case 4 -> EstadoTurno.COMPLETADO;
            default -> throw new IllegalArgumentException("Estado invalido.");
        };
    }

    private boolean odontologoTieneTurnos(Long odontologoId) {
        for (Turno turno : servicioTurno.listarTodos()) {
            if (turno.getOdontologo().getId().equals(odontologoId)) {
                return true;
            }
        }
        return false;
    }

    private void validarDniDisponible(Long pacienteId, String dni) {
        for (Paciente paciente : servicioPaciente.listarTodos()) {
            if (!paciente.getId().equals(pacienteId) && paciente.getDni().equals(dni)) {
                throw new IllegalArgumentException("Ya existe un paciente registrado con DNI " + dni + ".");
            }
        }
    }

    private void validarMatriculaDisponible(Long odontologoId, String matricula) {
        for (Odontologo odontologo : servicioOdontologo.listarTodos()) {
            if (!odontologo.getId().equals(odontologoId) && odontologo.getMatricula().equals(matricula)) {
                throw new IllegalArgumentException("Ya existe un odontologo registrado con matricula " + matricula + ".");
            }
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return Long.parseLong(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return LocalDate.parse(entrada.trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use AAAA-MM-DD, por ejemplo 2026-05-20.");
            }
        }
    }

    private LocalTime leerHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return LocalTime.parse(entrada.trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use HH:MM, por ejemplo 15:30.");
            }
        }
    }

    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("Este campo es obligatorio.");
        }
    }
}

