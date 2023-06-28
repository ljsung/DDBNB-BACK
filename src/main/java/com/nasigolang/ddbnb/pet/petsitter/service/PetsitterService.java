package com.nasigolang.ddbnb.pet.petsitter.service;

import com.nasigolang.ddbnb.member.repository.MemberRepository;
import com.nasigolang.ddbnb.pet.petmom.dto.PetMomDTO;
import com.nasigolang.ddbnb.pet.petmom.entity.PetMom;
import com.nasigolang.ddbnb.pet.petmom.repositroy.PetMomRepository;
import com.nasigolang.ddbnb.pet.petsitter.dto.PetsitterboardDTO;
import com.nasigolang.ddbnb.pet.petsitter.entity.PetsitterEntity;
import com.nasigolang.ddbnb.pet.petsitter.repository.PetsitterRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;


@Service
public class PetsitterService {

    private final PetsitterRepository petSitterRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    //    public Page<PetsitterboardDTO> findMenuList(Pageable page, String location, String petSize, String care, LocalDate startDate, LocalDate endDate) {
    //
    //        page = PageRequest.of(page.getPageNumber() <= 0 ? 0 : page.getPageNumber() - 1, page.getPageSize(), Sort.by("boardId"));
    //
    //        return petSitterRepository.findPetsitterEntityByLocationOrPetSizeOrCareOrStartDateOrEndDate(
    //                page, location, petSize, care, startDate, endDate).map(list -> modelMapper.map(list, PetsitterboardDTO.class));
    //        return petSitterRepository.findAll(page).map(list -> modelMapper.map(list,PetsitterboardDTO.class));
    //    }

//    public Page<PetsitterboardDTO> findMenuList(Pageable page, String location, String petSize, String care,
//                                                LocalDate startDate, LocalDate endDate) {
//        page = PageRequest.of(page.getPageNumber() <= 0 ? 0 : page.getPageNumber() - 1, page.getPageSize(), Sort.by("boardId")
//                                                                                                                .descending());
//
//        if(location != null && !location.isEmpty() || petSize != null && !petSize.isEmpty() || care != null && !care.isEmpty() || startDate != null || endDate != null) {
//            return petSitterRepository.findByPetsitterEntityContainingAndLocationContainingAndPetSizeContainingAndCareContainingAndStartDateContainingAndEndDate(page, location, petSize, care, startDate, endDate)
//                                      .map(list -> modelMapper.map(list, PetsitterboardDTO.class));
//        } else {
//            return petSitterRepository.findAll(page).map(list -> modelMapper.map(list, PetsitterboardDTO.class));
//        }
//    }
public Page<PetsitterboardDTO> findMenuList(Pageable page, String location, String petSize, String care,
                                            LocalDate startDate, LocalDate endDate, String sitterStatus) {
    Pageable pageable = PageRequest.of(page.getPageNumber() <= 0 ? 0 : page.getPageNumber() - 1,
            page.getPageSize(), Sort.by("boardId").descending());

    if (location != null && !location.isEmpty() || petSize != null && !petSize.isEmpty()
            || care != null && !care.isEmpty() || startDate != null || endDate != null) {
        return petSitterRepository.findByLocationContainingAndPetSizeContainingAndCareContainingAndStartDateContainingAndEndDateContainingAndSitterStatus(
                        pageable, location, petSize, care, startDate, endDate, sitterStatus)
                .map(list -> modelMapper.map(list, PetsitterboardDTO.class));
    } else {
        return petSitterRepository.findAll(pageable)
                .map(list -> modelMapper.map(list, PetsitterboardDTO.class));
    }
}

    @Transactional
    public void registPetSitter(PetsitterboardDTO petSitter) {
        petSitter.setBoardDate(LocalDate.now());
        petSitterRepository.save(modelMapper.map(petSitter, PetsitterEntity.class));
    }

    @Transactional
    public void modifyPetSitter(PetsitterboardDTO modifypetsitter) {

        PetsitterEntity foundPetsitter = petSitterRepository.findById(modifypetsitter.getBoardId()).get();

        foundPetsitter.setBoardTitle(modifypetsitter.getBoardTitle());
        foundPetsitter.setLocation(modifypetsitter.getLocation());
        foundPetsitter.setCare(modifypetsitter.getCare());
        foundPetsitter.setStartDate(modifypetsitter.getStartDate());
        foundPetsitter.setEndDate(modifypetsitter.getEndDate());
        foundPetsitter.setSignficant(modifypetsitter.getSignficant());
        foundPetsitter.setRequest(modifypetsitter.getRequest());
        foundPetsitter.setPetName(modifypetsitter.getPetName());
        foundPetsitter.setPetAge(modifypetsitter.getPetAge());
        foundPetsitter.setPetShape(modifypetsitter.getPetShape());
        foundPetsitter.setPetGender(modifypetsitter.getPetGender());
        foundPetsitter.setPetSize(modifypetsitter.getPetSize());

    }

    @Transactional
    public void deletePetSitter(Long borderId) {
        petSitterRepository.deleteById(borderId);
    }

    public PetsitterboardDTO findPetsitterByBoardNo(Long boardId) {

        return petSitterRepository.findById(boardId)
                                  .map(petsitterboard -> modelMapper.map(petsitterboard, PetsitterboardDTO.class))
                                  .orElseThrow(() -> new NoSuchElementException("펫시터를 찾을 수 없습니다."));
    }


    @Transactional
    public void updateSitterCancle(PetsitterboardDTO sitterCancle, long boardId) {
        PetsitterEntity petsitter = petSitterRepository.findById(boardId).get();

        petsitter.setSitterStatus(sitterCancle.getSitterStatus());

    }
    //내 펫시터 조회
    public Page<PetsitterboardDTO> findMyPetSitter(Pageable page, long memberId) {
        page = PageRequest.of(page.getPageNumber() <= 0 ? 0 : page.getPageNumber() - 1, page.getPageSize(), Sort.by("boardId"));

        return petSitterRepository.findByMember(page, memberRepository.findById(memberId))
                .map(petSitter -> modelMapper.map(petSitter, PetsitterboardDTO.class));
    }
}
