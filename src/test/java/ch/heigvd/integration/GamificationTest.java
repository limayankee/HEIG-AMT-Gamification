package ch.heigvd.integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by matthieu.villard on 24.01.2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources")
public class GamificationTest {

}