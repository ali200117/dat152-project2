/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// Alle ordrer, valgfritt filter på utløpsdato + paginering
	@GetMapping("/orders")
	public ResponseEntity<Object> getAllBorrowOrders(
			@RequestParam(required = false) String expiry,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders;

		if (expiry != null) {
			// Filter: utløper før gitt dato (yyyy-MM-dd)
			orders = orderService.findByExpiryDate(LocalDate.parse(expiry), pageable);
		} else {
			// Ingen filter: hent alle og ta ut riktig side
			List<Order> all = orderService.findAllOrders();
			int from = Math.min(page * size, all.size());
			int to = Math.min(from + size, all.size());
			orders = all.subList(from, to);
		}

		if (orders.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	// Hent én ordre, med HATEOAS-lenker
	@GetMapping("/orders/{id}")
	public ResponseEntity<Object> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException {

		Order order = orderService.findOrder(id);

		// Lenke til seg selv
		order.add(linkTo(methodOn(OrderController.class).getBorrowOrder(id)).withSelfRel());
		// Lenke til alle ordrer
		order.add(linkTo(methodOn(OrderController.class).getAllBorrowOrders(null, 0, 10)).withRel("all-orders"));
		// Lenke for å slette ordren
		order.add(linkTo(methodOn(OrderController.class).deleteBookOrder(id)).withRel("delete"));

		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	// Oppdater ordre
	@PutMapping("/orders/{id}")
	public ResponseEntity<Object> updateOrder(@PathVariable Long id, @RequestBody Order order)
			throws OrderNotFoundException {

		Order updatedOrder = orderService.updateOrder(order, id);

		return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
	}

	// Slett ordre
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Object> deleteBookOrder(@PathVariable Long id) throws OrderNotFoundException {

		orderService.deleteOrder(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
