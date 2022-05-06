package com.epam.esm.controller;

import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.model.tag.Tag;
import com.epam.esm.service.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(
            @RequestBody Tag tag
    ){
        return ResponseEntity.ok(tagService.create(tag));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> get(
            @RequestParam UUID id
    ){
        return ResponseEntity.ok(tagService.get(id));
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(
            @RequestParam UUID id
    ){
        return ResponseEntity.ok(tagService.delete(id));
    }
}
