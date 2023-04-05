package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.excepciones.MiExcepcion;
import com.egg.eggNews.repositorios.ImagenRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {

    @Autowired
    public ImagenRepositorio imagenRepositorio;

    @Transactional
    public Imagen guardar(MultipartFile archivo) throws  MiExcepcion{
        if (archivo != null) {

            try {
                
                Imagen imagen = new Imagen();
                
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepositorio.save(imagen);
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } 
        return null;
    }
    
    @Transactional
    public Imagen actualizar(MultipartFile archivo, Long idImagen) throws MiExcepcion{
        
        if (archivo != null) {

            try {
                Imagen imagen = new Imagen();
                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } 
        return null;
        
    }
    
    public Imagen traerImagen(Long id) {
        if (id != null) {
            Imagen imagen = new Imagen();
            Optional<Imagen> respuesta = imagenRepositorio.findById(id);
            if (respuesta.isPresent()) {
                imagen = respuesta.get();
                return imagen;
            }
        }
        return null;
    }
}
