package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.RuleRepository;
import ch.heigvd.dto.RuleDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping("/rules")
public class RulesController
{
    @Autowired
    private RuleRepository ruleRepository;

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    public List<RuleDTO> getRules(@RequestAttribute("application") Application app) {
        return ruleRepository.findByApplication(app).stream().map(RuleDTO::fromRule).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addRule(@Valid @RequestBody RuleDTO input, @RequestAttribute("application") Application app) {

        Rule rule = ruleRepository.findByNameAndApplication(input.getName(), app);

        if(rule != null){
            throw new ConflictException("Rule already exists");
        }

        rule = new Rule(input.getName(), input.getEventType(), input.getExpr(), app);

        ruleRepository.save(rule);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity editRule(@Valid @RequestBody RuleDTO input, @RequestAttribute("application") Application app) {

        Rule rule = ruleRepository.findByNameAndApplication(input.getName(), app);

        if(rule == null){
            throw new NotFoundException("Rule do not exists");
        }

        rule = new Rule(input.getName(), input.getEventType(), input.getExpr(), app);

        rule.setName(input.getName());
        rule.setEventType(input.getEventType());
        rule.setExpr(input.getExpr());

        ruleRepository.save(rule);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public ResponseEntity deleteRule(@PathVariable String name, @RequestAttribute("application") Application app) {
        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule do not exists");
        }

        ruleRepository.delete(rule);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
