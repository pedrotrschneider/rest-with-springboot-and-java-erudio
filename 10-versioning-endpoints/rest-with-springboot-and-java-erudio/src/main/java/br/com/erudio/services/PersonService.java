package br.com.erudio.services;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
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

    @Autowired
    PersonMapper personMapper;

    public List<PersonVO> findAll() {
        logger.info("Finding all people");
        return DozerMapper.parseList(personRepository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person with id " + id);
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        return DozerMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO personVO) {
        logger.info("Creating one person");
        var personModel = DozerMapper.parseObject(personVO, PersonModel.class);
        return DozerMapper.parseObject(personRepository.save(personModel), PersonVO.class);
    }

    public PersonVOV2 createV2(PersonVOV2 personVO) {
        logger.info("Creating one person with v2");
        var personModel = personMapper.convertVOToModel(personVO);
        return personMapper.convertModelToVO(personRepository.save(personModel));
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
        var updatePersonModel = DozerMapper.parseObject(updatePersonVO, PersonModel.class);
        return DozerMapper.parseObject(personRepository.save(updatePersonModel), PersonVO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person with id " + id);
        var personModel = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        personRepository.delete(personModel);
    }
}
