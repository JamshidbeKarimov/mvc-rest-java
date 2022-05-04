package com.epam.esm.DAO.tag;

import com.epam.esm.model.tag.Tag;
import com.epam.esm.model.tag.TagMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class TagDAOImpl implements TagDAO {

    private JdbcTemplate jdbcTemplate;

    public TagDAOImpl(DataSource dataSource) {
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
    public List<Tag> getAll() {
        String QUERY_GET_ALL = "select * from tag;";

        return jdbcTemplate.query(QUERY_GET_ALL, new TagMapper());
    }

    @Override
    public int delete(UUID tagId) {
        String QUERY_DELETE_TAG = "delete from tag where id = ?;";

        return jdbcTemplate.update(QUERY_DELETE_TAG, tagId);
    }

    @Override
    public void createWithGiftCertificate(UUID certificateId, List<Tag> tags) {
        String QUERY_CREATE_TAG = "insert into tag (id, name) values(?, ?);";
        String QUERY_CREATE_CONNECTION
                = "insert into gift_certificate_tag (tag_id, gift_certificate_id) values (?, ?);";

        tags.stream().forEach(tag -> {
            tag.setId(UUID.randomUUID());
            jdbcTemplate.update(QUERY_CREATE_TAG, tag.getId(), tag.getName());
            jdbcTemplate.update(QUERY_CREATE_CONNECTION,tag.getId(), certificateId);
        });
    }

    @Override
    public List<Tag> getTags(UUID certificateId) {
        String QUERY_GET_TAGS = "select *from get_certificate_tags(?);";

        List<Tag> tagList = jdbcTemplate.query(QUERY_GET_TAGS, new TagMapper(), certificateId);
        return tagList;
    }
}
