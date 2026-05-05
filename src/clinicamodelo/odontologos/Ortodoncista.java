package clinicamodelo.odontologos;

public class Ortodoncista  extends Odontologo {
    public Ortodoncista(Long id, String nombre, String apellido, String matricula) {
        super(id, nombre, apellido, matricula);
    }

    @Override
    public String toString() {
        return super.toString() + " [Especialidad: Ortodoncia]";
    }
}

