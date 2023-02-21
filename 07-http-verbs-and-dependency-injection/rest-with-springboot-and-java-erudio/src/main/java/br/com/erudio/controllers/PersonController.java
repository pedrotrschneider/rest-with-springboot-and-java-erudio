package br.com.erudio.controllers;

import br.com.erudio.models.PersonModel;
import br.com.erudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/")
    public List<PersonModel> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public PersonModel findById(@PathVariable(value = "id") String id) {
        return personService.findById(id);
    }
}
