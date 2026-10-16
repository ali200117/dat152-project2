/**
 * 
 */
package no.hvl.dat152.rest.ws.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
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
	
	// TODO public Set<Order> getUserOrders(Long userid)
	 
	
	// TODO public Order getUserOrder(Long userid, Long oid)
	
	// TODO public void deleteOrderForUser(Long userid, Long oid)
	
	// TODO public User createOrdersForUser(Long userid, Order order)
}
