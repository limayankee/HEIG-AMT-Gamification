package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
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
public class Rules
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
}
