package ch.heigvd.dao;

import ch.heigvd.models.Criterion;
import org.springframework.data.repository.CrudRepository;

public interface CriterionRepository extends CrudRepository<Criterion, Integer> {
	Criterion findByNameAndUserId(String name, int userId);
}
