package com.nasigolang.ddbnb.sample.service;

import com.nasigolang.ddbnb.sample.dto.SampleDTO;
import com.nasigolang.ddbnb.sample.entity.Sample;
import com.nasigolang.ddbnb.sample.repository.SampleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;
    private final ModelMapper modelMapper;

    public Page<SampleDTO> findMenuList(Pageable page) {

        page = PageRequest.of(page.getPageNumber() <= 0 ? 0 : page.getPageNumber() - 1, page.getPageSize(), Sort.by("memberId"));

        return sampleRepository.findAll(page).map(sample -> modelMapper.map(sample, SampleDTO.class));
    }

    @Transactional
    public void registNewMember(SampleDTO newMember) {

        sampleRepository.save(modelMapper.map(newMember, Sample.class));
    }

    @Transactional
    public void modifySample(SampleDTO sample) {

        Sample foundSample = sampleRepository.findById(sample.getMemberId()).get();

        foundSample.setNickname(sample.getNickname());
    }

    @Transactional
    public void deleteSample(int memberId) {
        sampleRepository.deleteById(memberId);
    }
}