package com.epam.esm.controller;

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


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(
            @RequestBody GiftCertificateDto giftCertificate
            ){
        return ResponseEntity.ok(giftCertificateService.create(giftCertificate));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> get(
            @RequestParam UUID id
    ){
        return ResponseEntity.ok(giftCertificateService.get(id));
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) String byTagName,
            @RequestParam(required = false) boolean doNameSort,
            @RequestParam(required = false) boolean doDateSort,
            @RequestParam(required = false) boolean isDescending
    ){
        return ResponseEntity.ok(giftCertificateService.getAll(
               searchWord, byTagName, doNameSort, doDateSort, isDescending
            ));
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

}
