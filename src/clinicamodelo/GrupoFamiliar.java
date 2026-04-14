package clinicamodelo;

import java.util.ArrayList;
import java.util.List;

public class GrupoFamiliar {
    private String nombreFamilia;
    private String planDescuento;
    private List<Paciente> miembros; // Relación de Agregación

    public GrupoFamiliar(String nombreFamilia, String planDescuento, ArrayList<Paciente> miembros) {
        this.nombreFamilia = nombreFamilia;
        this.planDescuento = planDescuento;
        if (miembros != null) {
            this.miembros = miembros;
        } else {
            this.miembros = new ArrayList<>();
        }
    }

    public String getNombreFamilia() { return nombreFamilia; }
    public void setNombreFamilia(String nombreFamilia) { this.nombreFamilia = nombreFamilia; }

    public String getPlanDescuento() { return planDescuento; }
    public void setPlanDescuento(String planDescuento) { this.planDescuento = planDescuento; }

    public List<Paciente> getMiembros() { return miembros; }
    public void setMiembros(List<Paciente> miembros) { this.miembros = miembros; }


    public void agregarMiembros(Paciente p) {
        if (p != null) {
            this.miembros.add(p);
            System.out.println("El paciente " + p.getNombre() + " " + p.getApellido() +" se agrego a la " + this.nombreFamilia);
        }
    }

    public void eliminarMiembros(Paciente p) {
        if (this.miembros.remove(p)) {
            System.out.println("Paciente " + p.getNombre() + " removido de la familia.");
        } else {
            System.out.println("El paciente no pertenece a este grupo familiar.");
        }
    }

    // Descuento con Plan
    public void aplicarDescuento(Double monto) {
        if (planDescuento != null && !planDescuento.isEmpty()) {
            Double total = monto * 0.85;
            System.out.println("Monto original: $" + monto + " | Con descuento: $" + total);
        } else {
            System.out.println("Monto a pagar: $" + monto + " (Sin plan de descuento)");
        }
    }

    public void listarMiembros() {
        for (Paciente p : miembros) {
            System.out.println("- " + p.getApellido() + ", " + p.getNombre() + " (DNI: " + p.getDni() + ")");
        }
    }

    @Override
    public String toString() {
        return "GrupoFamiliar [" +
                "Nombre='" + nombreFamilia + '\'' +
                ", Plan='" + planDescuento + '\'' +
                ", Miembros=" + miembros.size() +
                ']';
    }
}