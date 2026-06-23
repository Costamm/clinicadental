package clinicaApp;

import clinicagui.VentanaPrincipal;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //  se ejecuta un hilo secundario básico para simular la carga  de la base de datoss
        new Thread(() -> {
            try {
                // Simulamos 1.5 segundo de lectura de archivos CSV
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Una vez terminada la "carga" abrimos la interfaz gráfica en el hilo EDT (es como el unico hilo que maneja swing) de Swing
            SwingUtilities.invokeLater(() -> {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            });
        }).start();
    }
}