package but.learspring.rent.service;

import but.learspring.rent.model.Covenant;
import but.learspring.rent.repo.CovenantRepo;
import but.learspring.rent.template.Department;
import but.learspring.rent.template.Payment;
import but.learspring.rent.template.ResponseTemplate;
import but.learspring.rent.template.Users;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CovenantServiceImp implements CovenantService {

    @Autowired
    private CovenantRepo repo;

    @Autowired
    private RestTemplate restTemplate;
    private Gson gson = new Gson();
    private JsonObject jsonObject;
    @Value("service.department.url")
    private String departmentServiceUrl;

    @Value("service.user.url")
    private String userServiceUrl;


    //save covenant
    @Override
    public Covenant saveCovenant(Covenant covenant){
        log.info("saving covenant {}");
        return  repo.save(covenant);
    }

    @Override
    public List<Covenant> getAllCovenants() {
        log.info("fetching all covenants");
        return repo.findAll();
    }

    @Override
    public Covenant getCovenantById(Long covenantId) {
        return repo.findByCovenantId(covenantId);
    }

    @Override
    public boolean deleteCovenant(Long covenantId) {
        try{
            repo.deleteById(covenantId);
            log.info("deleted covenant: "+covenantId);
            return true;
        }catch (Exception e){
            log.info("oops! something go wrong");
            e.printStackTrace();
            return false;
        }
    }

    //get covenant with user and department
    public ResponseTemplate getCovenantUserDepartment(Long covenantId){
        log.info("fetching covenant with user and department details");
        try{
            ResponseTemplate rt = new ResponseTemplate();
            Covenant covenant = repo.findByCovenantId(covenantId);
            System.out.println(covenant.toString());

            String u = restTemplate
                    .getForObject(userServiceUrl+"/api/user/" + covenant.getUserId(), String.class);
            System.out.println(u);
            jsonObject = gson.fromJson(u,JsonObject.class);
            String us = jsonObject.get("data").toString();
            Users user = gson.fromJson(us,Users.class);
            System.out.println(user);

            String d = restTemplate
                    .getForObject(departmentServiceUrl+"/api/product/" + covenant.get_id(), String.class);
//            System.out.println(d);

            jsonObject = gson.fromJson(d, JsonObject.class);
            String depart = jsonObject.get("home").toString();
//            System.out.println(depart);
            Department department = gson.fromJson(depart, Department.class);
            System.out.println(department);

            rt.setCovenant(covenant);
            rt.setDepartment(department);
            rt.setUser(user);


            return rt;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
