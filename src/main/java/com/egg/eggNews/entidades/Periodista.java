package com.egg.eggNews.entidades;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "periodista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Periodista extends Usuario {

    private Integer sueldo;
    
}
