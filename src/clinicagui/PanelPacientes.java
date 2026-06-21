package clinicagui;

import clinicamodelo.pacientes.Domicilio;
import clinicamodelo.pacientes.Paciente;
import clinicaservice.ServicioPaciente;
import clinicaexception.ClinicaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelPacientes extends JPanel {

    private final ServicioPaciente servicioPaciente;

    // Componentes del Formulario
    private JTextField txtId, txtNombre, txtApellido, txtDni, txtTelefono, txtEmail;
    private JTextField txtCalle, txtNumero, txtLocalidad, txtProvincia;

    // Tabla y Botones
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnEliminar, btnLimpiar;

    public PanelPacientes(ServicioPaciente servicioPaciente) {
        this.servicioPaciente = servicioPaciente;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. CREAR EL FORMULARIO (Panel Izquierdo con estructura Grid)
        JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));

        panelFormulario.add(new JLabel("ID (Solo para nuevo):"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        panelFormulario.add(txtDni);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Calle:"));
        txtCalle = new JTextField();
        panelFormulario.add(txtCalle);

        panelFormulario.add(new JLabel("Número Domicilio:"));
        txtNumero = new JTextField();
        panelFormulario.add(txtNumero);

        panelFormulario.add(new JLabel("Localidad:"));
        txtLocalidad = new JTextField();
        panelFormulario.add(txtLocalidad);

        panelFormulario.add(new JLabel("Provincia:"));
        txtProvincia = new JTextField();
        panelFormulario.add(txtProvincia);

        // Botonera interna del formulario
        btnGuardar = new JButton("Guardar / Actualizar");
        btnLimpiar = new JButton("Limpiar Campos");
        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnLimpiar);

        // 2. CREAR LA TABLA (Centro / Derecha)
        String[] columnas = {"ID", "Apellido", "Nombre", "DNI", "Teléfono", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Tabla de solo lectura
        };
        tablaPacientes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPacientes);

        // Panel inferior para acciones de eliminación
        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar Seleccionado");
        panelAccionesTabla.add(btnEliminar);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(scrollTabla, BorderLayout.CENTER);
        panelDerecho.add(panelAccionesTabla, BorderLayout.SOUTH);

        // Agregar todo al panel principal
        add(panelFormulario, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        // 3. ASIGNAR LOS EVENTOS BÁSICOS
        configurarEventos();
        actualizarTabla();
    }

    private void configurarEventos() {
        // Evento Guardar / Actualizar
        btnGuardar.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String dni = txtDni.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String email = txtEmail.getText().trim();

                String calle = txtCalle.getText().trim();
                int numero = Integer.parseInt(txtNumero.getText().trim());
                String localidad = txtLocalidad.getText().trim();
                String provincia = txtProvincia.getText().trim();

                Domicilio domicilio = new Domicilio(id, calle, numero, localidad, provincia);
                Paciente paciente = new Paciente(id, nombre, apellido, dni, telefono, email, LocalDate.now(), domicilio);

                // Preguntamos al servicio si ya existe un paciente con ese ID.
                // Si existe -> actualizamos. Si no existe -> guardamos como nuevo.
                if (servicioPaciente.existePorId(id)) {
                    servicioPaciente.actualizar(paciente);
                    JOptionPane.showMessageDialog(this, "Paciente actualizado correctamente.");
                } else {
                    servicioPaciente.guardar(paciente);
                    JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
                }

                actualizarTabla();
                limpiarCampos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, verifique que ID y Número sean valores numéricos.", "Dato Inválido", JOptionPane.ERROR_MESSAGE);
            } catch (ClinicaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en negocio", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento Limpiar
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Evento Eliminar
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaPacientes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un paciente de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Long id = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);
            int seguro = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al paciente ID: " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (seguro == JOptionPane.YES_OPTION) {
                try {
                    servicioPaciente.eliminar(id);
                    JOptionPane.showMessageDialog(this, "Paciente eliminado.");
                    actualizarTabla();
                    limpiarCampos();
                } catch (ClinicaException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // MouseListener (vía selección) para cargar datos de la tabla al formulario al hacer click
        tablaPacientes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPacientes.getSelectedRow();
            if (fila != -1) {
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtApellido.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtDni.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
                txtEmail.setText(modeloTabla.getValueAt(fila, 5).toString());
            }
        });
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0); // Vaciar tabla
        List<Paciente> lista = servicioPaciente.listarTodos();
        for (Paciente p : lista) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getApellido(), p.getNombre(), p.getDni(), p.getTelefono(), p.getEmail()});
        }
    }

    private void limpiarCampos() {
        txtId.setText(""); txtNombre.setText(""); txtApellido.setText(""); txtDni.setText("");
        txtTelefono.setText(""); txtEmail.setText(""); txtCalle.setText(""); txtNumero.setText("");
        txtLocalidad.setText(""); txtProvincia.setText("");
        tablaPacientes.clearSelection();
    }
}