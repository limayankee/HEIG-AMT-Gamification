package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.BadgeDTO;
import ch.heigvd.dto.UserBadgeDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Badge;
import ch.heigvd.models.User;
import ch.heigvd.models.UserBadge;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/badges")
@Api(value = "Badges", description = "Badges management")
@ApiResponses(value = {
		@ApiResponse(
				code = 401,
				message = "Full authentication is required to access this resource"
		)
})
public class BadgeController {
	@Autowired
	BadgeRepository badgeRepository;
	@Autowired
	UserBadgeRepository userBadgeRepository;
	@Autowired
	ApplicationRepository applicationRepository;
	@Autowired
	UserRepository userRepository;


    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
			@ApiResponse(
					code = 201,
					message = "Badge successfully created"
			),
			@ApiResponse(
					code = 409,
					message = "Badge already registered"
			)
	})
	@ApiParam(value = "The information of the new badge", required = true)
	@RequestMapping(consumes = {"application/json"}, produces = {"application/json"}, method = RequestMethod.POST)
	public void post(@RequestAttribute("application") @ApiIgnore Application app,
                     @RequestBody @ApiParam(name = "badge") BadgeDTO input) {
		Badge badge = badgeRepository.findByNameAndApplicationId(input.getName(), app.getId());
		Application application = applicationRepository.findById(app.getId());

		if (badge != null) {
			throw new ConflictException("Badge already exists");
		}

		badgeRepository.save(input.buildBadge(application));
	}


	@ApiResponses(value = {
			@ApiResponse(
					code = 200,
					message = "The list of badges"
			)
	})
	@RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
	public List<BadgeDTO> get(@RequestAttribute("application") @ApiIgnore Application application) {
		List<Badge> badges = badgeRepository.findByApplicationId(application.getId());
		return badges.stream().map(BadgeDTO::fromBadge).collect(Collectors.toList());
	}

    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The list of badges for the given user",
                    responseContainer = "List"
            )
    })
	@RequestMapping(produces = {"application/json"}, method = RequestMethod.GET, path = "/user/{userId}")
	public List<UserBadgeDTO> getUserBadges(@RequestAttribute("application") @ApiIgnore Application application,
                                            @PathVariable String userId) {
		User u = userRepository.findByAppUserIdAndApplicationId(userId, application.getId());
		if (u == null) {
			throw new NotFoundException("User cannot be found");
		}
		List<UserBadge> badges = userBadgeRepository.findByUser(u);
		return badges.stream().map(UserBadgeDTO::fromUserBadge).collect(Collectors.toList());
	}



    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The badge was successfully removed"
            ),
            @ApiResponse(
                    code = 404,
                    message = "No badge found with the given name"
            )
    })
	@RequestMapping(method = RequestMethod.DELETE, value = "/{badgeName}")
	public void delete(@RequestAttribute("application") @ApiIgnore Application application, @PathVariable String badgeName) {
		Badge badge = badgeRepository.findByNameAndApplicationId(badgeName, application.getId());

		if (badge == null) {
			throw new NotFoundException("No badge matching name: " + badgeName);
		}

		badgeRepository.delete(badge);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "The badge was successfully edited"
            ),
            @ApiResponse(
                    code = 404,
                    message = "No badge found with the given name"
            )
    })
	@RequestMapping(method = RequestMethod.PATCH, value = "/{badgeName}")
	public void patch(@RequestAttribute("application") @ApiIgnore Application application,
                                @PathVariable String badgeName, @ApiParam(name = "badge", required = true) @RequestBody BadgeDTO input) {
		Badge badge = badgeRepository.findByNameAndApplicationId(badgeName, application.getId());

		if (badge == null) {
			throw new NotFoundException("No badge matching name: " + badgeName);
		}

		Badge toTest = badgeRepository.findByName(input.getName());

		if (toTest != null && toTest.getId() != badge.getId()) {
			throw new NotFoundException("There is already a badge with this name");
		}

		badge.setName(input.getName());
		badge.setImage(input.getImage());
		badge.setPoints(input.getPoints());
		badge.setRepeatable(input.isRepeatable());

		badgeRepository.save(badge);
    }
}
