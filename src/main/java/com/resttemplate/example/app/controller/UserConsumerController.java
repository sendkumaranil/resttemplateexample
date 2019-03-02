package com.resttemplate.example.app.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.resttemplate.example.app.model.User;
import com.resttemplate.example.app.service.UserConsumerService;

/**
 * 
 * @author anilkumar
 *
 */
@RestController
@RequestMapping("/abcorgnization")
public class UserConsumerController {

	@Autowired
	private UserConsumerService userConsumerService;
	
	@GetMapping(value="/users/",produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<User>> getUsers() {
		List<User> users=userConsumerService.listAllUsers();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> getUser(@PathVariable String username) {
		User user=userConsumerService.getUser(username);
		
		return ResponseEntity.ok(user);
	}
	
	@PostMapping(value="/user",consumes= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> createUser(@RequestBody User user,UriComponentsBuilder uriBuilder){
		URI uri=userConsumerService.createUser(user);
		HttpHeaders headers=new HttpHeaders();
		headers.setLocation(uri);
		return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
	}
	
	@PutMapping(value="/user/",consumes= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> updateUser(@RequestBody User user) {
		userConsumerService.updateUser(user);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	 
	@DeleteMapping(value="/users/{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		userConsumerService.deleteUser(username);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
