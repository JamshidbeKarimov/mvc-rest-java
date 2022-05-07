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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDAO tagDAO;
    private final ModelMapper modelMapper;

    @Override
    public BaseResponseDto<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setId(UUID.randomUUID());
        int create = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
        if (create == 1) {
            createTags(giftCertificateDto);
            return new BaseResponseDto<>(1, "success");
        }
        return new BaseResponseDto<>(-1, "failed to create gift certificate");
    }

    @Override
    public BaseResponseDto<GiftCertificateDto> get(UUID certificateId) {
        GiftCertificate giftCertificate = giftCertificateDAO.get(certificateId);
        GiftCertificateDto certificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        certificateDto.setTags(tagDAO.getTags(certificateId));
        return new BaseResponseDto<>(1, "success", certificateDto);
    }


    // sorting should be in database * done
    @Override
    public BaseResponseDto<List<GiftCertificateDto>> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending
    ) {
        List<GiftCertificate> certificateList = giftCertificateDAO.searchAndGetByTagName(
                searchWord, tagName, doNameSort, doDateSort, isDescending);

        if(certificateList.size() == 0)
            return new BaseResponseDto<>(0, "no certificates found");

        List<GiftCertificateDto> giftCertificateDtos = convertToDto(certificateList);
        return new BaseResponseDto<>(1, "success", addTags(giftCertificateDtos));
    }

    @Override
    public BaseResponseDto delete(UUID id) {
        int delete = giftCertificateDAO.delete(id);
        if (delete == 1) {
            return new BaseResponseDto(1, "certificate deleted");
        }
        return new BaseResponseDto(0, "this certificate doesn't exist");
    }

    @Override
    public BaseResponseDto update(GiftCertificateDto update) {
        GiftCertificate old = giftCertificateDAO.get(update.getId());
        update.setLastUpdateDate(
                ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT)
        );
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(update, old);
        int result = giftCertificateDAO.update(old);
        if (result == 1) {
            createTags(update);
            return new BaseResponseDto(1, "certificate updated");
        }
        return new BaseResponseDto(0, "cannot update certificate");
    }

    List<GiftCertificateDto> convertToDto(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map((certificate) ->
                        modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }


    List<GiftCertificateDto> addTags(List<GiftCertificateDto> certificateDtos) {
        return certificateDtos.stream().peek(certificate -> certificate.setTags(tagDAO.getTags(certificate.getId())))
                .collect(Collectors.toList());
    }

    void createTags(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getTags() != null)

            //method should be here
            tagDAO.createWithGiftCertificate(giftCertificateDto.getId(), giftCertificateDto.getTags());
    }

}
