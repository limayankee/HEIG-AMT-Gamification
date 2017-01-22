package ch.heigvd.dao;

import ch.heigvd.models.Criterion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface CriterionRepository extends CrudRepository<Criterion, Integer> {
	Criterion findByNameAndUserId(String name, int userId);

	@Query("select distinct c from Criterion c where c.name in (?1) and c.userId = ?2")
	List<Criterion> findByNameInSetForUser(Set<String> criteria, int userId);
}
