package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IStudentRepository extends CrudRepository<Student, Long> {
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<Student> findByRelations(Long id);
}
