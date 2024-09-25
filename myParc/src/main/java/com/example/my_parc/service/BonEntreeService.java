package com.example.my_parc.service;

import com.example.my_parc.domain.BonEntree;
import com.example.my_parc.domain.Materiel;
import com.example.my_parc.domain.User;
import com.example.my_parc.model.BonEntreeDTO;
import com.example.my_parc.model.Status;
import com.example.my_parc.repos.BonEntreeRepository;
import com.example.my_parc.repos.MaterielRepository;
import com.example.my_parc.repos.UserRepository;
import com.example.my_parc.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class BonEntreeService {

    private final BonEntreeRepository bonEntreeRepository;
    private final UserRepository userRepository;
    private final MaterielRepository materielRepository;

    public BonEntreeService(final BonEntreeRepository bonEntreeRepository,
                            final UserRepository userRepository,
                            final MaterielRepository materielRepository) {
        this.bonEntreeRepository = bonEntreeRepository;
        this.userRepository = userRepository;
        this.materielRepository = materielRepository;
    }

    public List<BonEntreeDTO> findAll() {
        final List<BonEntree> bonEntrees = bonEntreeRepository.findAll(Sort.by("numero"));
        return bonEntrees.stream()
                .map(bonEntree -> mapToDTO(bonEntree, new BonEntreeDTO()))
                .toList();
    }

    public BonEntreeDTO get(final Long numero) {
        return bonEntreeRepository.findById(numero)
                .map(bonEntree -> mapToDTO(bonEntree, new BonEntreeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(BonEntreeDTO bonEntreeDTO, List<Long> materielIds) {
        BonEntree bonEntree = new BonEntree();
        mapToEntity(bonEntreeDTO, bonEntree);

        // Fetch the selected materials and update their status
        List<Materiel> selectedMateriels = materielRepository.findAllById(materielIds);
        for (Materiel materiel : selectedMateriels) {
            materiel.setStatus(Status.IN_STOCK); // Update to IN_STOCK
            materielRepository.save(materiel);
        }

        bonEntree.setMateriels(new HashSet<>(selectedMateriels));
        return bonEntreeRepository.save(bonEntree).getNumero();
    }



    public void update(final Long numero, final BonEntreeDTO bonEntreeDTO, List<Long> materielIds) {
        final BonEntree bonEntree = bonEntreeRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bonEntreeDTO, bonEntree);

        // Fetch the selected materiels and update their status
        List<Materiel> selectedMateriels = materielRepository.findAllById(materielIds);
        for (Materiel materiel : selectedMateriels) {
            materiel.setStatus(Status.OUT_OF_STOCK);
            materielRepository.save(materiel);
        }

        bonEntree.setMateriels(new HashSet<>(selectedMateriels));
        bonEntreeRepository.save(bonEntree);
    }

    public void delete(final Long numero) {
        bonEntreeRepository.deleteById(numero);
    }

    private BonEntreeDTO mapToDTO(final BonEntree bonEntree, final BonEntreeDTO bonEntreeDTO) {
        bonEntreeDTO.setNumero(bonEntree.getNumero());
        bonEntreeDTO.setDateEntree(bonEntree.getDateEntree());
        bonEntreeDTO.setRecepteur(bonEntree.getRecepteur());
        bonEntreeDTO.setEmetteur(bonEntree.getEmetteur());
        bonEntreeDTO.setObservations(bonEntree.getObservations());
        bonEntreeDTO.setListeArticles(bonEntree.getListeArticles());
        bonEntreeDTO.setUserId(bonEntree.getUserId() == null ? null : bonEntree.getUserId().getId());
        return bonEntreeDTO;
    }

    private BonEntree mapToEntity(final BonEntreeDTO bonEntreeDTO, final BonEntree bonEntree) {
        bonEntree.setDateEntree(bonEntreeDTO.getDateEntree());
        bonEntree.setRecepteur(bonEntreeDTO.getRecepteur());
        bonEntree.setEmetteur(bonEntreeDTO.getEmetteur());
        bonEntree.setObservations(bonEntreeDTO.getObservations());
        bonEntree.setListeArticles(bonEntreeDTO.getListeArticles());
        final User userId = bonEntreeDTO.getUserId() == null ? null : userRepository.findById(bonEntreeDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        bonEntree.setUserId(userId);
        return bonEntree;
    }
}
