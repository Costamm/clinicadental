package clinicamodelo.planes;

public class PlanPremium extends Plan {
    public PlanPremium() { super("Plan Premium"); }
    @Override
    public double calcularDescuento(double montoOriginal) { return montoOriginal * 0.80; }
}
