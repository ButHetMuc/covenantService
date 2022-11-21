package but.learspring.rent.service;


import but.learspring.rent.model.Covenant;
import but.learspring.rent.template.ResponseTemplate;


import java.util.List;


public interface CovenantService {
    public Covenant saveCovenant(Covenant covenant);
    public ResponseTemplate getCovenantUserDepartment(Long covenantId);
    public List<Covenant> getAllCovenants();
    public Covenant getCovenantById(Long covenantId);
    public boolean deleteCovenant(Long covenantId);
}
