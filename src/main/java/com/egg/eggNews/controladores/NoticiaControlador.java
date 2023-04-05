
package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.servicios.NoticiaServicio;
import com.egg.eggNews.servicios.PeriodistaServicios;
import java.util.List;
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
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PER', 'ROLE_ADMIN')")
@RequestMapping("/noticia")
public class NoticiaControlador {
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @Autowired
    PeriodistaServicios periodistaServicios;
    
    @GetMapping("/lista")
    public String lista_noticias(ModelMap modelo) {
        
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        
        modelo.put("noticias", noticias);
        
        return "list_noticia.html";
    }
    
    @GetMapping("/escribir-noticia")
    public String registrarNoticia(ModelMap modelo) {
        
        List<Periodista> periodistas = periodistaServicios.listarPeriodistas();
        
        modelo.addAttribute("periodistas", periodistas);
        
        return "form_noticia.html";
    }
    
    @PostMapping("/registro")
    public String guardarNoticia(@RequestParam String titulo, @RequestParam String cuerpo, 
            @RequestParam Long idPeriodista,@RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            noticiaServicio.guardarNoticia(titulo, cuerpo, idPeriodista, archivo);
            modelo.put("exito", "Se guardó la noticia correctamente.");
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("cuerpo", cuerpo);
            modelo.put("idPeriodista", idPeriodista);
            return "form_noticia.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PER', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificarNoticia(@PathVariable Long id, ModelMap modelo) {
        List<Periodista> periodistas = periodistaServicios.listarPeriodistas();
        modelo.put("periodistas", periodistas);
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "modificar_noticia.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PER', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, @RequestParam String titulo, 
            @RequestParam String cuerpo, @RequestParam Long idPeriodista, @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            noticiaServicio.actualizarNoticia(id, titulo, cuerpo, idPeriodista, archivo);
            modelo.put("exito", "Se actualizó la noticia correctamente.");
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put(("error"), ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("cuerpo", cuerpo);
            modelo.put("idPeriodista", idPeriodista);
            return "modificar_noticia.html";
        }
    }
    
    @GetMapping("/leer/{id}")
    public String leerNoticia(@PathVariable Long id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "leer_noticia.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarNoticia(@PathVariable Long id, ModelMap modelo) {
        
        Noticia noticia = noticiaServicio.getOne(id);
        
        try {
            noticiaServicio.borrarNoticia(noticia.getId());
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:../lista";
        }
    }

}
