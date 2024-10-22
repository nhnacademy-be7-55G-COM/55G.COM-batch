package shop.s5g.batch.domain;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) throws Exception {

        if (person.getName().equalsIgnoreCase("정규")) {
            person.setGrade("플레");
        }

        return person;
    }
}
