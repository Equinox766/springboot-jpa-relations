package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ICourseRepository extends CrudRepository<Course, Long> {

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id")
    Optional<Course> findWithStudents(Long id);

}
