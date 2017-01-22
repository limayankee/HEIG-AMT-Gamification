package ch.heigvd.api;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.RuleRepository;
import ch.heigvd.dto.RuleDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Rule;
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
public class RuleTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	private Application app;

	@Before
	public void init() {
		restTemplate = restTemplate.withBasicAuth("pollcat", "pollcat");
		RuleDTO rule = new RuleDTO("existingRuleTest", "type", "expr");
		restTemplate.postForEntity("/rules", rule, Void.class);
		app = applicationRepository.findByName("pollcat");
	}

	@After
	public void reset() {
		restTemplate.delete("/rule/{name}", "existingRuleTest");
	}

	@Test
	public void testCreateRule() throws Exception {
		RuleDTO dto = new RuleDTO("ruleTest", "type", "expr");

		assertEquals(HttpStatus.CREATED, restTemplate.postForEntity("/rules", dto, Void.class).getStatusCode());

		Rule rule = ruleRepository.findByNameAndApplication("ruleTest", app);

		assertNotNull(rule);

		restTemplate.delete("/rules/{name}", "ruleTest");
	}

	@Test
	public void testCreateRuleWithConflict() throws Exception {
		RuleDTO dto = new RuleDTO("existingRuleTest", "type", "expr");

		assertEquals(HttpStatus.CONFLICT, restTemplate.postForEntity("/rules", dto, Void.class).getStatusCode());
	}

	@Test
	public void testDeleteRule() {
		HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
		HttpStatus response = restTemplate .exchange("/rules/{name}", HttpMethod.DELETE, entity, Void.class,
		                                             "existingRuleTest")
		                                   .getStatusCode();

		assertEquals(HttpStatus.NO_CONTENT, response);

		Rule rule = ruleRepository.findByNameAndApplication("existingRuleTest", app);

		assertNull(rule);
	}

	@Test
	public void testRuleFound() {
		RuleDTO rule = restTemplate.getForObject("/rules/{name}", RuleDTO.class, "existingRuleTest");
		assertEquals("existingRuleTest", rule.getName());
	}

	@Test
	public void testRuleNotFound() {

		HttpStatus response = restTemplate.getForEntity("/rules/{name}", RuleDTO.class, "ruleNotFoundTest")
		                                  .getStatusCode();

		assertEquals(HttpStatus.NOT_FOUND, response);
	}
}