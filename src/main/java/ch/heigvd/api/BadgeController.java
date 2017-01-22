package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.BadgeDTO;
import ch.heigvd.dto.UserBadgeDTO;
import ch.heigvd.models.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/badges")
@Api(value = "Badges", description = "CRUD on the badges")
public class BadgeController {
	@Autowired
	BadgeRepository badgeRepository;
	@Autowired
	UserBadgeRepository userBadgeRepository;
	@Autowired
	ApplicationRepository applicationRepository;
	@Autowired
	UserRepository userRepository;

	@RequestMapping(consumes = {"application/json"}, produces = {"application/json"}, method = RequestMethod.POST)
	public void post(@RequestAttribute("application") Application app, @RequestBody BadgeDTO input) {
		Badge badge = badgeRepository.findByNameAndApplicationId(input.getName(), app.getId());
		Application application = applicationRepository.findById(app.getId());

		if (badge != null) {
			throw new ConflictException("Badge already exists");
		}

		badgeRepository.save(input.buildBadge(application));
	}

	@RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
	public List<BadgeDTO> get(@RequestAttribute("application") Application application) {
		List<Badge> badges = badgeRepository.findByApplicationId(application.getId());
		return badges.stream().map(BadgeDTO::fromBadge).collect(Collectors.toList());
	}

	@RequestMapping(produces = {"application/json"}, method = RequestMethod.GET, path = "/user/{userId}")
	public List<UserBadgeDTO> getUserBadges(@RequestAttribute("application") Application application, @PathVariable String userId) {
		User u = userRepository.findByAppUserIdAndApplicationId(userId, application.getId());
		if (u == null) {
			throw new NotFoundException("User cannot be found");
		}
		List<UserBadge> badges = userBadgeRepository.findByUser(u);
		return badges.stream().map(UserBadgeDTO::fromUserBadge).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{badgeName}")
	public void delete(@RequestAttribute("application") Application application, @PathVariable String badgeName) {
		Badge badge = badgeRepository.findByNameAndApplicationId(badgeName, application.getId());

		if (badge == null) {
			throw new NotFoundException("No badge matching name: " + badgeName);
		}

		badgeRepository.delete(badge);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{badgeName}")
	public void patch(@RequestAttribute("application") Application application, @PathVariable String badgeName, @RequestBody BadgeDTO input) {
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
