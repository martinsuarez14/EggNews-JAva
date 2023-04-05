
package com.egg.eggNews.repositorios;

import com.egg.eggNews.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, Long> {
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public List<Noticia> buscarNoticiaPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT n FROM Noticia n WHERE n.creador.nombre = :nombre")
    public List<Noticia> listarNoticiasPorPeriodistas(@Param("nombre") String nombre);
    
}
