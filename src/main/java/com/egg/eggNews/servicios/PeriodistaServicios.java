package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.repositorios.PeriodistaRepositorio;
import com.egg.eggNews.rol.Rol;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PeriodistaServicios {

    @Autowired
    public PeriodistaRepositorio periodistaRepositorio;

    @Transactional
    public void crearPeriodista(String nombre, String email, String password,
            String password2, Integer sueldoMensual) throws MiExcepcion {
        validar(nombre, email, password);
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas deben coincidir.");
        }
        if (sueldoMensual == null) {
            throw new MiExcepcion("Debe ingresar el sueldo.");
        }

        Periodista periodista = new Periodista();

        periodista.setNombre(nombre);
        periodista.setEmail(email);
        periodista.setPassword(new BCryptPasswordEncoder().encode(password));
        periodista.setFechaDeAlta(new Date());
        periodista.setRol(Rol.PER);
        periodista.setActivo(true);
        periodista.setSueldo(sueldoMensual);

        periodistaRepositorio.save(periodista);
    }

    @Transactional
    public void modificarPeriodista(Long idPeriodista, String nombre, String email,
            String password, Integer sueldoMensual) throws MiExcepcion {

        validar(nombre, email, password);

        if (idPeriodista == null) {
            throw new MiExcepcion("El id está vacío.");
        }
        if (sueldoMensual == null) {
            throw new MiExcepcion("Debe ingresar el sueldo.");
        }

        Periodista periodista = new Periodista();

        Optional<Periodista> respuesta = periodistaRepositorio.findById(idPeriodista);

        if (respuesta.isPresent()) {
            periodista = respuesta.get();

            periodista.setNombre(nombre);
            periodista.setEmail(email);
            periodista.setPassword(password);
            periodista.setSueldo(sueldoMensual);

            periodistaRepositorio.save(periodista);
        }
    }

    @Transactional
    public void borrarPeriodista(Long id) throws MiExcepcion {
        if (id == null) {
            throw new MiExcepcion("El id está vacío.");
        }
        periodistaRepositorio.deleteById(id);
    }

    public Periodista getOne(Long id) {
        return periodistaRepositorio.getOne(id);
    }

    public List<Periodista> listarPeriodistas() {
        return periodistaRepositorio.findAll();
    }

    @Transactional
    public void modificarAltaPeriodista(Long idPeriodista, boolean activo) throws MiExcepcion {
        if (idPeriodista == null) {
            throw new MiExcepcion("El id del periodista está vacío.");
        }
        Periodista periodista = new Periodista();

        Optional<Periodista> respuesta = periodistaRepositorio.findById(idPeriodista);

        if (respuesta.isPresent()) {
            periodista = respuesta.get();

            periodista.setActivo(activo);

            periodistaRepositorio.save(periodista);
        }

    }

    public void validar(String nombre, String email, String password) throws MiExcepcion {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("El nombre no debe estar vacío.");
        }
        if (email == null || email.isEmpty()) {
            throw new MiExcepcion("El email no debe estar vacío.");
        }
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no debe estar vacía y debe ser mayor a 5 caracteres.");
        }
    }
}
