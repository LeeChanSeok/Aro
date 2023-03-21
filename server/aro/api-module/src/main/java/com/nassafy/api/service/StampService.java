package com.nassafy.api.service;

import com.nassafy.api.dto.req.StampDiaryReqDTO;
import com.nassafy.api.dto.res.StampDiaryResDTO;
import com.nassafy.api.util.S3Util;
import com.nassafy.core.DTO.MapStampDTO;
import com.nassafy.core.DTO.RegisterStampDTO;
import com.nassafy.core.entity.Attraction;
import com.nassafy.core.entity.Member;
import com.nassafy.core.entity.Stamp;
import com.nassafy.core.entity.StampImage;
import com.nassafy.core.respository.AttractionRepository;
import com.nassafy.core.respository.MemberRepository;
import com.nassafy.core.respository.StampImageRepository;
import com.nassafy.core.respository.StampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StampService {
    @Autowired
    private StampRepository stampRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 회원 가입 때 스탬프 조회
    public List<RegisterStampDTO> findStampsCountry(String countryName) {
        List<Attraction> attractions = attractionRepository.findByNation(countryName);
        List<RegisterStampDTO> registerStampDTOS = new ArrayList<>();

        for (Attraction attraction : attractions) {
            registerStampDTOS.add(new RegisterStampDTO(attraction.getColorStamp(), attraction.getAttractionName(), attraction.getDescription()));
        }
        return registerStampDTOS;
    }

    @Autowired
    private StampImageRepository stampImageRepository;

    @Autowired
    private S3Util s3Util;

    public List<MapStampDTO> findStampsByUserAndCountry(Long userId, String countryName) {
        List<Stamp> stamps = stampRepository.findByMemberId(userId);
        List<Attraction> attractions = attractionRepository.findByNation(countryName);

        List<MapStampDTO> mapStamps = new ArrayList<>();
        for (Attraction attraction : attractions) {
            Stamp stamp = stamps.stream()
                    .filter(s -> s.getAttraction().getId().equals(attraction.getId()))
                    .findFirst()
                    .orElse(null);

            if (stamp != null) {
                mapStamps.add(new MapStampDTO(attraction.getColorStamp(), stamp.getCertification()));
            }
        }

        return mapStamps;
    }

    // 자동으로 데이터를 추가하는 로직 회원 가입 이후 로직에서 호출 됨
    @Transactional
    public Integer makeStamp(Long memberId){
        Member member = memberRepository.findById(memberId).
                orElseThrow(
                        () -> new EntityNotFoundException("회원이 없습니다.")
                );
        List<Attraction> attractions = attractionRepository.findAll();
        for (Attraction attraction : attractions){
            Stamp stamp = Stamp.builder()
                    .member(member)
                    .attraction(attraction)
                    .certification(false)
                    .memo("")
                    .build();
            stampRepository.save(stamp);
        }
        return attractions.size();
    }

    public void createStampDiary(String nation, String attraction, Long memberId, StampDiaryReqDTO stampDiaryReqDTO) throws IllegalArgumentException, IOException {

        Stamp stamp = stampRepository
                .findByAttraction_nationAndAttraction_attractionNameAndMemberId(nation, attraction, memberId)
                .orElseThrow(IllegalArgumentException::new);

        stamp.editMemo(stampDiaryReqDTO.getMemo());

        Stamp savedStamp = stampRepository.save(stamp);

        for (MultipartFile file: stampDiaryReqDTO.getFiles()) {
            String imageUrl = s3Util.upload(file, "diary/" + memberId.toString() + "/" + nation + "/" + attraction);

            StampImage stampImage = StampImage.builder().image(imageUrl).stamp(savedStamp).build();

            stampImageRepository.save(stampImage);

            savedStamp.getStampImages().add(stampImage);
        }
    }

    public StampDiaryResDTO getStampDiary(String nation, String attraction, Long memberId) {
        Stamp stamp = stampRepository
                .findByAttraction_nationAndAttraction_attractionNameAndMemberId(nation, attraction, memberId)
                .orElseThrow(IllegalArgumentException::new);
        List<String> stampImages = stamp.getStampImages().stream().map(StampImage::getImage).collect(Collectors.toList());

        return StampDiaryResDTO.builder().images(stampImages).memo(stamp.getMemo()).build();
    }
}