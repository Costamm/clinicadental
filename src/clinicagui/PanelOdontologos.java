package clinicagui;

import clinicamodelo.odontologos.Odontologo;
import clinicaservice.ServicioOdontologo;
import clinicaexception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelOdontologos extends JPanel {

    private final ServicioOdontologo servicioOdontologo;
    private JTextField txtId, txtNombre, txtApellido, txtMatricula;
    private JTable tablaOdontologos;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnEliminar, btnLimpiar;

    public PanelOdontologos(ServicioOdontologo servicioOdontologo) {
        this.servicioOdontologo = servicioOdontologo;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Odontólogo"));

        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("Matrícula:"));
        txtMatricula = new JTextField();
        panelFormulario.add(txtMatricula);

        btnGuardar = new JButton("Guardar / Actualizar");
        btnLimpiar = new JButton("Limpiar");
        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnLimpiar);

        // Tabla
        String[] columnas = {"ID", "Apellido", "Nombre", "Matrícula"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Solo lectura
        };
        tablaOdontologos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaOdontologos);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar Seleccionado");
        panelAcciones.add(btnEliminar);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(scroll, BorderLayout.CENTER);
        panelDerecho.add(panelAcciones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        configurarEventos();
        actualizarTabla();
    }

    private void configurarEventos() {
        // Guardar / Actualizar
        btnGuardar.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String matricula = txtMatricula.getText().trim();

                Odontologo o = new Odontologo(id, nombre, apellido, matricula);

                if (servicioOdontologo.existePorId(id)) {
                    servicioOdontologo.actualizar(o);
                    JOptionPane.showMessageDialog(this, "Odontólogo actualizado.");
                } else {
                    servicioOdontologo.guardar(o);
                    JOptionPane.showMessageDialog(this, "Odontólogo guardado.");
                }

                actualizarTabla();
                limpiarCampos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un valor numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClinicaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnEliminar.addActionListener(e -> {
            int fila = tablaOdontologos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un odontólogo de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Long id = (Long) modeloTabla.getValueAt(fila, 0);
            int seguro = JOptionPane.showConfirmDialog(this, "¿Eliminar al odontólogo ID: " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (seguro == JOptionPane.YES_OPTION) {
                try {
                    servicioOdontologo.eliminar(id);
                    JOptionPane.showMessageDialog(this, "Odontólogo eliminado.");
                    actualizarTabla();
                    limpiarCampos();
                } catch (ClinicaException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // selección de fila
        tablaOdontologos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaOdontologos.getSelectedRow();
            if (fila != -1) {
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtApellido.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtMatricula.setText(modeloTabla.getValueAt(fila, 3).toString());
            }
        });
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Odontologo> lista = servicioOdontologo.listarTodos();
        for (Odontologo o : lista) {
            modeloTabla.addRow(new Object[]{o.getId(), o.getApellido(), o.getNombre(), o.getMatricula()});
        }
    }

    private void limpiarCampos() {
        txtId.setText(""); txtNombre.setText(""); txtApellido.setText(""); txtMatricula.setText("");
        tablaOdontologos.clearSelection();
    }
}