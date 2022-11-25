package but.learspring.rent.api;

import but.learspring.rent.model.Covenant;
import but.learspring.rent.service.CovenantService;
import but.learspring.rent.service.CovenantServiceImp;
import but.learspring.rent.template.ResponseTemplate;
import but.learspring.rent.template.Users;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/covenant")
public class CovenantController {
    @Autowired
    private CovenantService service;

    private static final String RENT_SERVICE = "rentService";
    @Value("${service.user.url}")
    private String userServiceUrl;

    @PostMapping("/")
    public Covenant addCovenant(@RequestBody Covenant covenant) {
        return service.saveCovenant(covenant);
    }

    //    @CircuitBreaker(name = RENT_SERVICE , fallbackMethod = "fallback")
    @Retry(name = RENT_SERVICE)
    //@TimeLimiter(name = RENT_SERVICE)
    @GetMapping("/")
    @Cacheable(value = "Covenants")
    public ResponseEntity<List<Covenant>> getCovenants(
            HttpServletRequest httpServletRequest) throws IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        Users user = null;
        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url( userServiceUrl+"/api/user/info/")
                    .get()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                return ResponseEntity.status(HttpStatus.OK).body(service.getAllCovenants());
            }
        }
        log.error("invalid token");
        return null;
    }

    //    @CircuitBreaker(name = RENT_SERVICE , fallbackMethod = "fallback")
    @Retry(name = RENT_SERVICE)
//    @TimeLimiter(name = RENT_SERVICE)
    @GetMapping("/{covenantId}")
    @Cacheable(value = "CovenantDetails")
    public ResponseEntity<ResponseTemplate> getCovenantWithUserDepartment(@PathVariable Long covenantId
            ,HttpServletRequest httpServletRequest ) throws IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        Users user = null;
        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url( userServiceUrl+"/api/user/info/")
                    .get()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                ResponseTemplate r = service.getCovenantUserDepartment(covenantId);
                if (r !=null) {
                    return ResponseEntity.status(HttpStatus.OK).body(r);
                }
            }
        }
        log.error("invalid token");
        return  null;
    }
    @GetMapping("/byId/{covenantId}")
    @Cacheable(value = "covenantById")
    public Covenant getCovenantById(@PathVariable Long covenantId
            ,HttpServletRequest httpServletRequest) throws IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        Users user = null;
        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url( userServiceUrl+"/api/user/info/")
                    .get()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                    return service.getCovenantById(covenantId);
            }
        }
        log.error("invalid token");
        return  null;
    }

    @DeleteMapping("/{covenantId}")
    public ResponseEntity<String> deleteCovenant(@PathVariable Long covenantId
            ,@RequestHeader(value = "Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        if (token == null || !authorizationHeader.startsWith("Bearer")){
            log.error("token invalid");
            return null;
        }
        log.info("deleting covenant:" + covenantId);
        service.deleteCovenant(covenantId);
        return ResponseEntity.status(HttpStatus.OK).body("deleted successful");
    }

    public String fallback() {
        return "something go wrong";
    }

    public String retryFallback() {
        return "this is retry fallback";
    }

    public String timeLimiterFallback() {
        return "this is time limiter fallback";
    }

}
