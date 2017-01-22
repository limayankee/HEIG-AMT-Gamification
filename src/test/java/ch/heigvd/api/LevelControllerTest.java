package ch.heigvd.api;

import ch.heigvd.dto.LevelDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by matthieu.villard on 22.01.2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class LevelControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void init() {
		restTemplate = restTemplate.withBasicAuth("pollcat", "pollcat");
		LevelDTO level = new LevelDTO("existingLevelTest", 20);
		restTemplate.postForEntity("/levels", level, Void.class);
	}

	@After
	public void reset() {
		restTemplate.delete("/levels/{name}", "existingLevelTest");
	}

	@Test
	public void testCreateLevel() throws Exception {
		LevelDTO dto = new LevelDTO("levelTest", 200000);

		assertEquals(HttpStatus.CREATED, restTemplate.postForEntity("/levels", dto, Void.class).getStatusCode());

		assertEquals(HttpStatus.OK, restTemplate.getForEntity("/levels/{name}", LevelDTO.class, "levelTest").getStatusCode());

		restTemplate.delete("/levels/{name}", "levelTest");
	}

	@Test
	public void testCreateLevelWithConflict() throws Exception {
		LevelDTO dto = new LevelDTO("existingLevelTest", 200000);

		assertEquals(HttpStatus.CONFLICT, restTemplate.postForEntity("/levels", dto, Void.class).getStatusCode());

	}

	@Test
	public void testDeleteLevel() {
		HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
		HttpStatus response = restTemplate .exchange("/levels/{name}", HttpMethod.DELETE, entity, Void.class,
		                                             "existingLevelTest")
		                                   .getStatusCode();

		assertEquals(HttpStatus.NO_CONTENT, response);

		response = restTemplate.getForEntity("/levels/{name}", Void.class, "existingLevelTest").getStatusCode();
		assertEquals(HttpStatus.NOT_FOUND, response);
	}

	@Test
	public void testLevelFound() {
		LevelDTO level = restTemplate.getForObject("/levels/{name}", LevelDTO.class, "existingLevelTest");
		assertEquals("existingLevelTest", level.getName());
	}

	@Test
	public void testLevelNotFound() {

		HttpStatus response = restTemplate.getForEntity("/levels", LevelDTO.class, "levelNotFoundTest")
		                                  .getStatusCode();

		assertEquals(HttpStatus.NOT_FOUND, response);
	}
}