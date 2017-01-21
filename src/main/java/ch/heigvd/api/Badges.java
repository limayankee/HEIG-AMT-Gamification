package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dto.BadgeDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Badge;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jfleroy
 */
@RestController
@RequestMapping("/badges")
@Api(value = "Badges", description = "CRUD on the badges")
public class Badges
{
    @Autowired
    BadgeRepository badgeRepository;
    @Autowired
    ApplicationRepository applicationRepository;

   @RequestMapping(produces = {"application/json"}, method = RequestMethod.POST)
    public void post(@RequestBody BadgeDTO input){
       Badge badge = badgeRepository.findByNameAndApplicationId(input.getName(), 8);
       Application application = applicationRepository.findById(8);

       if (badge != null) {
           throw new ConflictException("Badge allready existe");
       }

       badgeRepository.save(input.buildBadge(application));
   }
}
