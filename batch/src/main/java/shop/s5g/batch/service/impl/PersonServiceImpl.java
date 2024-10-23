package shop.s5g.batch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.batch.domain.Person;
import shop.s5g.batch.domain.PersonDto;
import shop.s5g.batch.repo.PersonRepository;
import shop.s5g.batch.service.PersonService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public PersonDto findById(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("Bad Request Exception");
        }

        Person person = personRepository.findById(id)
                .orElseThrow();

        return new PersonDto(person.getId(), person.getName(), person.getGrade());
    }
}
