package com.epam.esm.DTO;


import com.epam.esm.model.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiftCertificateDto {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;
    private List<Tag> tags;

}
