package ch.heigvd.api;

import ch.heigvd.dto.EventDTO;
import ch.heigvd.dto.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
	private String mockUser = "mockUser";
	private String mockTemporaryUser = "mockTemporaryUser";
	private String mockInexistantUser = "mockInexstantUser";

	@Autowired
	private TestRestTemplate restTemplate;


	@Before
	public void setup() {
		EventDTO mockEvent = new EventDTO("mockEvent", mockUser, null);
		restTemplate = restTemplate.withBasicAuth("pollcat", "pollcat");
		restTemplate.postForObject("/events", mockEvent, Void.class);
	}

	@After
	public void cleanup() {
		restTemplate.delete("/users/{userId}", mockUser);
	}

	@Test
	public void testUserNotFound() {
		HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
		HttpStatus response = restTemplate
				.exchange("/users/{userId}", HttpMethod.GET, entity, UserDTO.class, mockInexistantUser)
				.getStatusCode();
		assertEquals(HttpStatus.NOT_FOUND, response);
	}

	@Test
	public void testUserFound() {
		UserDTO user = restTemplate.getForObject("/users/{userId}", UserDTO.class, mockUser);
		assertEquals(user.getId(), mockUser);
	}

	@Test
	public void testDeleteUser() {
		EventDTO mockEvent = new EventDTO("mockEvent", mockTemporaryUser, null);
		restTemplate.postForObject("/events", mockEvent, Void.class); //creates a new user

		UserDTO user = restTemplate.getForObject("/users/{userId}", UserDTO.class, mockTemporaryUser);
		assertEquals(user.getId(), mockTemporaryUser); //checks that user exists

		restTemplate.delete("/users/{userId}", mockTemporaryUser);

		HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
		HttpStatus response = restTemplate //expects that user is NOT_FOUND
				.exchange("/users/{userId}", HttpMethod.GET, entity, UserDTO.class, mockTemporaryUser)
				.getStatusCode();
		assertEquals(HttpStatus.NOT_FOUND, response);
	}


}

