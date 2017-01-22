package ch.heigvd.dao;

import ch.heigvd.models.Badge;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BadgeRepository extends CrudRepository<Badge, Integer> {
	Badge findByName(String name);
	Badge findByNameAndApplicationId(String name, int applicationId);
	List<Badge> findByApplicationId(int applicationId);
}
