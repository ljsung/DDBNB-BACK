package com.nasigolang.ddbnb.pet.Applicant.controller;

import com.nasigolang.ddbnb.common.ResponseDto;
import com.nasigolang.ddbnb.common.paging.Pagenation;
import com.nasigolang.ddbnb.common.paging.ResponseDtoWithPaging;
import com.nasigolang.ddbnb.common.paging.SelectCriteria;
import com.nasigolang.ddbnb.pet.Applicant.dto.ApplicantDTO;
import com.nasigolang.ddbnb.pet.Applicant.service.ApplicantService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/v1/applicant")
@AllArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;

    @GetMapping("/{boardId}")
    @ApiOperation(value="신청자 목록 조회")
    public ResponseEntity<ResponseDto> findApplicantList(@PageableDefault Pageable page,
                                                         @PathVariable long boardId){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Page<ApplicantDTO> applicantList = applicantService.findApplicantList(page,boardId);
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(applicantList);

        ResponseDtoWithPaging data = new ResponseDtoWithPaging(applicantList.getContent(), selectCriteria);

        return ResponseEntity.ok().headers(headers).body(new ResponseDto(HttpStatus.OK, "조회 성공", data));
    }


    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> findApplicant(@RequestBody ApplicantDTO applicant) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        applicantService.registApplicant(applicant);

        return ResponseEntity.ok().headers(headers).body(new ResponseDto(HttpStatus.OK, "생성 성공", null));

    }
}
