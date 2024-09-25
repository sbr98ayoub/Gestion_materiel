package com.example.my_parc.service;

import com.example.my_parc.domain.BonEntree;
import com.example.my_parc.domain.Bonsortie;
import com.example.my_parc.domain.Materiel;
import com.example.my_parc.model.BonEntreeDTO;
import com.example.my_parc.model.BonsortieDTO;
import com.example.my_parc.model.MaterielDTO;
import com.example.my_parc.model.Status;
import com.example.my_parc.repos.BonEntreeRepository;
import com.example.my_parc.repos.BonsortieRepository;
import com.example.my_parc.repos.MaterielRepository;
import com.example.my_parc.util.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaterielService {

    private final MaterielRepository materielRepository;
    private final BonEntreeRepository bonEntreeRepository;
    private final BonsortieRepository bonsortieRepository;
    private final AzureBlobStorageService azureBlobStorageService;

    public MaterielService(final MaterielRepository materielRepository,
                           final BonEntreeRepository bonEntreeRepository,
                           final BonsortieRepository bonsortieRepository,
                           final AzureBlobStorageService azureBlobStorageService) {
        this.materielRepository = materielRepository;
        this.bonEntreeRepository = bonEntreeRepository;
        this.bonsortieRepository = bonsortieRepository;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    public List<MaterielDTO> findAll() {
        final List<Materiel> materiels = materielRepository.findAll();
        return materiels.stream()
                .map(materiel -> mapToDTO(materiel, new MaterielDTO()))
                .collect(Collectors.toList());
    }
    public long countByStatus(Status status) {
        return materielRepository.countByStatus(status);
    }

    public Map<String, Long> countMaterielsByType() {
        return materielRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(materiel -> materiel.getTypeMateriel().getDisplayName(), Collectors.counting()));
    }

    public MaterielDTO get(final Long code) {
        return materielRepository.findById(code)
                .map(materiel -> mapToDTO(materiel, new MaterielDTO()))
                .orElseThrow(NotFoundException::new);
    }
    public List<MaterielDTO> findByStatus(Status status) {
        List<Materiel> materiels = materielRepository.findByStatus(status);
        return materiels.stream()
                .map(materiel -> mapToDTO(materiel, new MaterielDTO()))
                .collect(Collectors.toList());
    }

    public Long create(final MaterielDTO materielDTO, MultipartFile imageFile) {
        String containerName = "materiel";
        String imageUrl = azureBlobStorageService.uploadImage(imageFile, containerName);
        materielDTO.setImageUrl(imageUrl);

        final Materiel materiel = new Materiel();
        mapToEntity(materielDTO, materiel);

        return materielRepository.save(materiel).getCode();
    }

    public void update(final Long code, final MaterielDTO materielDTO, MultipartFile imageFile) {
        final Materiel materiel = materielRepository.findById(code)
                .orElseThrow(NotFoundException::new);

        if (imageFile != null && !imageFile.isEmpty()) {
            String containerName = "materiel";
            String imageUrl = azureBlobStorageService.uploadImage(imageFile, containerName);
            materielDTO.setImageUrl(imageUrl);
        } else {
            materielDTO.setImageUrl(materiel.getImageUrl());
        }

        mapToEntity(materielDTO, materiel);
        materielRepository.save(materiel);
    }

    public void delete(final Long code) {
        materielRepository.deleteById(code);
    }


    public Materiel getMaterielById(Long code) {
        return materielRepository.findById(code).orElse(null); // Assuming you have a repository for Materiel
    }

    private MaterielDTO mapToDTO(final Materiel materiel, final MaterielDTO materielDTO) {
        materielDTO.setCode(materiel.getCode());
        materielDTO.setNumeroSerie(materiel.getNumeroSerie());
        materielDTO.setMarque(materiel.getMarque());
        materielDTO.setModele(materiel.getModele());
        materielDTO.setTypeMateriel(materiel.getTypeMateriel());
        materielDTO.setLibelle(materiel.getLibelle());
        materielDTO.setDescription(materiel.getDescription());
        materielDTO.setAccesoires(materiel.getAccesoires());
        materielDTO.setImageUrl(materiel.getImageUrl());
        materielDTO.setStatus(materiel.getStatus());

//        // Map BonEntrees and Bonsorties to their respective DTOs
//        materielDTO.setBonEntrees(materiel.getBonEntrees().stream()
//                .map(this::mapToBonEntreeDTO)
//                .collect(Collectors.toList()));
//
//        materielDTO.setBonSorties(materiel.getBonSorties().stream()
//                .map(this::mapToBonsortieDTO)
//                .collect(Collectors.toList()));

        return materielDTO;
    }

    private Materiel mapToEntity(final MaterielDTO materielDTO, final Materiel materiel) {
        materiel.setNumeroSerie(materielDTO.getNumeroSerie());
        materiel.setMarque(materielDTO.getMarque());
        materiel.setModele(materielDTO.getModele());
        materiel.setTypeMateriel(materielDTO.getTypeMateriel());
        materiel.setLibelle(materielDTO.getLibelle());
        materiel.setDescription(materielDTO.getDescription());
        materiel.setAccesoires(materielDTO.getAccesoires());
        materiel.setImageUrl(materielDTO.getImageUrl());
        materiel.setStatus(materielDTO.getStatus());

        // Set BonEntrees
//        if (materielDTO.getBonEntrees() != null) {
//            Set<BonEntree> bonEntrees = materielDTO.getBonEntrees().stream()
//                    .map(dto -> bonEntreeRepository.findById(dto.getNumero())
//                            .orElseThrow(() -> new NotFoundException("BonEntree not found")))
//                    .collect(Collectors.toSet());
//            materiel.setBonEntrees(bonEntrees);
//        }
//
//        // Set BonSorties
//        if (materielDTO.getBonSorties() != null) {
//            Set<Bonsortie> bonSorties = materielDTO.getBonSorties().stream()
//                    .map(dto -> bonsortieRepository.findById(dto.getNumero())
//                            .orElseThrow(() -> new NotFoundException("BonSortie not found")))
//                    .collect(Collectors.toSet());
//            materiel.setBonSorties(bonSorties);
//        }

        return materiel;
    }

    private BonEntreeDTO mapToBonEntreeDTO(BonEntree bonEntree) {
        BonEntreeDTO dto = new BonEntreeDTO();
        dto.setNumero(bonEntree.getNumero());
        dto.setDateEntree(bonEntree.getDateEntree());
        dto.setRecepteur(bonEntree.getRecepteur());
        dto.setEmetteur(bonEntree.getEmetteur());
        dto.setObservations(bonEntree.getObservations());
        dto.setListeArticles(bonEntree.getListeArticles());
        dto.setUserId(bonEntree.getUserId().getId());
        return dto;
    }

    private BonsortieDTO mapToBonsortieDTO(Bonsortie bonsortie) {
        BonsortieDTO dto = new BonsortieDTO();
        dto.setNumero(bonsortie.getNumero());
        dto.setDateSortie(bonsortie.getDateSortie());
        dto.setRecepteur(bonsortie.getRecepteur());
        dto.setEmetteur(bonsortie.getEmetteur());
        dto.setObservations(bonsortie.getObservations());
        dto.setUserId(bonsortie.getUserId().getId());
        return dto;
    }
}
