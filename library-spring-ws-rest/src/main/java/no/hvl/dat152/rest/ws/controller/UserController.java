/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(){
		
		List<User> users = userService.findAllUsers();
		
		if(users.isEmpty())
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException{
		
		User user = userService.findUser(id);
		
		return new ResponseEntity<>(user, HttpStatus.OK);	
		
	}
	
	// TODO - createUser (@Mappings, URI=/users, and method)
	@PostMapping(value = "/users")
	public ResponseEntity<Object> createUser(@RequestBody User user){

		User newUser = userService.saveUser(user);
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	// UPDATE USER
	@PutMapping(value = "/users/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException{

		User updatedUser = userService.updateUser(user, id);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	// TODO - deleteUser (@Mappings, URI, and method)
	@DeleteMapping(value = "/users/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Long id) throws UserNotFoundException{
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);

	}
	// Hent alle ordrer til én bruker
	@GetMapping(value = "/users/{id}/orders")
	public ResponseEntity<Object> getUserOrders(@PathVariable Long id) throws UserNotFoundException {

		Set<Order> orders = userService.getUserOrders(id);

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	// Hent én bestemt ordre til brukeren
	@GetMapping(value = "/users/{uid}/orders/{oid}")
	public ResponseEntity<Object> getUserOrder(@PathVariable Long uid, @PathVariable Long oid)
			throws UserNotFoundException, OrderNotFoundException {

		Order order = userService.getUserOrder(uid, oid);

		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	// Slett én ordre fra brukeren
	@DeleteMapping(value = "/users/{uid}/orders/{oid}")
	public ResponseEntity<Object> deleteUserOrder(@PathVariable Long uid, @PathVariable Long oid)
			throws UserNotFoundException, OrderNotFoundException {

		userService.deleteOrderForUser(uid, oid);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Opprett ordre for bruker, med HATEOAS
	@PostMapping(value = "/users/{id}/orders")
	public ResponseEntity<Object> createUserOrder(@PathVariable Long id, @RequestBody Order order)
			throws UserNotFoundException, OrderNotFoundException {

		
		User user = userService.createOrdersForUser(id, order);

		// Legg lenker på hver ordre
		for (Order o : user.getOrders()) {
			
			o.add(linkTo(methodOn(UserController.class).getUserOrder(id, o.getId())).withSelfRel());
			
			o.add(linkTo(methodOn(UserController.class).getUserOrders(id)).withRel("all-orders"));
			
			o.add(linkTo(methodOn(UserController.class).deleteUserOrder(id, o.getId())).withRel("delete"));
		}

		return new ResponseEntity<>(user.getOrders(), HttpStatus.CREATED);
	}

	
}
