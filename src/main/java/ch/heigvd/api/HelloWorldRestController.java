package ch.heigvd.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldRestController {


    //-------------------Retrieve All Users--------------------------------------------------------

    @RequestMapping(value = "/test/", method = RequestMethod.GET)
    public ResponseEntity<String> test() {
        String rtn = "rtn";

        return new ResponseEntity<String>(rtn, HttpStatus.OK);
    }

}