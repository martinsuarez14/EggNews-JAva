
package com.egg.eggNews.repositorios;

import com.egg.eggNews.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, Long> {
   
    
}
