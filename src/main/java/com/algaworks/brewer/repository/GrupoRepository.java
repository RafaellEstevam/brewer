package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Grupo;

/**
 * @author Rafaell Estevam
 *
 */
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long>{
}
