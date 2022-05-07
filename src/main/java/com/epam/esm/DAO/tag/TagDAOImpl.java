package com.epam.esm.DAO.tag;

import com.epam.esm.model.tag.Tag;
import com.epam.esm.model.tag.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class TagDAOImpl implements TagDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int create(Tag tag) {
        String QUERY_CREATE_TAG = "insert into tag (id, name) values(?, ?);";

        return jdbcTemplate.update(QUERY_CREATE_TAG, tag.getId(), tag.getName());
    }

    @Override
    public Tag get(UUID tagId) {
        String QUERY_GET_TAG = "select * from tag where id = ?;";

        return jdbcTemplate.queryForObject(QUERY_GET_TAG, new TagMapper(), tagId);
    }

    @Override
    public Tag getByName(String name) {
        String QUERY_GET_BY_NAME = "select * from tag where name = ?";

        try {
            return jdbcTemplate.queryForObject(QUERY_GET_BY_NAME, new TagMapper(), name);
        }
        catch (EmptyResultDataAccessException e){
            System.out.println("no tag with name: " + name);
        }
        return null;
    }

    @Override
    public List<Tag> getAll() {
        String QUERY_GET_ALL = "select * from tag;";

        return jdbcTemplate.query(QUERY_GET_ALL, new TagMapper());
    }

    @Override
    public int delete(UUID tagId) {
        String QUERY_DELETE_TAG = "delete from tag where id = ?;";

        return jdbcTemplate.update(QUERY_DELETE_TAG, tagId);
    }

    //should be in gift certificate service
    @Override
    public void createWithGiftCertificate(UUID certificateId, List<Tag> tags) {
        String QUERY_CREATE_TAG = "insert into tag (id, name) values(?, ?);";
        String QUERY_CREATE_CONNECTION
                = "insert into gift_certificate_tag (tag_id, gift_certificate_id) values (?, ?);";

        tags.forEach(tag -> {
            Tag byName = getByName(tag.getName());
            if(byName != null){
                jdbcTemplate.update(QUERY_CREATE_CONNECTION,byName.getId(), certificateId);
            }else {
                tag.setId(UUID.randomUUID());
                jdbcTemplate.update(QUERY_CREATE_TAG, tag.getId(), tag.getName());
                jdbcTemplate.update(QUERY_CREATE_CONNECTION, tag.getId(), certificateId);
            }
        });
    }

    @Override
    public List<Tag> getTags(UUID certificateId) {
        String QUERY_GET_TAGS = "select *from get_certificate_tags(?);";

        return jdbcTemplate.query(QUERY_GET_TAGS, new TagMapper(), certificateId);
    }


}
