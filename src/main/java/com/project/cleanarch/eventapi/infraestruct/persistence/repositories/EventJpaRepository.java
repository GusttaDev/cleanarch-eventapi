package com.project.cleanarch.eventapi.infraestruct.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

/**
 * Repositório JPA para EventEntity - Camada de Infraestrutura
 * Interface do Spring Data JPA para operações de banco de dados
 */
@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long>, PagingAndSortingRepository<EventEntity, Long>,
        JpaSpecificationExecutor<EventEntity> {

    boolean existsByIdentifier(String identifier);

}