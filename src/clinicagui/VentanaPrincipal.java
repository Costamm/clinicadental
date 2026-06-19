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

        // --- EXPLICACIÓN: CONFIGURACIÓN DEL MARCO (JFrame) ---
        setTitle("Sistema de Gestión - Clínica Dental HSM360");
        setSize(950, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null); // Centra la ventana en la pantalla del usuario

        // Evitamos que la ventana se cierre de golpe. Queremos interceptar el evento con un WindowListener
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // --- EXPLICACIÓN: LAYOUT PRINCIPAL ---
        // Usamos BorderLayout para dividir la ventana en un menú fijo a la izquierda (WEST)
        // y el contenido dinámico en el centro (CENTER).
        setLayout(new BorderLayout());

        // CardLayout nos permite apilar múltiples "pantallas" como si fueran cartas,
        // mostrando solo una a la vez sin tener que abrir ventanas flotantes molestas.
        cardLayout = new CardLayout();
        panelCentralContenedor = new JPanel(cardLayout);

        // --- EXPLICACIÓN: CONSTRUCCIÓN VISUAL MODULAR ---
        crearPanelLateral();
        crearPanelesDeGestionBase();

        // Acoplamos los paneles construidos al marco principal
        add(panelLateral, BorderLayout.WEST);
        add(panelCentralContenedor, BorderLayout.CENTER);

        // Escuchamos el evento de cierre de ventana para asegurar el guardado automático
        configurarCierreAutomatico();
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel();
        // Elegimos un color gris azulado oscuro muy profesional
        panelLateral.setBackground(new Color(41, 48, 66));

        // Usamos un GridLayout de 6 filas y 1 columna para que los botones queden alineados verticalmente
        panelLateral.setLayout(new GridLayout(6, 1, 10, 15));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo/Título de la App en la parte superior del menú
        JLabel lblTitulo = new JLabel(" HSM360 Dental", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelLateral.add(lblTitulo);

        // Inicializamos los botones de navegación
        btnPacientes = new JButton("Gestión Pacientes");
        btnOdontologos = new JButton("Gestión Odontólogos");
        btnTurnos = new JButton("Gestión Turnos");
        btnBusquedas = new JButton("Búsquedas Avanzadas");

        // --- EXPLICACIÓN: MANEJO DE EVENTOS (ActionListener) ---
        // Al hacer clic en un botón, le indicamos al CardLayout que voltee la "carta"
        // mostrando el panel correspondiente mediante su identificador String.
        btnPacientes.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelPacientes"));
        btnOdontologos.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelOdontologos"));
        btnTurnos.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelTurnos"));
        btnBusquedas.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelBusquedas"));

        // Añadimos los componentes al contenedor lateral
        panelLateral.add(btnPacientes);
        panelLateral.add(btnOdontologos);
        panelLateral.add(btnTurnos);
        panelLateral.add(btnBusquedas);
    }

    private void crearPanelesDeGestionBase() {
        // Reemplazamos los carteles fijos por las instancias reales de los formularios creados
        PanelPacientes panelPacientesReal = new PanelPacientes(this.servicioPaciente);
        PanelOdontologos panelOdontologosReal = new PanelOdontologos(this.servicioOdontologo);

        // Conservamos provisoriamente los de la Fase 3
        JPanel pTurnos = new JPanel(new BorderLayout());
        pTurnos.add(new JLabel("Pantalla de Turnos (Fase 3)", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel pBusquedas = new JPanel(new BorderLayout());
        pBusquedas.add(new JLabel("Pantalla de Búsquedas Avanzadas (Fase 3)", SwingConstants.CENTER), BorderLayout.CENTER);

        // Los montamos dentro del contenedor central del CardLayout
        panelCentralContenedor.add(panelPacientesReal, "panelPacientes");
        panelCentralContenedor.add(panelOdontologosReal, "panelOdontologos");
        panelCentralContenedor.add(pTurnos, "panelTurnos");
        panelCentralContenedor.add(pBusquedas, "panelBusquedas");
    }

    private void configurarCierreAutomatico() {
        // --- EXPLICACIÓN: MANEJO DE EVENTOS DE VENTANA (WindowListener) ---
        // Modificamos el comportamiento por defecto del botón 'X' para preguntar confirmación.
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
                    // Como tus repositorios actuales guardan automáticamente al disco tras cada operación CRUD,
                    // cerrar la aplicación garantiza que la persistencia se mantuvo transparente y correcta.
                    System.exit(0);
                }
            }
        });
    }
}