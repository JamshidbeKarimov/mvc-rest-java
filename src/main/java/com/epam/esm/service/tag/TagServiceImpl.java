package com.epam.esm.service.tag;

import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.model.tag.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagDAO tagDAO;

    @Override
    public BaseResponseDto<Tag> create(Tag tag) {
        tag.setName(tag.getName().toLowerCase());
        if(checkExistence(tag.getName()) != null)
            return new BaseResponseDto<>(0, "tag with this name already exists");

        tag.setId(UUID.randomUUID());
        int create = tagDAO.create(tag);
        if(create == 1 )
            return new BaseResponseDto<>(1, "success");
        return new BaseResponseDto<>(0, "failed to create gift certificate");
    }

    @Override
    public BaseResponseDto<Tag> get(UUID tagId) {
        Tag tag = tagDAO.get(tagId);
        return new BaseResponseDto<>(1, "success", tag);
    }

    @Override
    public BaseResponseDto<List<Tag>> getAll() {
        return new BaseResponseDto<>(1, "success", tagDAO.getAll());
    }

    @Override
    public BaseResponseDto delete(UUID tagId) {
        int delete = tagDAO.delete(tagId);
        if(delete == 1)
            return new BaseResponseDto(1, "certificate deleted");
        return new BaseResponseDto(0, "cannot delete certificate");
    }

    private Tag checkExistence(String name){
        return tagDAO.getByName(name);
    }

}
