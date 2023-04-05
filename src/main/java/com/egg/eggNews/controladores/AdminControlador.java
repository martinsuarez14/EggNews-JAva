
package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.rol.Rol;
import com.egg.eggNews.servicios.NoticiaServicio;
import com.egg.eggNews.servicios.PeriodistaServicios;
import com.egg.eggNews.servicios.UsuarioServicio;
import java.util.ArrayList;
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
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @Autowired
    PeriodistaServicios periodistaServicios;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {
//        List<Noticia> noticias = noticiaServicio.listarNoticias();
//        
//        Noticia not1 = noticias.get(0);
//        Noticia not2 = noticias.get(1);
//        Noticia not3 = noticias.get(2);
//        
//        modelo.addAttribute("not1", not1);
//        modelo.addAttribute("not2", not2);
//        modelo.addAttribute("not3", not3);
        return "panel.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registro-periodista")
    public String registroPeriodista() {
        return "form_periodista.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/registro-periodista")
    public String registrarPeriodista(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam MultipartFile archivo, @RequestParam Integer sueldo, ModelMap modelo) {
        try {
            periodistaServicios.crearPeriodista(nombre, email, password, password2, sueldo);
            modelo.put("exito", "Se registró correctamente al Periodista");
            return "form_periodista.html";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "form_periodista.html";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/lista-periodistas")
    public String listarPeriodistas(ModelMap modelo) {
        
        List<Periodista> periodistas = periodistaServicios.listarPeriodistas();
        
        modelo.addAttribute("periodistas", periodistas);
        
        return "list_periodista.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/lista-admin")
    public String listarAdmins(ModelMap modelo) {
        
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        
        List<Usuario> administradores = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() == Rol.ADMIN) {
                administradores.add(usuario);
            }
        }
        
        modelo.addAttribute("administradores", administradores);
        
        return "list_admin.html";
    }
    
    @GetMapping("/actualizarPeriodista/{id}")
    public String actualizarPeriodista(@PathVariable Long id, ModelMap modelo) {
       
        Periodista periodista = periodistaServicios.getOne(id);

        modelo.addAttribute("periodista", periodista);
        
        return "modificar_periodista.html";
    }
    
    @PostMapping("/actualizarPeriodista/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam String nombre, @RequestParam String email, 
            @RequestParam String password, @RequestParam(required = false) MultipartFile archivo, 
            @RequestParam Integer sueldo, ModelMap modelo) {
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
