package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IFactRepository
import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.IFactService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FactService implements IFactService {
    private IFactRepository factRepository

    @Autowired
    FactService(IFactRepository factRepository) {
        this.factRepository = factRepository
    }

    @Override
    List<Fact> findForAmenity(Amenity findForAmenity) {
        return this.factRepository.findByAmenity(findForAmenity)
    }

    @Override
    List<Fact> findAll() {
        return this.factRepository.findAll() as List<Fact>
    }

    @Override
    Fact save(Fact fact) {
        return factRepository.save(fact)
    }
}
