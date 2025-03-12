package org.equinox.jparelations.repositories;

import org.equinox.jparelations.entities.Student;
import org.springframework.data.repository.CrudRepository;

public interface IStudentRepository extends CrudRepository<Student, Long> {
}
