package but.learspring.rent.repo;

import but.learspring.rent.model.Covenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CovenantRepo extends JpaRepository<Covenant,Long> {
    Covenant findByCovenantId(Long covenantId);
}
