package ch.heigvd.api;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.ApplicationDTO;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.User;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/events", consumes = "application/json")
public class EventController {

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
	EventDTO echoEvent(@RequestAttribute("application") Application app, @RequestBody EventDTO event) {
		User u = userRepository.findByAppUserIdAndApplicationId(event.getUserId(), app.getId());
		if (u == null) {
			userRepository.save(new User(applicationRepository.findById(app.getId()), event.getUserId()));
		}

		//TODO: treat the event with rule system


		return event;
	}
}
