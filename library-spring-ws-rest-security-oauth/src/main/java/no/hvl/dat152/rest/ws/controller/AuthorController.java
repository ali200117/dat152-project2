/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	// TODO authority annotation


	

    @Autowired
    private AuthorService authorService;


    @GetMapping("/authors")
    public List<Author> getAllAuthors()  {

        return authorService.findAll();

    }

	

    @GetMapping("/authors/{id}")
    public Author getAuthor (@PathVariable int id) throws AuthorNotFoundException{

        return authorService.findById(id);

    }
	

    @GetMapping("/authors/{id}/books")
    public Set<Book> getBooksByAuthorId(@PathVariable int id) throws AuthorNotFoundException{

        return authorService.findBooksByAuthorId(id);

    }
	
	// TODO - createAuthor (@Mappings, URI, and method)
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor (@RequestBody Author author){

        Author savedAuthor = authorService.saveAuthor(author);


        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
    }
	
	// TODO - updateAuthor (@Mappings, URI, and method)
    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor (@RequestBody Author author, @PathVariable int id) throws AuthorNotFoundException{

        Author existingAuthor = authorService.updateAuthor(author, id);

        return new ResponseEntity<>(existingAuthor, HttpStatus.OK);

    }


}
