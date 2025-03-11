package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IClientRepository extends CrudRepository<Client,Long> {

    @Query("SELECT c FROM Client c JOIN FETCH c.addresses WHERE c.id = ?1")
    Optional<Client> findOne(Long id);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.invoices  WHERE c.id = ?1")
    Optional<Client> findWithInvoices(Long id);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.invoices LEFT JOIN FETCH c.addresses  WHERE c.id = ?1")
    Optional<Client> findWithRelations(Long id);

}
