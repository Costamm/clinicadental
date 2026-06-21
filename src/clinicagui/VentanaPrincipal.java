package clinicagui;

import clinicarepository.RepositorioOdontologo;
import clinicarepository.RepositorioPaciente;
import clinicarepository.RepositorioTurno;
import clinicaservice.ServicioOdontologo;
import clinicaservice.ServicioPaciente;
import clinicaservice.ServicioTurno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    private ServicioPaciente servicioPaciente;
    private ServicioOdontologo servicioOdontologo;
    private ServicioTurno servicioTurno;

    // 2. Componentes de la Interfaz Visual
    private JPanel panelLateral;
    private JPanel panelCentralContenedor;
    private CardLayout cardLayout;

    private JButton btnPacientes;
    private JButton btnOdontologos;
    private JButton btnTurnos;
    private JButton btnBusquedas;

    public VentanaPrincipal() {
        RepositorioPaciente repoPaciente = new RepositorioPaciente();
        RepositorioOdontologo repoOdontologo = new RepositorioOdontologo();
        RepositorioTurno repoTurno = new RepositorioTurno(repoPaciente, repoOdontologo);

        this.servicioPaciente = new ServicioPaciente(repoPaciente);
        this.servicioOdontologo = new ServicioOdontologo(repoOdontologo);
        this.servicioTurno = new ServicioTurno(repoTurno, repoPaciente, repoOdontologo);

        // --- CONFIGURACIÓN DEL MARCO (JFrame) ---
        setTitle("Sistema de Gestión - Clínica Dental HSM360");
        setSize(950, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null); // Centra la ventana en la pantalla del usuario

        // Evitamos que la ventana se cierre de golpe. Interceptamos el evento con un WindowListener
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // --- LAYOUT PRINCIPAL ---
        // BorderLayout divide la ventana: menú fijo a la izquierda (WEST)
        // y contenido dinámico en el centro (CENTER).
        setLayout(new BorderLayout());

        // CardLayout apila las pantallas como cartas y muestra solo una a la vez.
        cardLayout = new CardLayout();
        panelCentralContenedor = new JPanel(cardLayout);

        // --- CONSTRUCCIÓN VISUAL MODULAR ---
        crearPanelLateral();
        crearPanelesDeGestionBase();

        // Acoplamos los paneles construidos al marco principal
        add(panelLateral, BorderLayout.WEST);
        add(panelCentralContenedor, BorderLayout.CENTER);

        // Escuchamos el evento de cierre para confirmar la salida
        configurarCierreAutomatico();
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel();
        // Color gris azulado oscuro
        panelLateral.setBackground(new Color(41, 48, 66));

        // GridLayout de 6 filas y 1 columna: botones alineados verticalmente
        panelLateral.setLayout(new GridLayout(6, 1, 10, 15));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Título de la App en la parte superior del menú
        JLabel lblTitulo = new JLabel(" HSM360 Dental", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelLateral.add(lblTitulo);

        // Botones de navegación
        btnPacientes = new JButton("Gestión Pacientes");
        btnOdontologos = new JButton("Gestión Odontólogos");
        btnTurnos = new JButton("Gestión Turnos");
        btnBusquedas = new JButton("Búsquedas Avanzadas");

        // --- MANEJO DE EVENTOS (ActionListener) ---
        // Al hacer clic, el CardLayout "voltea la carta" y muestra el panel por su nombre.
        btnPacientes.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelPacientes"));
        btnOdontologos.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelOdontologos"));
        btnTurnos.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelTurnos"));
        btnBusquedas.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelBusquedas"));

        // Añadimos los botones al contenedor lateral
        panelLateral.add(btnPacientes);
        panelLateral.add(btnOdontologos);
        panelLateral.add(btnTurnos);
        panelLateral.add(btnBusquedas);
    }

    private void crearPanelesDeGestionBase() {
        // Creamos las instancias reales de los CUATRO paneles funcionales
        PanelPacientes panelPacientesReal = new PanelPacientes(this.servicioPaciente);
        PanelOdontologos panelOdontologosReal = new PanelOdontologos(this.servicioOdontologo);
        PanelTurnos panelTurnosReal = new PanelTurnos(this.servicioTurno, this.servicioPaciente, this.servicioOdontologo);
        PanelBusquedas panelBusquedasReal = new PanelBusquedas(this.servicioTurno);

        // Los montamos dentro del contenedor central del CardLayout.
        // El segundo parámetro (String) es el identificador que usa cardLayout.show()
        panelCentralContenedor.add(panelPacientesReal, "panelPacientes");
        panelCentralContenedor.add(panelOdontologosReal, "panelOdontologos");
        panelCentralContenedor.add(panelTurnosReal, "panelTurnos");
        panelCentralContenedor.add(panelBusquedasReal, "panelBusquedas");
    }

    private void configurarCierreAutomatico() {
        // --- MANEJO DE EVENTOS DE VENTANA (WindowListener) ---
        // Modificamos el comportamiento del botón 'X' para pedir confirmación.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        VentanaPrincipal.this,
                        "¿Está seguro que desea salir? Los cambios pendientes han sido guardados.",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    // Los repositorios guardan automáticamente al disco tras cada operación CRUD,
                    // así que al cerrar la persistencia ya quedó garantizada.
                    System.exit(0);
                }
            }
        });
    }
}