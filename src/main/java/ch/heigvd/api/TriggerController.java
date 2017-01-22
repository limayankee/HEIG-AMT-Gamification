package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.RuleRepository;
import ch.heigvd.dao.TriggerRepository;
import ch.heigvd.dto.RuleDTO;
import ch.heigvd.dto.TriggerDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Rule;
import ch.heigvd.models.Trigger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rules", consumes = "application/json")
@Api(value = "Rules", description = "CRUD on the rules")
public class TriggerController {

	@Autowired
	private TriggerRepository triggerRepository;

	@RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
	public List<TriggerDTO> getTriggers(@RequestAttribute("application") Application app) {
		return triggerRepository.findByApplication(app).stream().map(TriggerDTO::fromTrigger).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity addTrigger(@RequestAttribute("application") Application app, @Valid @RequestBody TriggerDTO input) {
		Trigger trigger = triggerRepository.findByNameAndApplication(input.getName(), app);
		if(trigger != null){
			throw new ConflictException("Trigger already exists");
		}
		trigger = new Trigger(input.getName(), trigger.getExpr(), app);
		triggerRepository.save(trigger);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
