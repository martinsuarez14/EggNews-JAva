package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.repositorios.NoticiaRepositorio;
import com.egg.eggNews.repositorios.PeriodistaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {
    
    @Autowired
    public NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    public PeriodistaRepositorio periodistaRepositorio;
    
    @Autowired
    public PeriodistaServicios periodistaServicios;
    
    @Autowired
    public ImagenServicio imagenServicio;
    
    @Transactional
    public void guardarNoticia(String titulo, String cuerpo, Long idPeriodista, MultipartFile archivo) throws MiExcepcion {
        
        validar(titulo, cuerpo, idPeriodista);
        
        Noticia noticia = new Noticia();
        Periodista periodista = new Periodista();
        
        Optional<Periodista> respuesta = periodistaRepositorio.findById(idPeriodista);
        
        if (respuesta.isPresent()) {
            periodista = respuesta.get();
            
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setCreador(periodista);
            noticia.setFechaDePublicacion(new Date());
            
            Imagen imagen = imagenServicio.guardar(archivo);
            noticia.setImagen(imagen);
            
            noticiaRepositorio.save(noticia);
        }
    }
    
    @Transactional
    public void actualizarNoticia(Long idNoticia, String titulo, String cuerpo, 
            Long idPeriodista, MultipartFile archivo) throws MiExcepcion {
        if (idNoticia == null) {
            throw new MiExcepcion("El id de la noticia está vacío");
        }
        validar(titulo, cuerpo, idPeriodista);
        Noticia noticia = new Noticia();
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        Periodista periodista = new Periodista();
        Optional<Periodista> periodistaRespuesta = periodistaRepositorio.findById(idPeriodista);
        if (respuesta.isPresent() && periodistaRespuesta.isPresent()) {
            noticia = respuesta.get();
            periodista = periodistaRespuesta.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setCreador(periodista);
            
            Imagen imagen = imagenServicio.actualizar(archivo, noticia.getImagen().getId());
            noticia.setImagen(imagen);
            
            noticiaRepositorio.save(noticia);
        }
    }
    
    @Transactional
    public void borrarNoticia(Long idNoticia) throws MiExcepcion {
        if (idNoticia == null) {
            throw new MiExcepcion("La noticia no se encuentra.");
        }
        
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        
        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            
            noticiaRepositorio.delete(noticia);
        }
    }
    
    public Noticia getOne(Long id) {
        return noticiaRepositorio.getOne(id);
    }
    
    public List<Noticia> listarNoticias() {
        return noticiaRepositorio.findAll();
    }
    
    public void validar(String titulo, String cuerpo, Long idPeriodista) throws MiExcepcion {
        if (titulo == null || titulo.isEmpty()) {
            throw new MiExcepcion("El título no debe estar vacío.");
        }
        if (cuerpo == null || cuerpo.isEmpty()) {
            throw new MiExcepcion("El cuerpo de la noticia no debe estar vacío.");
        }
        if (idPeriodista == null) {
            throw new MiExcepcion("El id del periodista no se reconoce.");
        }
    }
    
}
