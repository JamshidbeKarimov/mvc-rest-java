package com.epam.esm.service.gift_certificate;

import com.epam.esm.DAO.gift_certificate.GiftCertificateDAO;
import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.model.gift_certificate.GiftCertificate;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService{

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDAO tagDAO;
    private final ModelMapper modelMapper;


    @Override
    public BaseResponseDto<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setCreateDate(getTime());
        giftCertificateDto.setId(UUID.randomUUID());

        int create = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));

        if(create == 1 ) {
            createTags(giftCertificateDto);
            return new BaseResponseDto<>(1, "success");
        }

        return new BaseResponseDto<>(0, "failed to create gift certificate");
    }

    @Override
    public BaseResponseDto<GiftCertificateDto> get(UUID id) {
        GiftCertificate giftCertificate = giftCertificateDAO.get(id);
        GiftCertificateDto certificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);

        return new BaseResponseDto<>(1, "success", certificateDto);
    }

    @Override
    public BaseResponseDto<List<GiftCertificateDto>> getAll(
            boolean doNameSort, boolean doDateSort, boolean byTagName
    ) {
        List<GiftCertificate> certificateList = giftCertificateDAO.getAll();

        if (certificateList.size() == 0)
            return new BaseResponseDto<>(0, "You haven't created Gift Certificates yet");

        return new BaseResponseDto<>(1, "success", addTags(convertToDto(certificateList)));
    }

    @Override
    public BaseResponseDto delete(UUID id) {
        int delete = giftCertificateDAO.delete(id);

        if(delete == 1)
            return new BaseResponseDto(1, "certificate deleted");

        return new BaseResponseDto(0, "this certificate doesn't exist");
    }

    @Override
    public BaseResponseDto update(GiftCertificateDto update) {
        GiftCertificate old = giftCertificateDAO.get(update.getId());
        update.setLastUpdateDate(getTime());

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(update, old);

        int result = giftCertificateDAO.update(old);

        if(result == 1) {
            createTags(update);
            return new BaseResponseDto(1, "certificate updated");
        }

        return new BaseResponseDto(0, "cannot update certificate");
    }


    @Override
    public BaseResponseDto<List<GiftCertificateDto>> getByTagName(String tagName) {
        List<GiftCertificate> certificateList = giftCertificateDAO.getByTagName(tagName);

        if (certificateList.size() == 0)
            return new BaseResponseDto<>(0, "no certificate found with this tag");

        return new BaseResponseDto<>(1, "success", addTags(convertToDto(certificateList)));
    }

    @Override
    public BaseResponseDto<List<GiftCertificateDto>> searchByDescriptionOrName(String keyWord) {
        List<GiftCertificate> certificateList = giftCertificateDAO.search(keyWord);

        if (certificateList.size() == 0)
            return new BaseResponseDto<>(0, "no match found");

        return new BaseResponseDto<>(1, "success", addTags(convertToDto(certificateList)));
    }

    @Override
    public BaseResponseDto<List<GiftCertificateDto>> sortByName() {
        List<GiftCertificate> certificates = giftCertificateDAO.getAll();

        certificates.sort(
                Comparator.comparing(GiftCertificate::getName));

        return new BaseResponseDto<>(1, "success", addTags(convertToDto(certificates)));
    }

    @Override
    public BaseResponseDto<List<GiftCertificateDto>> sortByDate() {
        List<GiftCertificate> certificates = giftCertificateDAO.getAll();

        certificates.sort(
                Comparator.comparing((GiftCertificate c) -> parseTime(c.getCreateDate())));

        return new BaseResponseDto<>(1, "success", addTags(convertToDto(certificates)));
    }



    private String getTime(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private Date parseTime(String time){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        try {
            return df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<GiftCertificateDto> convertToDto(List<GiftCertificate> certificates){
        return certificates.stream()
                .map((certificate) ->
                        modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    private List<GiftCertificateDto> addTags(List<GiftCertificateDto> certificateDtos){
        return certificateDtos.stream().map(certificate -> {
            certificate.setTags(tagDAO.getTags(certificate.getId()));
            return certificate;
        }).collect(Collectors.toList());
    }

    private void createTags(GiftCertificateDto giftCertificateDto){
        if(giftCertificateDto.getTags() != null)
            tagDAO.createWithGiftCertificate(giftCertificateDto.getId(), giftCertificateDto.getTags());
    }

}
