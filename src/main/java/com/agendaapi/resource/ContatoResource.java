package com.agendaapi.resource;

import java.io.InputStream;
import java.util.Optional;

import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendaapi.model.Contato;
import com.agendaapi.repository.ContatoRepository;

@RestController
@RequestMapping(value = "/contatos")
@CrossOrigin("*")
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
    public Page<Contato> list(
    	@RequestParam(value = "page", defaultValue = "0") Integer pagina,
    	@RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina    		
    	){
    	
    	PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return repository.findAll(pageRequest);
    }

    @PatchMapping(value = "/{id}/favorito")
    public void favorite(@PathVariable Integer id){
       Optional<Contato> contato = repository.findById(id);
       contato.ifPresent(c -> {
    	   boolean favorito = c.getFavorito() == Boolean.TRUE;
           c.setFavorito(!favorito);
           repository.save(c);
       });
    }
    
    @PutMapping(value = "/{id}/foto")
    public Optional<byte[]> addPhoto(@PathVariable Integer id, @RequestParam("foto") Part arquivo) {
    	Optional<Contato> contato = repository.findById(id);
    	return contato.map(c -> {
    		try {
				InputStream is = arquivo.getInputStream();
				byte[] bytes = new byte[(int) arquivo.getSize()];
				IOUtils.readFully(is, bytes);
				c.setFoto(bytes);
				repository.save(c);
				is.close();			
				return bytes;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
    	});
    }
}
