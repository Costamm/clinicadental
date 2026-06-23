package clinicamodelo.odontologos;


public class Cirujano extends Odontologo {
    public Cirujano(Long id, String nombre, String apellido, String matricula) {
        super(id, nombre, apellido, matricula);
    }

    @Override
    public String toString() {
        return super.toString() + " [Especialidad: Cirugía]";
    }
}
