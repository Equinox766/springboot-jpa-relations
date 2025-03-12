package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.Course;
import org.springframework.data.repository.CrudRepository;

public interface ICourseRepository extends CrudRepository<Course, Long> {

}
