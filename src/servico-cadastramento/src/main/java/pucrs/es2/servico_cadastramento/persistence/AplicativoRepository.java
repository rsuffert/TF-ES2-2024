package pucrs.es2.servico_cadastramento.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pucrs.es2.servico_cadastramento.entities.Aplicativo;

public interface AplicativoRepository extends JpaRepository<Aplicativo, Long> {}