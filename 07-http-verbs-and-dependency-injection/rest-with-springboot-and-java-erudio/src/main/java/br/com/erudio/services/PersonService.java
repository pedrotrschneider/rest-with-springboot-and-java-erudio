package br.com.erudio.services;

import br.com.erudio.models.PersonModel;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    static final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

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

}
