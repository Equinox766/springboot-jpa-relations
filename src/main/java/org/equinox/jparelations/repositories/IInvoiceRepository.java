package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface IInvoiceRepository extends CrudRepository<Invoice,Long> {
}
