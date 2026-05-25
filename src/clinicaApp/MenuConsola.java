package clinicaApp;

import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Domicilio;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.turnos.EstadoTurno;
import clinicamodelo.turnos.Turno;
import clinicaservice.ServicioOdontologo;
import clinicaservice.ServicioPaciente;
import clinicaservice.ServicioTurno;
import clinicaexception.ClinicaException;

import java.time.LocalDate;
import java.time.LocalTime;
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
            switch (opcion) {
                case 1 -> crearPaciente();
                case 2 -> buscarPaciente();
                case 3 -> listarPacientes();
                case 4 -> actualizarPaciente();
                case 5 -> eliminarPaciente();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
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
            switch (opcion) {
                case 1 -> crearOdontologo();
                case 2 -> buscarOdontologo();
                case 3 -> listarOdontologos();
                case 4 -> actualizarOdontologo();
                case 5 -> eliminarOdontologo();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
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
        } while (opcion != 0);
    }

    private void crearPaciente() {
        try {
            Paciente paciente = leerPaciente();
            if (servicioPaciente.guardar(paciente)) {
                System.out.println("Paciente creado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void buscarPaciente() {
        try {
            Long id = leerLong("ID del paciente: ");
            Paciente paciente = servicioPaciente.buscarPorId(id);
            System.out.println(paciente);
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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
        try {
            Long id = leerLong("ID del paciente a actualizar: ");
            // Validamos que exista primero
            Paciente existente = servicioPaciente.buscarPorId(id);

            System.out.println("Ingrese los nuevos datos del paciente.");
            existente.setNombre(leerTextoObligatorio("Nombre: "));
            existente.setApellido(leerTextoObligatorio("Apellido: "));
            existente.setDni(leerTextoObligatorio("DNI: "));
            existente.setTelefono(leerTextoObligatorio("Telefono: "));
            existente.setEmail(leerTextoObligatorio("Email: "));
            existente.setDomicilio(leerDomicilio());

            if (servicioPaciente.actualizar(existente)) {
                System.out.println("Paciente actualizado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void eliminarPaciente() {
        try {
            Long id = leerLong("ID del paciente a eliminar: ");
            Paciente paciente = servicioPaciente.buscarPorId(id);

            if (!paciente.getTurnos().isEmpty()) {
                System.out.println("ERROR: No se puede eliminar: el paciente tiene turnos asociados.");
                return;
            }
            if (servicioPaciente.eliminar(id)) {
                System.out.println("Paciente eliminado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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
        Long id = leerLong("ID domicilio: ");
        String calle = leerTextoObligatorio("Calle: ");
        int numero = leerEntero("Numero: ");
        String localidad = leerTextoObligatorio("Localidad: ");
        String provincia = leerTextoObligatorio("Provincia: ");
        return new Domicilio(id, calle, numero, localidad, provincia);
    }

    private void crearOdontologo() {
        try {
            Odontologo odontologo = leerOdontologo();
            if (servicioOdontologo.guardar(odontologo)) {
                System.out.println("Odontologo creado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void buscarOdontologo() {
        try {
            Long id = leerLong("ID del odontologo: ");
            Odontologo odontologo = servicioOdontologo.buscarPorId(id);
            System.out.println(odontologo);
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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
        try {
            Long id = leerLong("ID del odontologo a actualizar: ");
            Odontologo existente = servicioOdontologo.buscarPorId(id);

            System.out.println("Ingrese los nuevos datos del odontologo.");
            existente.setNombre(leerTextoObligatorio("Nombre: "));
            existente.setApellido(leerTextoObligatorio("Apellido: "));
            existente.setMatricula(leerTextoObligatorio("Matricula: "));

            if (servicioOdontologo.actualizar(existente)) {
                System.out.println("Odontologo actualizado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void eliminarOdontologo() {
        try {
            Long id = leerLong("ID del odontologo a eliminar: ");
            servicioOdontologo.buscarPorId(id);

            if (odontologoTieneTurnos(id)) {
                System.out.println("ERROR: No se puede eliminar: el odontologo tiene turnos asociados.");
                return;
            }
            if (servicioOdontologo.eliminar(id)) {
                System.out.println("Odontologo eliminado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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
        try {
            Long id = leerLong("ID del turno: ");
            Long pacienteId = leerLong("ID del paciente: ");
            Long odontologoId = leerLong("ID del odontologo: ");
            LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
            LocalTime hora = leerHora("Hora (HH:MM): ");

            System.out.println("Tipo de turno\n1. Comun\n2. Control\n3. Urgente");
            int tipo = leerEntero("Seleccione tipo: ");

            Turno turno;
            switch (tipo) {
                case 1 -> turno = servicioTurno.crearTurno(id, pacienteId, odontologoId, fecha, hora);
                case 2 -> turno = servicioTurno.crearTurnoControl(id, pacienteId, odontologoId, fecha, hora);
                case 3 -> {
                    String motivo = leerTextoObligatorio("Motivo de urgencia: ");
                    turno = servicioTurno.crearTurnoUrgente(id, pacienteId, odontologoId, fecha, hora, motivo);
                }
                default -> {
                    System.out.println("Tipo de turno invalido.");
                    return;
                }
            }

            System.out.println("Turno creado correctamente.\n" + turno);
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void buscarTurno() {
        try {
            Long id = leerLong("ID del turno: ");
            Turno turno = servicioTurno.buscarPorId(id);
            System.out.println(turno);
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
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
        try {
            Long id = leerLong("ID del turno a actualizar: ");
            Turno turno = servicioTurno.buscarPorId(id);

            turno.setFecha(leerFecha("Nueva fecha (AAAA-MM-DD): "));
            turno.setHora(leerHora("Nueva hora (HH:MM): "));
            turno.setEstado(leerEstadoTurno());

            if (servicioTurno.actualizar(turno)) {
                System.out.println("Turno actualizado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void eliminarTurno() {
        try {
            Long id = leerLong("ID del turno a eliminar: ");
            if (servicioTurno.eliminar(id)) {
                System.out.println("Turno eliminado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void cancelarTurno() {
        try {
            Long id = leerLong("ID del turno a cancelar: ");
            if (servicioTurno.cancelar(id)) {
                System.out.println("Turno cancelado correctamente.");
            }
        } catch (ClinicaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private EstadoTurno leerEstadoTurno() {
        while (true) {
            System.out.println("Estado del turno");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. CANCELADO");
            System.out.println("4. COMPLETADO");
            int opcion = leerEntero("Seleccione estado: ");
            switch (opcion) {
                case 1:
                    return EstadoTurno.PENDIENTE;
                case 2:
                    return EstadoTurno.CONFIRMADO;
                case 3:
                    return EstadoTurno.CANCELADO;
                case 4:
                    return EstadoTurno.COMPLETADO;
                default:
                    System.out.println("Estado invalido.");
            }
        }
    }

    private boolean odontologoTieneTurnos(Long odontologoId) {
        for (Turno turno : servicioTurno.listarTodos()) {
            if (turno.getOdontologo().getId().equals(odontologoId)) {
                return true;
            }
        }
        return false;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            Integer numero = convertirAEntero(entrada);
            if (numero != null) {
                return numero;
            }
            System.out.println("Debe ingresar un numero entero valido.");
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            Long numero = convertirALong(entrada);
            if (numero != null) {
                return numero;
            }
            System.out.println("Debe ingresar un numero valido.");
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            LocalDate fecha = convertirAFecha(entrada);
            if (fecha != null) {
                return fecha;
            }
            System.out.println("Formato invalido. Use AAAA-MM-DD, por ejemplo 2026-05-20.");
        }
    }

    private LocalTime leerHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            LocalTime hora = convertirAHora(entrada);
            if (hora != null) {
                return hora;
            }
            System.out.println("Formato invalido. Use HH:MM, por ejemplo 15:30.");
        }
    }

    private Integer convertirAEntero(String entrada) {
        Long numero = convertirALong(entrada);
        if (numero == null || numero < Integer.MIN_VALUE || numero > Integer.MAX_VALUE) {
            return null;
        }
        return numero.intValue();
    }

    private Long convertirALong(String entrada) {
        String texto = entrada.trim();
        if (texto.isEmpty()) {
            return null;
        }

        boolean esNegativo = texto.charAt(0) == '-';
        int inicio = esNegativo ? 1 : 0;
        if (inicio == texto.length()) {
            return null;
        }

        long resultado = 0;
        for (int i = inicio; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (caracter < '0' || caracter > '9') {
                return null;
            }
            int digito = caracter - '0';
            if (resultado > (Long.MAX_VALUE - digito) / 10) {
                return null;
            }
            resultado = resultado * 10 + digito;
        }

        return esNegativo ? -resultado : resultado;
    }

    private LocalDate convertirAFecha(String entrada) {
        String texto = entrada.trim();
        if (texto.length() != 10 || texto.charAt(4) != '-' || texto.charAt(7) != '-') {
            return null;
        }

        Integer anio = convertirNumeroPositivo(texto.substring(0, 4));
        Integer mes = convertirNumeroPositivo(texto.substring(5, 7));
        Integer dia = convertirNumeroPositivo(texto.substring(8, 10));
        if (anio == null || mes == null || dia == null) {
            return null;
        }
        if (mes < 1 || mes > 12) {
            return null;
        }
        int diasDelMes = obtenerDiasDelMes(anio, mes);
        if (dia < 1 || dia > diasDelMes) {
            return null;
        }
        return LocalDate.of(anio, mes, dia);
    }

    private LocalTime convertirAHora(String entrada) {
        String texto = entrada.trim();
        if (texto.length() != 5 || texto.charAt(2) != ':') {
            return null;
        }

        Integer hora = convertirNumeroPositivo(texto.substring(0, 2));
        Integer minutos = convertirNumeroPositivo(texto.substring(3, 5));
        if (hora == null || minutos == null) {
            return null;
        }
        if (hora < 0 || hora > 23 || minutos < 0 || minutos > 59) {
            return null;
        }
        return LocalTime.of(hora, minutos);
    }

    private Integer convertirNumeroPositivo(String texto) {
        int resultado = 0;
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (caracter < '0' || caracter > '9') {
                return null;
            }
            resultado = resultado * 10 + (caracter - '0');
        }
        return resultado;
    }

    private int obtenerDiasDelMes(int anio, int mes) {
        switch (mes) {
            case 2:
                return esBisiesto(anio) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    private boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
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

