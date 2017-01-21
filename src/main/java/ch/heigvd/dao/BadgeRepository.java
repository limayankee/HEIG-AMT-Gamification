package ch.heigvd.dao;

import ch.heigvd.models.Badge;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by matthieu.villard on 21.01.2017.
 */
public interface BadgeRepository extends CrudRepository<Badge, Integer> {
	Badge findByNameAndApplicationId(String name, int applicationId);
}
