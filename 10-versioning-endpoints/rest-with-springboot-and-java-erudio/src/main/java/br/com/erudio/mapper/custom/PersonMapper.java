package br.com.erudio.mapper.custom;

import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.models.PersonModel;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonVOV2 convertModelToVO(PersonModel personModel) {
        var personVO = new PersonVOV2();
        personVO.setId(personModel.getId());
        personVO.setAddress(personModel.getAddress());
        personVO.setBirthday(new Date());
        personVO.setFirstName(personModel.getFirstName());
        personVO.setLastName(personModel.getLastName());
        personVO.setGender(personModel.getGender());
        return personVO;
    }

    public PersonModel convertVOToModel(PersonVOV2 personVO) {
        var personModel = new PersonModel();
        personModel.setId(personVO.getId());
        personModel.setAddress(personVO.getAddress());
        personModel.setFirstName(personVO.getFirstName());
        personModel.setLastName(personVO.getLastName());
        personModel.setGender(personVO.getGender());
        return personModel;
    }
}
