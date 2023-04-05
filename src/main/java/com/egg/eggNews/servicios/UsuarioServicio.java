package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.repositorios.UsuarioRepositorio;
import com.egg.eggNews.rol.Rol;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    public UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public ImagenServicio imagenServicio;

    @Transactional
    public void crearUsuario(String nombre, String email, String password,
            String password2, MultipartFile archivo) throws MiExcepcion {

        validar(nombre, email, password);
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas deben coincidir.");
        }
        if (archivo == null) {
            throw new MiExcepcion("La imagen está vacía.");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setFechaDeAlta(new Date());

        if (nombre == "admin") {
            usuario.setRol(Rol.ADMIN);
        } else {
            usuario.setRol(Rol.USER);
        }
        usuario.setActivo(true);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void actualizarUsuario(Long idUsuario, String nombre, String email,
            String password, MultipartFile archivo) throws MiExcepcion {
        validar(nombre, email, password);
        if (idUsuario == null) {
            throw new MiExcepcion("El id de usuario está vacío.");
        }
        if (archivo == null) {
            throw new MiExcepcion("La imagen está vacía.");
        }

        Usuario usuario = new Usuario();

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {
            usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(password);

            Long idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional
    public void borrarUsuario(Long id) throws MiExcepcion {
        if (id == null) {
            throw new MiExcepcion("El id está vacío.");
        }
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuarioRepositorio.deleteById(id);
        }
    }

    public Usuario getOne(Long id) {
        return usuarioRepositorio.getOne(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarUsuarioPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }
}
