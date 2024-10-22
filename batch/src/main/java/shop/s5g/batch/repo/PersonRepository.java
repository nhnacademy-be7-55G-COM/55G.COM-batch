package shop.s5g.batch.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
