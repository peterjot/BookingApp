package pl.deadwood.bookingapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    public static final String API_V1 = "application/pl.deadwood.v1+json";
    public static final String API_V2 = "application/pl.deadwood.v2+json";

    @GetMapping("")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping(path = "version", produces = API_V1)
    public ResponseEntity<String> version1() {
        return ResponseEntity.ok(API_V1);
    }

    @GetMapping(path = "version", produces = API_V2)
    public ResponseEntity<String> version2() {
        return ResponseEntity.ok(API_V1);
    }
}
