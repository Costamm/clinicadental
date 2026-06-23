package clinicagui;

import clinicamodelo.odontologos.Odontologo;
import clinicamodelo.pacientes.Paciente;
import clinicamodelo.turnos.Turno;
import clinicaservice.ServicioOdontologo;
import clinicaservice.ServicioPaciente;
import clinicaservice.ServicioTurno;
import clinicaexception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PanelTurnos extends JPanel {

    private final ServicioTurno servicioTurno;
    private final ServicioPaciente servicioPaciente;
    private final ServicioOdontologo servicioOdontologo;

    private JTextField txtId, txtFecha, txtHora, txtMotivoUrgencia;
    private JComboBox<Paciente> comboPacientes;
    private JComboBox<Odontologo> comboOdontologos;
    private JComboBox<String> comboTipoTurno;

    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnCancelar, btnLimpiar;

    public PanelTurnos(ServicioTurno servicioTurno, ServicioPaciente servicioPaciente, ServicioOdontologo servicioOdontologo) {
        this.servicioTurno = servicioTurno;
        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // formulario de los turnos
        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agendar / Modificar Turno"));

        panelFormulario.add(new JLabel("ID Turno:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Paciente:"));
        comboPacientes = new JComboBox<>();
        panelFormulario.add(comboPacientes);

        panelFormulario.add(new JLabel("Odontólogo:"));
        comboOdontologos = new JComboBox<>();
        panelFormulario.add(comboOdontologos);

        panelFormulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        txtFecha = new JTextField();
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("Hora (HH:MM):"));
        txtHora = new JTextField();
        panelFormulario.add(txtHora);

        panelFormulario.add(new JLabel("Tipo de Turno:"));
        comboTipoTurno = new JComboBox<>(new String[]{"Común", "Control", "Urgente"});
        panelFormulario.add(comboTipoTurno);

        panelFormulario.add(new JLabel("Motivo (Solo Urgente):"));
        txtMotivoUrgencia = new JTextField();
        txtMotivoUrgencia.setEnabled(false); // Inhabilitado por defecto
        panelFormulario.add(txtMotivoUrgencia);

        btnGuardar = new JButton("Agendar Turno");
        btnLimpiar = new JButton("Limpiar");
        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnLimpiar);

        // tabla de registros
        String[] columnas = {"ID", "Paciente", "Odontólogo", "Fecha", "Hora", "Estado", "Costo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaTurnos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaTurnos);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar Turno Seleccionado");
        panelAcciones.add(btnCancelar);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(scroll, BorderLayout.CENTER);
        panelDerecho.add(panelAcciones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        configurarEventos();
        cargarCombos();
        actualizarTabla();
    }

    private void cargarCombos() {
        comboPacientes.removeAllItems();
        comboOdontologos.removeAllItems();


        servicioPaciente.listarTodos().forEach(comboPacientes::addItem);
        servicioOdontologo.listarTodos().forEach(comboOdontologos::addItem);
    }

    private void configurarEventos() {
        // Habilitar campo motivo si elige tipo "Urgente"
        comboTipoTurno.addActionListener(e -> {
            String seleccion = (String) comboTipoTurno.getSelectedItem();
            txtMotivoUrgencia.setEnabled("Urgente".equals(seleccion));
        });

        // Guardar turno
        btnGuardar.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                Paciente pac = (Paciente) comboPacientes.getSelectedItem();
                Odontologo od = (Odontologo) comboOdontologos.getSelectedItem();
                LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
                LocalTime hora = LocalTime.parse(txtHora.getText().trim());
                String tipo = (String) comboTipoTurno.getSelectedItem();

                if (pac == null || od == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente y un odontólogo.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Turno nuevoTurno;
                if ("Control".equals(tipo)) {
                    nuevoTurno = servicioTurno.crearTurnoControl(id, pac.getId(), od.getId(), fecha, hora);
                } else if ("Urgente".equals(tipo)) {
                    String motivo = txtMotivoUrgencia.getText().trim();
                    nuevoTurno = servicioTurno.crearTurnoUrgente(id, pac.getId(), od.getId(), fecha, hora, motivo);
                } else {
                    nuevoTurno = servicioTurno.crearTurno(id, pac.getId(), od.getId(), fecha, hora);
                }

                JOptionPane.showMessageDialog(this, "Turno agendado correctamente.\nCosto estimado: $" + nuevoTurno.calcularCosto());
                actualizarTabla();
                limpiarCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Verifique datos", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancelar Turno
        btnCancelar.addActionListener(e -> {
            int fila = tablaTurnos.getSelectedRow();
            if (fila == -1) return;
            Long id = (Long) modeloTabla.getValueAt(fila, 0);
            try {
                servicioTurno.cancelar(id);
                JOptionPane.showMessageDialog(this, "Turno cancelado correctamente.");
                actualizarTabla();
            } catch (ClinicaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Turno> lista = servicioTurno.listarTodos();
        for (Turno t : lista) {
            String nomPac = t.getPaciente() != null ? t.getPaciente().getApellido() + ", " + t.getPaciente().getNombre() : "Sin Paciente";
            String nomOd = t.getOdontologo() != null ? t.getOdontologo().getApellido() : "Sin Odontólogo";
            modeloTabla.addRow(new Object[]{t.getId(), nomPac, nomOd, t.getFecha(), t.getHora(), t.getEstado(), "$" + t.calcularCosto()});
        }
    }

    private void limpiarCampos() {
        txtId.setText(""); txtFecha.setText(""); txtHora.setText(""); txtMotivoUrgencia.setText("");
        comboTipoTurno.setSelectedIndex(0);
        tablaTurnos.clearSelection();
    }
}