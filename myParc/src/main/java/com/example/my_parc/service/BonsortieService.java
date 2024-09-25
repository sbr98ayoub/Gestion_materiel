package com.example.my_parc.service;

import com.example.my_parc.domain.Bonsortie;
import com.example.my_parc.domain.Materiel;
import com.example.my_parc.domain.User;
import com.example.my_parc.model.BonsortieDTO;
import com.example.my_parc.model.Status;
import com.example.my_parc.repos.BonsortieRepository;
import com.example.my_parc.repos.MaterielRepository;
import com.example.my_parc.repos.UserRepository;
import com.example.my_parc.util.NotFoundException;
import com.example.my_parc.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BonsortieService {

    private final BonsortieRepository bonsortieRepository;
    private final UserRepository userRepository;
    private final MaterielRepository materielRepository;

    public BonsortieService(final BonsortieRepository bonsortieRepository,
                            final UserRepository userRepository, final MaterielRepository materielRepository) {
        this.bonsortieRepository = bonsortieRepository;
        this.userRepository = userRepository;
        this.materielRepository = materielRepository;
    }

    public List<BonsortieDTO> findAll() {
        final List<Bonsortie> bonsorties = bonsortieRepository.findAll(Sort.by("numero"));
        return bonsorties.stream()
                .map(bonsortie -> mapToDTO(bonsortie, new BonsortieDTO()))
                .toList();
    }

    public BonsortieDTO get(final Long numero) {
        return bonsortieRepository.findById(numero)
                .map(bonsortie -> mapToDTO(bonsortie, new BonsortieDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BonsortieDTO bonsortieDTO, List<Long> materielIds) {
        final Bonsortie bonsortie = new Bonsortie();
        mapToEntity(bonsortieDTO, bonsortie);

        // Sauvegarder le bon de sortie
        Long savedNumero = bonsortieRepository.save(bonsortie).getNumero();

        // Mettre à jour le statut des matériels en "OUT_OF_STOCK"
        for (Long materielId : materielIds) {
            Materiel materiel = materielRepository.findById(materielId)
                    .orElseThrow(() -> new NotFoundException("Matériel non trouvé"));
            materiel.setStatus(Status.OUT_OF_STOCK);
            materielRepository.save(materiel); // Sauvegarder le matériel mis à jour
        }

        return savedNumero;
    }

    public void update(final Long numero, final BonsortieDTO bonsortieDTO) {
        final Bonsortie bonsortie = bonsortieRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bonsortieDTO, bonsortie);
        bonsortieRepository.save(bonsortie);
    }

    public void delete(final Long numero) {
        bonsortieRepository.deleteById(numero);
    }

    private BonsortieDTO mapToDTO(final Bonsortie bonsortie, final BonsortieDTO bonsortieDTO) {
        bonsortieDTO.setNumero(bonsortie.getNumero());
        bonsortieDTO.setDateSortie(bonsortie.getDateSortie());
        bonsortieDTO.setRecepteur(bonsortie.getRecepteur());
        bonsortieDTO.setEmetteur(bonsortie.getEmetteur());
        bonsortieDTO.setObservations(bonsortie.getObservations());
        bonsortieDTO.setUserId(bonsortie.getUserId() == null ? null : bonsortie.getUserId().getId());
        return bonsortieDTO;
    }

    private Bonsortie mapToEntity(final BonsortieDTO bonsortieDTO, final Bonsortie bonsortie) {
        bonsortie.setDateSortie(bonsortieDTO.getDateSortie());
        bonsortie.setRecepteur(bonsortieDTO.getRecepteur());
        bonsortie.setEmetteur(bonsortieDTO.getEmetteur());
        bonsortie.setObservations(bonsortieDTO.getObservations());
        final User userId = bonsortieDTO.getUserId() == null ? null : userRepository.findById(bonsortieDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        bonsortie.setUserId(userId);
        return bonsortie;
    }

    public ReferencedWarning getReferencedWarning(final Long numero) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Bonsortie bonsortie = bonsortieRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        final Materiel bonSortieMateriel = materielRepository.findFirstByBonSortiesContains(bonsortie);
        if (bonSortieMateriel != null) {
            referencedWarning.setKey("bonsortie.materiel.bonSortie.referenced");
            referencedWarning.addParam(bonSortieMateriel.getCode());
            return referencedWarning;
        }
        return null;
    }
}
