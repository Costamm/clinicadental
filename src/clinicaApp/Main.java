package clinicaApp;

import clinicagui.VentanaPrincipal;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ejecutamos un hilo secundario básico para simular la carga asíncrona de la base de datos
        new Thread(() -> {
            try {
                // Simulamos 1.5 segundos de lectura de archivos CSV
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Una vez terminada la "carga", abrimos la interfaz gráfica en el hilo EDT de Swing
            SwingUtilities.invokeLater(() -> {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            });
        }).start();
    }
}