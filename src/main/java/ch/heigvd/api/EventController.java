package ch.heigvd.api;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.CriterionRepository;
import ch.heigvd.dao.RuleRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.dto.ScriptEngineResultDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Rule;
import ch.heigvd.models.User;
import ch.heigvd.scripting.rules.RuleEngine;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/events", consumes = "application/json")
public class EventController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private CriterionRepository criterionRepository;

	@ApiResponses(value = {
			@ApiResponse(
					code = 200,
					message = "Successful operation.",
					response = ScriptEngineResultDTO.class
			),
			@ApiResponse(
					code = 400,
					message = "Bad request for event",
					response = Void.class
			)
	})
	@ApiParam(value = "The information of the new application", required = true)
	@RequestMapping(method = RequestMethod.POST)
	ScriptEngineResultDTO processEvent(@RequestAttribute("application") Application app, @Valid @RequestBody EventDTO event) throws Exception {
		User user = userRepository.findByAppUserIdAndApplicationId(event.getUserId(), app.getId());
		if (user == null) {
			userRepository.save(new User(applicationRepository.findById(app.getId()), event.getUserId()));
		}

		List<Rule> rules = ruleRepository.findMatching(app, event.getType());
		try (RuleEngine engine = new RuleEngine(user, event, criterionRepository)) {
			rules.forEach(engine::executeRule);
			return engine.getResult();
		}
	}
}
