package ch.heigvd.api;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.ApplicationDTO;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.models.User;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/events", consumes = "application/json")
public class EventController
{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@ApiResponses(value = {
			@ApiResponse(
					code = 201,
					message = "Successful operation.",
					response = ApplicationDTO.class
			),
			@ApiResponse(
					code = 400,
					message = "Bad request for event",
					response = Void.class
			)
	})
	@ApiParam(value = "The information of the new application", required = true)
	@RequestMapping(method = RequestMethod.POST)
	EventDTO echoEvent(@RequestBody EventDTO event) {
		System.out.println("Received a new event from " + event.getUserId());
		int applicationId = 8;
		User u = userRepository.findByAppUserIdAndApplicationId(event.getUserId(), applicationId);
		if (u == null) {
			userRepository.save(new User(applicationRepository.findById(applicationId), event.getUserId()));
		}
		return event;
	}

	/*public ApplicationDTO add(@RequestBody ApplicationRegisterDTO input) {
		Application app = applicationRepository.findByName(input.name);
		if(app != null){
			throw new ConflictException("Application already registered");
		}

		app = input.buildApplication();

		applicationRepository.save(app);

		return ApplicationDTO.fromApplication(app);
	}*/
}
