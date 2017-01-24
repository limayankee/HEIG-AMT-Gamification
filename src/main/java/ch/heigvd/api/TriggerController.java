package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.TriggerRepository;
import ch.heigvd.dto.TriggerDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Trigger;
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
@RequestMapping(value = "/triggers", consumes = "application/json")
@ApiResponses(value = {
		@ApiResponse(
				code = 401,
				message = "Full authentication is required to access this resource"
		)
})
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
		trigger = new Trigger(input.getName(), trigger.getExpr(), app, input.getCriteria());
		triggerRepository.save(trigger);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{name}", consumes = "application/json")
	public ResponseEntity editTrigger(@RequestAttribute("application") Application app,
	                                  @Valid @RequestBody TriggerDTO input,
	                                  @PathVariable("name") String name) {
		Trigger trigger = triggerRepository.findByNameAndApplication(name, app);
		if(trigger == null){
			throw new NotFoundException("Trigger do not exist");
		}
		Trigger trigger2 = triggerRepository.findByNameAndApplication(input.getName(), app);
		if(trigger2 != null && trigger.getId() != trigger2.getId()){
			throw new ConflictException("Trigger name already used");
		}

		trigger.setName(input.getName());
		trigger.setExpr(input.getExpr());
		trigger.setTriggerCriterias(input.getCriteria());

		triggerRepository.save(trigger);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{name}")
	public ResponseEntity deleteTrigger(@RequestAttribute("application") Application app,
	                                    @PathVariable("name") String name) {
		Trigger trigger = triggerRepository.findByNameAndApplication(name, app);
		if(trigger == null){
			throw new NotFoundException("Trigger do not exist");
		}

		triggerRepository.delete(trigger);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
