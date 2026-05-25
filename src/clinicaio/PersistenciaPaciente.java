package clinicaio;

import clinicamodelo.pacientes.Domicilio;
import clinicamodelo.pacientes.Paciente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaPaciente {
    private static final String DIRECTORIO = "datos";
    private static final String ARCHIVO = DIRECTORIO + "/pacientes.csv";

    public void guardar(List<Paciente> pacientes) {
        BufferedWriter writer = null;
        try {
            new File(DIRECTORIO).mkdirs();
            writer = new BufferedWriter(new FileWriter(ARCHIVO));
            for (Paciente p : pacientes) {
                Domicilio d = p.getDomicilio();
                String linea = p.getId() + "," +
                        p.getNombre() + "," +
                        p.getApellido() + "," +
                        p.getDni() + "," +
                        p.getTelefono() + "," +
                        p.getEmail() + "," +
                        p.getFechaIngreso() + "," +
                        (d != null ? d.getId() : "") + "," +
                        (d != null ? d.getCalle() : "") + "," +
                        (d != null ? d.getNumero() : "") + "," +
                        (d != null ? d.getLocalidad() : "") + "," +
                        (d != null ? d.getProvincia() : "");
                writer.write(linea);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error al guardar pacientes: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar archivo de pacientes: " + e.getMessage());
                }
            }
        }
    }

    public List<Paciente> cargar() {
        List<Paciente> pacientes = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(ARCHIVO));
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    if (linea.trim().isEmpty()) continue;
                    String[] campos = linea.split(",", -1);
                    if (campos.length < 12) continue;

                    Long id = Long.parseLong(campos[0]);
                    String nombre = campos[1];
                    String apellido = campos[2];
                    String dni = campos[3];
                    String telefono = campos[4];
                    String email = campos[5];
                    LocalDate fechaIngreso = LocalDate.parse(campos[6]);

                    Domicilio domicilio = null;
                    if (!campos[7].isEmpty()) {
                        Long domId = Long.parseLong(campos[7]);
                        String calle = campos[8];
                        int numero = Integer.parseInt(campos[9]);
                        String localidad = campos[10];
                        String provincia = campos[11];
                        domicilio = new Domicilio(domId, calle, numero, localidad, provincia);
                    }

                    pacientes.add(new Paciente(id, nombre, apellido, dni, telefono, email, fechaIngreso, domicilio));
                } catch (Exception e) {
                    System.out.println("Linea invalida en pacientes.csv, se omite: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el archivo de pacientes: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar archivo de pacientes: " + e.getMessage());
                }
            }
        }
        return pacientes;
    }
}
