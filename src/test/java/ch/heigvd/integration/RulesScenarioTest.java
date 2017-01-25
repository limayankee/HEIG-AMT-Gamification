package ch.heigvd.integration;

import ch.heigvd.GamificationApplication;
import ch.heigvd.dto.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jdk.nashorn.internal.parser.JSONParser;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by matthieu.villard on 24.01.2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class RulesScenarioTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private ResponseEntity response;

	private static String username;
	private static String pwd;
	private String name;
	private int threshold;
	private int points;
	private String image;
	private boolean repeatable;
	private String eventType;
	private String expr;
	private String userId;
	private String payload;


	@Given("^The client use the name (.+) and the password (.+)$")
	public void use_the_name_and_password(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}

	@When("^The client calls /applications$")
	public void calls_applications() {
		ApplicationRegisterDTO dto = new ApplicationRegisterDTO(username, pwd);
		response = restTemplate.postForEntity("/applications", dto, ApplicationDTO.class);
	}

	@Then("The client should get a response with HTTP status code (.+)")
	public void should_get_a_response_with_code(int statusCode) {
		assertEquals(statusCode, response.getStatusCodeValue());
	}

	@And("The response should contain the application id")
	public void response_should_contain_app_id() {
		assertTrue(((ResponseEntity<ApplicationDTO>) response).getBody().getId() != 0);
	}

	@Given("^The client use the name (.+) and the threshold (.+)$")
	public void use_the_name_and_threshold(String name, int threshold) {
		this.name = name;
		this.threshold = threshold;
	}

	@When("^The client calls /levels$")
	public void calls_levels() {
		LevelDTO dto = new LevelDTO(name, threshold);
		restTemplate = restTemplate.withBasicAuth(username, pwd);
		response = restTemplate.postForEntity("/levels", dto, Void.class);
	}

	@Given("^The client create the badge (.+), (.+), (\\d+), (.+)$")
	public void creates_the_badge(String name, String image, int points, boolean repeatable) {
		this.name = name;
		this.points = points;
		this.image = image;
		this.repeatable = repeatable;
	}

	@When("^The client calls /badges$")
	public void calls_badges() {
		BadgeDTO dto = new BadgeDTO(name, image, points, repeatable);
		restTemplate = restTemplate.withBasicAuth(username, pwd);
		response = restTemplate.postForEntity("/badges", dto, Void.class);
	}

	@Given("^The client use the name (.+), the eventT type (.+) and the expression (.+)$")
	public void use_name_eventType_expr(String name, String eventType, String expr) {
		this.name = name;
		this.eventType = eventType;
		this.expr = expr;
	}

	@When("^The client calls /rules")
	public void calls_rules() {
		RuleDTO dto = new RuleDTO(name, eventType, expr);
		restTemplate = restTemplate.withBasicAuth(username, pwd);
		response = restTemplate.postForEntity("/rules", dto, Void.class);
	}

	@Given("^The client use the type (.+), the user id (.+) and the payload (.+)$")
	public void use_type_userId_payload(String eventType, String userId, String payload) {
		this.eventType = eventType;
		this.userId = userId;
		this.payload = payload;
	}

	@When("^The client calls /events$")
	public void calls_events() {
		EventDTO dto = new EventDTO(eventType, userId, payload);

		restTemplate = restTemplate.withBasicAuth(username, pwd);
		response = restTemplate.postForEntity("/events", dto, Void.class);
	}

	@And("^The user should have (\\d+) badge\\(s\\)$")
	public void user_should_have_badge(int nb) throws Throwable {
		restTemplate = restTemplate.withBasicAuth(username, pwd);
		List<BadgeDTO> badges = restTemplate.getForObject("/users", UserDTO.class, userId).getBadges();
		assertEquals(nb, badges.size());
	}

	@When("^The client calls DELETE on /applications$")
	public void calls_delet_application() {
		restTemplate = restTemplate.withBasicAuth(username, pwd);
		HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
		response = restTemplate.exchange("/applications", HttpMethod.DELETE, entity, Void.class);
	}

}