/**
 * 
 */
package no.hvl.dat152.rest.ws.service;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	
	// SAVE USER
	public User saveUser(User user) {
		
		user = userRepository.save(user);
		return user;
	}
	
	// DELETE USER
	public void deleteUser(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id)
		.orElseThrow(()-> new UserNotFoundException("User with id: " + id + "not found"));
		userRepository.delete(user);
	}
	
	// UPDATE EXISTING USER
	public User updateUser(User user, Long id) throws UserNotFoundException {
		User existingUser = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User with id: " + id + " not found"));
		existingUser.setFirstname(user.getFirstname());
		existingUser.setLastname(user.getLastname());
		
		return userRepository.save(existingUser);

	}
	
	// GET USER -ALL ORDERS
	 public Set<Order> getUserOrders(Long userid) throws UserNotFoundException{

		User user = findUser(userid);
		return user.getOrders();
	 }
	
	// GET ONE ORDER OF ONE USER
	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = findUser(userid);
		
		for(Order order : user.getOrders()) {
			if (order.getId().equals(oid)) {
				return order;
			}
		}
		throw new OrderNotFoundException("Order with id: " + oid + " not fond with user with id: " + userid);

		
	}
	
	// DELETE AN ORDER FOR AN USER

		public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		
			User user = findUser(userid);
			Order order = getUserOrder(userid, oid);
			user.removeOrder(order);
			userRepository.save(user);
		
	}
	// TODO public User createOrdersForUser(Long userid, Order order)
	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException{

		User user = findUser(userid);
		user.addOrder(order);
		userRepository.save(user);
		return user;
	}
	
}
