package com.epam.esm.service;

import com.epam.esm.DTO.response.BaseResponseDto;

import java.util.List;
import java.util.UUID;

public interface BaseService<T> {

    BaseResponseDto<T> create(T t);

    BaseResponseDto<T> get(UUID id);

    BaseResponseDto delete(UUID id);

//    BaseResponseDto<List<T>> getAll();
}
