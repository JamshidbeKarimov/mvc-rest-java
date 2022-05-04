package com.epam.esm.controller;

import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.service.gift_certificate.GiftCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/gift_certificate")
@AllArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final TagDAO tagDAO;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(
            @RequestBody GiftCertificateDto giftCertificate
            ){
//        if(giftCertificate.getTags() != null && creating.getStatus() == 1){
//            tagDAO.createWithGiftCertificate(giftCertificate.getId(), giftCertificate.getTags());
//        }

        return ResponseEntity.ok(giftCertificateService.create(giftCertificate));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> get(
            @RequestParam UUID id
    ){
        return ResponseEntity.ok(
                giftCertificateService.get(id));
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) boolean doNameSort,
            @RequestParam(required = false) boolean doDateSort,
            @RequestParam(required = false) boolean byTagName
    ){
        return ResponseEntity.ok(giftCertificateService.getAll(
                doNameSort, doDateSort, byTagName));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(
            @RequestParam UUID id
    ){
        return ResponseEntity.ok(giftCertificateService.delete(id));
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<?> update(
            @RequestBody GiftCertificateDto update
    ){
        return ResponseEntity.ok(giftCertificateService.update(update));
    }

    @RequestMapping(value = "/tag_name", method = RequestMethod.GET)
    public ResponseEntity<?> getByTagName(
            @RequestParam String tagName
    ){
        return ResponseEntity.ok(giftCertificateService.getByTagName(tagName));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchByDescriptionOrName(
            @RequestParam String keyWord
    ){
        return ResponseEntity.ok(giftCertificateService.searchByDescriptionOrName(keyWord));
    }

    @RequestMapping(value = "/name_sort", method = RequestMethod.GET)
    public ResponseEntity<?> sortByName(){
        return ResponseEntity.ok(giftCertificateService.sortByName());
    }

    @RequestMapping(value = "/date_sort", method = RequestMethod.GET)
    public ResponseEntity<?> sortByDate(){
        return ResponseEntity.ok(giftCertificateService.sortByDate());
    }







    


}
