package br.com.erudio.services;

import br.com.erudio.models.PersonModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    static final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<PersonModel> findAll() {
        logger.info("Finding all people");
        List<PersonModel> people = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            var personModel = mockPerson(i);
            people.add(personModel);
        }
        return people;
    }

    public PersonModel findById(String id) {
        logger.info("Finding one person by id.");
        var personModel = new PersonModel();
        personModel.setId(counter.incrementAndGet());
        personModel.setFirstName("Leandro");
        personModel.setLastName("Costa");
        personModel.setAddress("Uberlândia - Minas Gerais - Brasil");
        personModel.setGender("Male");
        return personModel;
    }

    private PersonModel mockPerson(int i) {
        var personModel = new PersonModel();
        personModel.setId(counter.incrementAndGet());
        personModel.setFirstName("First name " + i);
        personModel.setLastName("Last Name " + i);
        personModel.setAddress("Address " + i);
        personModel.setGender("Gender " + i);
        return personModel;
    }
}
