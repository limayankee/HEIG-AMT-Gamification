package ch.heigvd.api;

import ch.heigvd.dao.*;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.dto.EventProcessingResultDTO;
import ch.heigvd.dto.ScriptEngineResultDTO;
import ch.heigvd.models.*;
import ch.heigvd.scripting.rules.CriterionDelta;
import ch.heigvd.scripting.rules.RuleEngine;
import ch.heigvd.scripting.triggers.TriggerEngine;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	@Autowired
	private BadgeRepository badgeRepository;

	@Autowired
	private UserBadgeRepository userBadgeRepository;

	@Autowired
	private TriggerRepository triggerRepository;

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
	EventProcessingResultDTO processEvent(@RequestAttribute("application") Application app, @Valid @RequestBody EventDTO event) throws Exception {
		User user = userRepository.findByAppUserIdAndApplicationId(event.getUserId(), app.getId());
		if (user == null) {
			userRepository.save(new User(applicationRepository.findById(app.getId()), event.getUserId()));
		}

		List<Rule> rules = ruleRepository.findMatching(app, event.getType());
		try (RuleEngine ruleEngine = new RuleEngine(app, user, event, criterionRepository, badgeRepository, userBadgeRepository);
		     TriggerEngine triggerEngine = new TriggerEngine()) {
			rules.forEach(ruleEngine::executeRule);
			Set<String> updatedCriteriaNames = ruleEngine.getUpdatedCriteria()
			                                             .stream()
			                                             .map(CriterionDelta::getCriterion)
			                                             .map(Criterion::getName)
			                                             .collect(Collectors.toSet());
			List<Trigger> triggers = triggerRepository.findByCriterionName(updatedCriteriaNames);
			return new EventProcessingResultDTO(ruleEngine.getResult(), triggerEngine.getResult());
		}
	}
}
