package com.lab.user.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.lab.user.management.dto.UserDetailsDTO;
import com.lab.user.management.entity.User;
import com.lab.user.management.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/v1", produces = { MediaType.APPLICATION_JSON_VALUE })

@Tag(name = "CRUD REST APIs for Lab user management", description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE lab user details")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/getUserDetails/{userId}")
	@Operation(summary = "Fetch user details REST API", description = "REST API to fetch lab user details")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "204", description = "HTTP Status NO CONTENT") })
	public ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable int userId) {
		var userDetails = userService.getUserDetailsById(userId);
		if (userDetails == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userDetails);
		else
			return ResponseEntity.status(HttpStatus.OK).body(userDetails);
	}

	@PutMapping("/updateUserDetails/{userId}")
	@Operation(summary = "Update user details REST API", description = "REST API to update lab user details")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "400", description = "HTTP Status NO CONTENT") })
	public ResponseEntity<String> updateUserDetails(@RequestBody UserDetailsDTO userDetails, @PathVariable int userId) {

		if (userId != 0) {
			UserDetailsDTO user = userService.getUserDetailsById(userId);
			if (user != null) {
				boolean updated = userService.updateUserDetails(userDetails);
				if (updated)
					return ResponseEntity.status(HttpStatus.OK).body("User details updated successfully");
			}
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update user details");
	}

	@PostMapping("/createUser")
	@Operation(summary = "Create user REST API", description = "REST API to crerate lab user")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status Created"),
			@ApiResponse(responseCode = "400", description = "HTTP Status NO CONTENT") })
	public ResponseEntity<String> createUser(@RequestBody User user) {

		if (user != null) {
			boolean created = userService.createUser(user);
			if (created)
				return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not save. check if the user already present");
	}

	@DeleteMapping("/deleteUser/{userId}")
	@Operation(summary = "Delete user REST API", description = "REST API to delete lab user")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "HTTP Status NO CONTENT but deleted"),
			@ApiResponse(responseCode = "404", description = "HTTP Status NOT FOUND") })
	public ResponseEntity<String> deleteUser(@PathVariable int userId) {

		boolean deleted = userService.deleteUser(userId);
		if (deleted)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User saved successfully");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not save. check if the user already present");
	}
}
