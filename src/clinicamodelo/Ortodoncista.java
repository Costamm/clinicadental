package clinicamodelo;

public class Ortodoncista  extends Odontologo {
    public Ortodoncista(Long id, String nombre, String apellido, String matricula) {
        super(id, nombre, apellido, matricula);
    }

    @Override
    public String toString() {
        return super.toString() + " [Especialidad: Ortodoncia]";
    }
}

// Ejemplo 2: Cirujano
public class Cirujano extends Odontologo {
    public Cirujano(Long id, String nombre, String apellido, String matricula) {
        super(id, nombre, apellido, matricula);
    }

    @Override
    public String toString() {
        return super.toString() + " [Especialidad: Cirugía]";
    }
}
