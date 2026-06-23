package clinicagui;

import clinicamodelo.turnos.Turno;
import clinicaservice.ServicioTurno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelBusquedas extends JPanel {

    private final ServicioTurno servicioTurno;
    private JTextField txtFechaInicio, txtFechaFin, txtOdontologoId;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton btnBuscarFechas, btnBuscarOdontologo;

    public PanelBusquedas(ServicioTurno servicioTurno) {
        this.servicioTurno = servicioTurno;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // PAnel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda de Turnos"));

        // Filtro 1
        panelFiltros.add(new JLabel("Desde (AAAA-MM-DD):"));
        txtFechaInicio = new JTextField(8);
        panelFiltros.add(txtFechaInicio);

        panelFiltros.add(new JLabel("Hasta:"));
        txtFechaFin = new JTextField(8);
        panelFiltros.add(txtFechaFin);

        btnBuscarFechas = new JButton("Filtrar Fechas");
        panelFiltros.add(btnBuscarFechas);

        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));

        // Filtro 2
        panelFiltros.add(new JLabel("ID Odontólogo:"));
        txtOdontologoId = new JTextField(4);
        panelFiltros.add(txtOdontologoId);

        btnBuscarOdontologo = new JButton("Filtrar Odontólogo");
        panelFiltros.add(btnBuscarOdontologo);

        // resultados
        String[] columnas = {"ID Turno", "Paciente", "Odontólogo", "Fecha", "Hora", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaResultados = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaResultados);

        add(panelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        configurarEventos();
    }

    private void configurarEventos() {
        // Evento Filtrar Fechas usando Stream API existente
        btnBuscarFechas.addActionListener(e -> {
            try {
                LocalDate inicio = LocalDate.parse(txtFechaInicio.getText().trim());
                LocalDate fin = LocalDate.parse(txtFechaFin.getText().trim());
                List<Turno> resultados = servicioTurno.buscarPorRangoFechas(inicio, fin);
                mostrarResultados(resultados);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en el formato de fechas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // filtrar por odontologo
        btnBuscarOdontologo.addActionListener(e -> {
            try {
                Long idOdontologo = Long.parseLong(txtOdontologoId.getText().trim());
                List<Turno> resultados = servicioTurno.filtrarPorOdontologo(idOdontologo);
                mostrarResultados(resultados);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un ID de odontólogo válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void mostrarResultados(List<Turno> turnos) {
        modeloTabla.setRowCount(0);
        if (turnos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron turnos con los filtros ingresados.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (Turno t : turnos) {
            String pac = t.getPaciente() != null ? t.getPaciente().getApellido() : "N/A";
            String od = t.getOdontologo() != null ? t.getOdontologo().getApellido() : "N/A";
            modeloTabla.addRow(new Object[]{t.getId(), pac, od, t.getFecha(), t.getHora(), t.getEstado()});
        }
    }
}