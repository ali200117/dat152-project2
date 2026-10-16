/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	
	public Book saveBook(Book book) {
		
		return bookRepository.save(book);
		
	}
	
	public List<Book> findAll(){
		
		return (List<Book>) bookRepository.findAll();
		
	}
	
	
	public Book findByISBN(String isbn) throws BookNotFoundException {
		
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
		
		return book;
	}
	
	// TODO public Book updateBook(Book book, String isbn)

	public Book updateBook(Book book, String isbn) throws BookNotFoundException{

		Book existingBook = findByISBN(isbn);

		existingBook.setAuthors(book.getAuthors());
		existingBook.setTitle(book.getTitle());

		return bookRepository.save(existingBook);

	}
	
	// TODO public List<Book> findAllPaginate(Pageable page)
	public List<Book> findAllPaginate(Pageable page){

		List<Book> bookList = new ArrayList<>();

		for (Book book : bookRepository.findAll(page)){
			bookList.add(book);
		}

		return bookList;



	}
	
	// TODO public Set<Author> findAuthorsOfBookByISBN(String isbn)

	public Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException{

		Book existingBook = findByISBN(isbn);

		return existingBook.getAuthors();

	}

	// TODO public void deleteById(long id)
	public void deleteById(long id) throws BookNotFoundException{

		Book existingBook = bookRepository.findById(id)
				.orElseThrow(()-> new BookNotFoundException("Book with id: " + id + " does not exist"));

		bookRepository.delete(existingBook);
	}

	
	// TODO public void deleteByISBN(String isbn) 
	public void deleteByISBN(String isbn) throws BookNotFoundException{

		Book existingBook = findByISBN(isbn);

		bookRepository.delete(existingBook);


	}
}
