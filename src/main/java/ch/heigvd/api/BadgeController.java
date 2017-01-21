package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dto.BadgeDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Badge;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jfleroy
 */
@RestController
@RequestMapping("/badges")
@Api(value = "Badges", description = "CRUD on the badges")
public class BadgeController
{
    @Autowired
    BadgeRepository badgeRepository;
    @Autowired
    ApplicationRepository applicationRepository;

   @RequestMapping(consumes = {"application/json"}, produces = {"application/json"}, method = RequestMethod.POST)
    public void post(@RequestAttribute("application") Application app, @RequestBody BadgeDTO input){
       Badge badge = badgeRepository.findByNameAndApplicationId(input.getName(), app.getId());
       Application application = applicationRepository.findById(app.getId());

       if (badge != null) {
           throw new ConflictException("Badge allready existe");
       }

       badgeRepository.save(input.buildBadge(application));
   }

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    public List<BadgeDTO> get(@RequestAttribute("application") Application application){
        List<Badge> badges = badgeRepository.findByApplicationId(application.getId());

        if (!badges.isEmpty()){
            throw new NotFoundException("No badge found for this application");
        }

       return badges.stream().map(BadgeDTO::fromBadgesList).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestAttribute("application") Application application){
        List<Badge> badges = badgeRepository.findByApplicationId(application.getId());

        badgeRepository.delete(badges);
    }
}
