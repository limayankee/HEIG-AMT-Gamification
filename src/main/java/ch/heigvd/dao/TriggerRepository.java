package ch.heigvd.dao;

import ch.heigvd.models.Trigger;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TriggerRepository extends CrudRepository<Trigger, Integer> {
	@Query("select distinct t from Trigger t, TriggerCriteria tc where tc.criterionName in (?1) and tc.trigger = t")
	List<Trigger> findByCriterionName(Set<String> criteria);
}
