package br.com.erudio.services;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.ResourceNotFoundException;
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

    public List<PersonVO> findAll() {
        logger.info("Finding all people");
        return personRepository.findAll();
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person with id " + id);
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
    }

    public PersonVO create(PersonVO personVO) {
        logger.info("Creating one person");
        return personRepository.save(personVO);
    }

    public PersonVO update(PersonVO personVO) {
        logger.info("Updating one person");
        logger.info(personVO.getAddress());
        var updatePersonVO = personRepository.findById(personVO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + personVO.getId()));
        updatePersonVO.setFirstName(personVO.getFirstName());
        updatePersonVO.setLastName(personVO.getLastName());
        updatePersonVO.setGender(personVO.getGender());
        updatePersonVO.setAddress(personVO.getAddress());
        return personRepository.save(updatePersonVO);
    }

    public void delete(Long id) {
        logger.info("Deleting one person with id " + id);
        var personVO = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        personRepository.delete(personVO);
    }
}
