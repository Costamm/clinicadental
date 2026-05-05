package clinicamodelo.planes;

public class PlanBasico extends Plan {
    public PlanBasico() { super("Plan Básico"); }
    @Override
    public double calcularDescuento(double montoOriginal) { return montoOriginal * 0.95; }
}
