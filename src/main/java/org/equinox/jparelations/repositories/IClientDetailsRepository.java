package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.ClientDetail;
import org.springframework.data.repository.CrudRepository;

public interface IClientDetailsRepository extends CrudRepository<ClientDetail,Long> {
}
