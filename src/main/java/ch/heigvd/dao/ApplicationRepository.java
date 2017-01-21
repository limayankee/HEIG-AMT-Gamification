package ch.heigvd.dao;

import ch.heigvd.models.Application;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by matthieu.villard on 21.01.2017.
 */
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
	Application findByName(String name);
	Application findById(Integer id);
}
