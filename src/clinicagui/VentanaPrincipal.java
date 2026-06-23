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

    //  partes de la interfaz visual
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

        //  el marco
        setTitle("Sistema de Gestión - Clínica Dental HSM360");
        setSize(950, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null); // Centra la ventana en la pantalla del usuario

        // para que no se cierre la ventana de golpe
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // BorderLayout divide la ventana
        setLayout(new BorderLayout());

        // CardLayout apila la s cartas
        cardLayout = new CardLayout();
        panelCentralContenedor = new JPanel(cardLayout);

        crearPanelLateral();
        crearPanelesDeGestionBase();


        add(panelLateral, BorderLayout.WEST);
        add(panelCentralContenedor, BorderLayout.CENTER);

        configurarCierreAutomatico();
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel();

        panelLateral.setBackground(new Color(41, 48, 66));


        panelLateral.setLayout(new GridLayout(6, 1, 10, 15));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // el titulo
        JLabel lblTitulo = new JLabel(" Sonrisa feliz", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelLateral.add(lblTitulo);

        // Botones de navegación
        btnPacientes = new JButton("Gestión Pacientes");
        btnOdontologos = new JButton("Gestión Odontólogos");
        btnTurnos = new JButton("Gestión Turnos");
        btnBusquedas = new JButton("Búsquedas Avanzadas");


        // Al hacer clic, el CardLayout "voltea la carta" y muestra el panel por su nombre.
        btnPacientes.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelPacientes"));
        btnOdontologos.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelOdontologos"));
        btnTurnos.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelTurnos"));
        btnBusquedas.addActionListener(e -> cardLayout.show(panelCentralContenedor, "panelBusquedas"));

        // se aniaden los botones
        panelLateral.add(btnPacientes);
        panelLateral.add(btnOdontologos);
        panelLateral.add(btnTurnos);
        panelLateral.add(btnBusquedas);
    }

    private void crearPanelesDeGestionBase() {
        // Creamos las instancias
        PanelPacientes panelPacientesReal = new PanelPacientes(this.servicioPaciente);
        PanelOdontologos panelOdontologosReal = new PanelOdontologos(this.servicioOdontologo);
        PanelTurnos panelTurnosReal = new PanelTurnos(this.servicioTurno, this.servicioPaciente, this.servicioOdontologo);
        PanelBusquedas panelBusquedasReal = new PanelBusquedas(this.servicioTurno);

        panelCentralContenedor.add(panelPacientesReal, "panelPacientes");
        panelCentralContenedor.add(panelOdontologosReal, "panelOdontologos");
        panelCentralContenedor.add(panelTurnosReal, "panelTurnos");
        panelCentralContenedor.add(panelBusquedasReal, "panelBusquedas");
    }

    private void configurarCierreAutomatico() {
        //  botón 'X' para pedir la confirmación
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
                    System.exit(0);
                }
            }
        });
    }
}