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

        return new BaseResponseDto<>(0, "failed to create gift certificate");
    }

    @Override
    public BaseResponseDto<GiftCertificateDto> get(UUID certificateId) {
        GiftCertificate giftCertificate = giftCertificateDAO.get(certificateId);
        GiftCertificateDto certificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        certificateDto.setTags(tagDAO.getTags(certificateId));

        return new BaseResponseDto<>(1, "success", certificateDto);
    }

//    @Override
//    public BaseResponseDto<List<GiftCertificateDto>> getAll(
//           String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending
//    ) {
//        List<GiftCertificate> certificateList = giftCertificateDAO.getAll();
//
//        if (certificateList.size() == 0)
//            return new BaseResponseDto<>(0, "You haven't created Gift Certificates yet");
//
//        return new BaseResponseDto<>(1, "success", addTags(convertToDto(certificateList)));
//    }

    @Override
    public BaseResponseDto<List<GiftCertificateDto>> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending
    ) {

        List<GiftCertificate> certificateList = getCertificateList(searchWord, tagName);
        List<GiftCertificateDto> giftCertificateDtos = convertToDto(sortCertificateList(
                doNameSort, doDateSort, isDescending, certificateList
        ));
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




    private List<GiftCertificateDto> convertToDto(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map((certificate) ->
                        modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    private List<GiftCertificate> getCertificateList(String searchWord, String tagName) {
        if (searchWord != null) {
            if (tagName != null) {
                tagName = tagName.toLowerCase();
                return giftCertificateDAO.searchAndGetByTagName(searchWord, tagName);
            }
            return giftCertificateDAO.search(searchWord);
        } else if (tagName != null)
                return giftCertificateDAO.getByTagName(tagName);

        return giftCertificateDAO.getAll();
    }

    private List<GiftCertificate> sortCertificateList(
            boolean doNameSort, boolean doDateSort, boolean isDescending, List<GiftCertificate> certificateList) {

        if (doNameSort) {
            if (doDateSort)
                certificateList.sort(
                        Comparator.comparing(GiftCertificate::getName)
                                .thenComparing(c -> ZonedDateTime.parse(c.getCreateDate())));
            else
                certificateList.sort(Comparator.comparing(GiftCertificate::getName));
        } else if (doDateSort)
            certificateList.sort(
                    Comparator.comparing((GiftCertificate c) -> ZonedDateTime.parse(c.getCreateDate())));

        if(isDescending)
            Collections.reverse(certificateList);

        return certificateList;
    }

    private List<GiftCertificateDto> addTags(List<GiftCertificateDto> certificateDtos) {
        return certificateDtos.stream().peek(certificate -> certificate.setTags(tagDAO.getTags(certificate.getId())))
                .collect(Collectors.toList());
    }

    private void createTags(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getTags() != null)
            tagDAO.createWithGiftCertificate(giftCertificateDto.getId(), giftCertificateDto.getTags());
    }

}
