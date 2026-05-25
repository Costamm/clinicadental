package clinicaio;

import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.turnos.EstadoTurno;
import clinicamodelo.turnos.Turno;
import clinicamodelo.turnos.TurnoControl;
import clinicamodelo.turnos.TurnoUrgente;
import clinicarepository.IRepositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaTurno {
    private static final String DIRECTORIO = "datos";
    private static final String ARCHIVO = DIRECTORIO + "/turnos.csv";

    public void guardar(List<Turno> turnos) {
        BufferedWriter writer = null;
        try {
            new File(DIRECTORIO).mkdirs();
            writer = new BufferedWriter(new FileWriter(ARCHIVO));
            for (Turno t : turnos) {
                if (t == null || t.getPaciente() == null || t.getOdontologo() == null) continue;
                String tipo;
                String motivoUrgencia = "";
                if (t instanceof TurnoUrgente) {
                    tipo = "URGENTE";
                    motivoUrgencia = ((TurnoUrgente) t).getMotivoUrgencia();
                } else if (t instanceof TurnoControl) {
                    tipo = "CONTROL";
                } else {
                    tipo = "COMUN";
                }
                String linea = t.getId() + "," +
                        t.getPaciente().getId() + "," +
                        t.getOdontologo().getId() + "," +
                        t.getFecha() + "," +
                        t.getHora() + "," +
                        t.getEstado() + "," +
                        tipo + "," +
                        motivoUrgencia;
                writer.write(linea);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error al guardar turnos: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar archivo de turnos: " + e.getMessage());
                }
            }
        }
    }

    public List<Turno> cargar(IRepositorio<Paciente> repoPaciente, IRepositorio<Odontologo> repoOdontologo) {
        List<Turno> turnos = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(ARCHIVO));
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    if (linea.trim().isEmpty()) continue;
                    String[] campos = linea.split(",", -1);
                    if (campos.length < 7) continue;

                    Long id = Long.parseLong(campos[0]);
                    Long pacienteId = Long.parseLong(campos[1]);
                    Long odontologoId = Long.parseLong(campos[2]);
                    LocalDate fecha = LocalDate.parse(campos[3]);
                    LocalTime hora = LocalTime.parse(campos[4]);
                    EstadoTurno estado = EstadoTurno.valueOf(campos[5]);
                    String tipo = campos[6];

                    Paciente paciente = repoPaciente.buscarPorId(pacienteId);
                    Odontologo odontologo = repoOdontologo.buscarPorId(odontologoId);

                    if (paciente == null || odontologo == null) continue;

                    Turno turno;
                    if ("URGENTE".equals(tipo)) {
                        String motivo = campos.length > 7 ? campos[7] : "";
                        turno = new TurnoUrgente(id, null, odontologo, fecha, hora, estado, motivo);
                    } else if ("CONTROL".equals(tipo)) {
                        turno = new TurnoControl(id, null, odontologo, fecha, hora, estado);
                    } else {
                        turno = new Turno(id, null, odontologo, fecha, hora, estado);
                    }
                    turno.setPaciente(paciente);
                    turnos.add(turno);
                } catch (Exception e) {
                    System.out.println("Linea invalida en turnos.csv, se omite: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el archivo de turnos: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar archivo de turnos: " + e.getMessage());
                }
            }
        }
        return turnos;
    }
}
