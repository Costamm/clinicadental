package clinicaio;

import clinicamodelo.odontologos.Odontologo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaOdontologo {
    private static final String DIRECTORIO = "datos";
    private static final String ARCHIVO = DIRECTORIO + "/odontologos.csv";

    public void guardar(List<Odontologo> odontologos) {
        BufferedWriter writer = null;
        try {
            new File(DIRECTORIO).mkdirs();
            writer = new BufferedWriter(new FileWriter(ARCHIVO));
            for (Odontologo o : odontologos) {
                String linea = o.getId() + "," +
                        o.getNombre() + "," +
                        o.getApellido() + "," +
                        o.getMatricula();
                writer.write(linea);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error al guardar odontologos: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar archivo de odontologos: " + e.getMessage());
                }
            }
        }
    }

    public List<Odontologo> cargar() {
        List<Odontologo> odontologos = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(ARCHIVO));
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    if (linea.trim().isEmpty()) continue;
                    String[] campos = linea.split(",", -1);
                    if (campos.length < 4) continue;

                    Long id = Long.parseLong(campos[0]);
                    String nombre = campos[1];
                    String apellido = campos[2];
                    String matricula = campos[3];

                    odontologos.add(new Odontologo(id, nombre, apellido, matricula));
                } catch (Exception e) {
                    System.out.println("Linea invalida en odontologos.csv, se omite: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el archivo de odontologos: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar archivo de odontologos: " + e.getMessage());
                }
            }
        }
        return odontologos;
    }
}
