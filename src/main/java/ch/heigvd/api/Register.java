package ch.heigvd.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jfleroy
 */

@RestController
public class Register
{
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> test() {
        String rtn = "rtn";

        return new ResponseEntity<String>(rtn, HttpStatus.OK);
    }
}
