package ch.heigvd.api;

import ch.heigvd.dto.UserDTO;
import ch.heigvd.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserTest {

	@Autowired
	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;


	@Before
	public void setup() {
		mockServer = MockRestServiceServer.createServer(restTemplate);

	}

	@Test
	public void testCannotGet() {
		mockServer.expect(requestTo("/users/1"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withBadRequest());

		try {
			restTemplate.getForObject("/users/{userId}", UserDTO.class, 1);
		} catch(HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}

		mockServer.verify();
	}



}

