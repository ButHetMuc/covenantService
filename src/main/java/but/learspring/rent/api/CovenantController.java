package but.learspring.rent.api;

import but.learspring.rent.model.Covenant;
import but.learspring.rent.service.CovenantServiceImp;
import but.learspring.rent.template.ResponseTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/covenant")
public class CovenantController {
    @Autowired
    private CovenantServiceImp service;

    private static final String RENT_SERVICE = "rentService";

    int count =1;

    @PostMapping("/")
    public Covenant addUser(@RequestBody Covenant covenant){
        return service.saveCovenant(covenant);
    }

//    @CircuitBreaker(name = RENT_SERVICE , fallbackMethod = "fallback")
//    @Retry(name = RENT_SERVICE)
//    @TimeLimiter(name = RENT_SERVICE)
    @GetMapping("/")
    public ResponseEntity<List<Covenant>> getCovenants(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllCovenants());
    }

//    @CircuitBreaker(name = RENT_SERVICE , fallbackMethod = "fallback")
    @Retry(name = RENT_SERVICE)
//    @TimeLimiter(name = RENT_SERVICE)

    @GetMapping("/{covenantId}")
    public ResponseEntity<ResponseTemplate> getCovenantWithUserDepartment(@PathVariable Long covenantId){
        log.info("retry method called" + count++ +" time at " + new Date());
        ResponseTemplate r = service.getCovenantUserDepartment(covenantId);
        if (r == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }else {
            return  ResponseEntity.status(HttpStatus.OK).body(r);
        }

    }
    @DeleteMapping("/{covenantId}")
    public ResponseEntity<String> deleteCovenant(@PathVariable Long covenantId){
        log.info("deleting covenant:"  + covenantId);
        service.deleteCovenant(covenantId);
        return ResponseEntity.status(HttpStatus.OK).body("deleted successful");
    }

    public String fallback (){
        return "something go wrong";
    }

    public String retryFallback(){
        return "this is retry fallback";
    }
    public String timeLimiterFallback(){
        return "this is time limiter fallback";
    }

}
