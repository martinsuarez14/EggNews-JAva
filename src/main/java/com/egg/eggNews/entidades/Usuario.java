
package com.egg.eggNews.entidades;

import com.egg.eggNews.rol.Rol;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    protected String nombre;
    protected String email;
    protected String password;

    @Temporal(TemporalType.DATE)
    protected Date fechaDeAlta;
    
    @Enumerated(EnumType.STRING)
    protected Rol rol;
    
    protected boolean activo;

    @OneToOne
    protected Imagen imagen;

}
