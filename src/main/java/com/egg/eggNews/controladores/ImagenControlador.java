
package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.servicios.ImagenServicio;
import com.egg.eggNews.servicios.NoticiaServicio;
import com.egg.eggNews.servicios.PeriodistaServicios;
import com.egg.eggNews.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @Autowired
    PeriodistaServicios periodistaServicios;
    
    @Autowired
    ImagenServicio imagenServicio;
    
    //RECIBIR ID DE IMAGEN
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioServicio.getOne(id);
        
        byte[] imagen = usuario.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    
    @GetMapping("/noticia/{id}")
    public ResponseEntity<byte[]> imagenNoticia(@PathVariable Long id) {
        
        Noticia noticia = noticiaServicio.getOne(id);
        
        Imagen foto = imagenServicio.traerImagen(noticia.getImagen().getId());
        
        byte[] imagen = foto.getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    
    @GetMapping("/periodista/{id}")
    public ResponseEntity<byte[]> imagenPeriodista(@PathVariable Long id) {
        
        Periodista periodista = periodistaServicios.getOne(id);
        
        Imagen foto = imagenServicio.traerImagen(periodista.getImagen().getId());
        
        byte[] imagen = foto.getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}
