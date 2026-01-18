package it.sdc.restserver.controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/numbers")
@CrossOrigin(origins = "http://localhost:4200")
public class NumberController {

    @GetMapping(value = "random", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getRandom(@RequestParam(defaultValue = "0") Integer min, @RequestParam(defaultValue = "10") Integer max) {
        return String.valueOf(Math.random() * (max - min) + min);
    }

}
