package clinicamodelo.planes;

public abstract class Plan {
    protected String nombre;

    public Plan(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }

    public abstract double calcularDescuento(double montoOriginal);
}

