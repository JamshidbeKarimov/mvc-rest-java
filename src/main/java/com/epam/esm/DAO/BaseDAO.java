package com.epam.esm.DAO;

import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;


public interface BaseDAO<T> {


    int create(T t);

    T get(UUID id);

    List<T> getAll();

    int delete(UUID id);

}
