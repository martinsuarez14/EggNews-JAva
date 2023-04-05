package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.servicios.NoticiaServicio;
import com.egg.eggNews.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    NoticiaServicio noticiaServicio;
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "form_usuario.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            usuarioServicio.crearUsuario(nombre, email, password, password2, archivo);
            modelo.put("exito", "El usuario se creó correctamente.");
            return "index.html";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "form_usuario.html";
        }
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña incorrectos.");
        }
        return "login.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        
        Noticia not1 = noticias.get(0);
        Noticia not2 = noticias.get(1);
        Noticia not3 = noticias.get(2);
        
        modelo.addAttribute("not1", not1);
        modelo.addAttribute("not2", not2);
        modelo.addAttribute("not3", not3);
        
        return "inicio.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PER', 'ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable Long id, ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
//        modelo.put("usuario", usuarioServicio.getOne(id));
        return "modificar_usuario.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PER', 'ROLE_ADMIN')")
    @PostMapping("/actualizarPerfil/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam String nombre, @RequestParam String email, 
            @RequestParam String password, @RequestParam(required = false) MultipartFile archivo, ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(id, nombre, email, password, archivo);
            modelo.put("exito", "Se actualizaron los datos.");
            return "redirect:../inicio";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "modificar_usuario.html";
        }
    }
    

    
}
