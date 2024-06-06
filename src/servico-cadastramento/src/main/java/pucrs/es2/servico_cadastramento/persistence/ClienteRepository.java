package pucrs.es2.servico_cadastramento.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pucrs.es2.servico_cadastramento.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}