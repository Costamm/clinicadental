package clinicaexception;

public class PacienteNoEncontradoException extends ClinicaException {
    public PacienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}