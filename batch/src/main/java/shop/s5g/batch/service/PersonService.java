package shop.s5g.batch.service;


import shop.s5g.batch.domain.Person;
import shop.s5g.batch.domain.PersonDto;

public interface PersonService {

    PersonDto findById(Long id);
}
