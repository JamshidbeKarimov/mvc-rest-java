package com.epam.esm.DTO;


import com.epam.esm.model.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiftCertificateDto {

    {
        this.createDate = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
    }

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;

    // use LocalDateTime
    private String createDate;
    private String lastUpdateDate;
    private List<Tag> tags;

}
