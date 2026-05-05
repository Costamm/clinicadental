package clinicaApp;

import clinicarepository.RepositorioOdontologo;
import clinicarepository.RepositorioPaciente;
import clinicarepository.RepositorioTurno;
import clinicaservice.ServicioOdontologo;
import clinicaservice.ServicioPaciente;
import clinicaservice.ServicioTurno;



public class Main {
    public static void main(String[] args) {
        RepositorioPaciente repositorioPaciente = new RepositorioPaciente();
        RepositorioOdontologo repositorioOdontologo = new RepositorioOdontologo();
        RepositorioTurno repositorioTurno = new RepositorioTurno();

        ServicioPaciente servicioPaciente = new ServicioPaciente(repositorioPaciente);
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo(repositorioOdontologo);
        ServicioTurno servicioTurno = new ServicioTurno(repositorioTurno, repositorioPaciente, repositorioOdontologo);

        MenuConsola menuConsola = new MenuConsola(servicioPaciente, servicioOdontologo, servicioTurno);
        menuConsola.iniciar();
    }
}