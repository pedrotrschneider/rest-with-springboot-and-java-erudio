package br.com.erudio.services;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.models.PersonModel;
import br.com.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    public List<PersonModel> findAll() {
        logger.info("Finding all people");
        return personRepository.findAll();
    }

    public PersonModel findById(Long id) {
        logger.info("Finding one person with id " + id);
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
    }

    public PersonModel create(PersonModel personModel) {
        logger.info("Creating one person");
        return personRepository.save(personModel);
    }

    public PersonModel update(PersonModel personModel) {
        logger.info("Updating one person");
        logger.info(personModel.getAddress());
        var updatePersonModel = personRepository.findById(personModel.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + personModel.getId()));
        updatePersonModel.setFirstName(personModel.getFirstName());
        updatePersonModel.setLastName(personModel.getLastName());
        updatePersonModel.setGender(personModel.getGender());
        updatePersonModel.setAddress(personModel.getAddress());
        return personRepository.save(updatePersonModel);
    }

    public void delete(Long id) {
        logger.info("Deleting one person with id " + id);
        var personModel = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        personRepository.delete(personModel);
    }
}
