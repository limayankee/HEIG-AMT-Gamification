package ch.heigvd.integration;

import ch.heigvd.dto.ApplicationDTO;
import ch.heigvd.dto.ApplicationRegisterDTO;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by matthieu.villard on 24.01.2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class ApplicationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private String name;
	private String pwd;
	private ResponseEntity<ApplicationDTO> response;

	@Given("The client use the name (.*) and the password (.*)")
	public void use_the_name_and_password(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}

	@When("The client calls /applications")
	public void calls_applications() {
		ApplicationRegisterDTO dto = new ApplicationRegisterDTO(name, pwd);
		response = restTemplate.postForEntity("/applications", dto, ApplicationDTO.class);
	}

	@Then("The client should get a response with HTTP status code (.*)")
	public void should_get_a_response_with_code(int statusCode) {
		assertEquals(statusCode, response.getStatusCodeValue());
	}

	@And("The response should contain the application id")
	public void response_should_contain_app_id() {
		System.out.println(response.getBody());
		assertTrue(response.getBody().getId() != 0);
	}

}