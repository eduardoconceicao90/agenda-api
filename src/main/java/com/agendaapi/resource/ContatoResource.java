package com.agendaapi.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendaapi.model.Contato;
import com.agendaapi.repository.ContatoRepository;

@RestController
@RequestMapping(name = "/contatos")
public class ContatoResource {

    @Autowired
    private ContatoRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato save(@RequestBody Contato contato){
        return repository.save(contato);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        repository.deleteById(id);
    }

    @GetMapping
    public List<Contato> list(){
        return repository.findAll();
    }

    @PatchMapping(value = "/{id}/favorito")
    public void favorite(@PathVariable Integer id, @RequestBody Boolean favorito){
       Optional<Contato> contato = repository.findById(id);
       contato.ifPresent(c -> {
           c.setFavorito(favorito);
           repository.save(c);
       });
    }
}
