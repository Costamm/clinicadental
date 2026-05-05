package clinicamodelo.grupos;

import clinicamodelo.pacientes.Paciente;
import clinicamodelo.planes.Plan;

import java.util.ArrayList;
import java.util.List;

public class GrupoFamiliar {
    private String nombreFamilia;
    private Plan plan;
    private List<Paciente> miembros; // Relación de Agregación

    public GrupoFamiliar(String nombreFamilia, Plan plan, ArrayList<Paciente> miembros) {
        this.nombreFamilia = nombreFamilia;
        this.plan = plan;
        this.miembros = (miembros != null) ? miembros : new ArrayList<>();
    }

    public String getNombreFamilia() { return nombreFamilia; }
    public void setNombreFamilia(String nombreFamilia) { this.nombreFamilia = nombreFamilia; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

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
        if (plan != null) {
            Double total = plan.calcularDescuento(monto);
            System.out.println("Plan aplicado: " + plan.getNombre());
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
                ", Plan='" + plan + '\'' +
                ", Miembros=" + miembros.size() +
                ']';
    }
}