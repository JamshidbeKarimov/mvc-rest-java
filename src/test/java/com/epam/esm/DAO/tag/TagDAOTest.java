package com.epam.esm.DAO.tag;

import com.epam.esm.model.tag.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TagDAOTest {

    @InjectMocks
    private TagDAOImpl tagDAO;

    private Tag tag;

    @BeforeAll
    static void beforeAll() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");


        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("create table tag\n" +
                "(\n" +
                "    id uuid not null primary key,\n" +
                "    name character varying\n" +
                ")");

        jdbcTemplate.update(
                "insert into tag values('64eeb184-972c-4bef-9879-c003d7352bd0', 'testTag');\n" +
                        "insert into tag values('badc0e82-f873-491a-b2cc-5ba6580ac71f', 'testTag2');\n" +
                        "insert into tag values('c57e4db1-6ae4-4aee-b0d1-aaee00c26f77', 'testTag3');\n" +
                        "insert into tag values('fce5b289-6029-44cf-a870-5a0c30fd6d83', 'testTag4');");
    }

    @BeforeEach

    void setUp() {
        tag = new Tag(UUID.fromString("64eeb184-972c-4bef-9879-c003d7352bd0"), "testTag");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        tagDAO.setJdbcTemplate(dataSource);
    }

    @Test
    public void canCreateTag(){
        tag.setId(UUID.randomUUID());
        int i = tagDAO.create(tag);

        assertEquals(1, 1);
    }

    @Test
    public void canGetTagById(){
        Tag tag = tagDAO.get(this.tag.getId());
        assertNotNull(tag);
    }

    @Test
    public void canGetTagByName(){
        Tag testTag = tagDAO.getByName("testTag3");
        assertNotNull(testTag);
    }

    @Test
    public void canGetAllTags(){
        List<Tag> all = tagDAO.getAll();
        assertEquals(4, all.size());
    }

    @Test
    public void canDeleteTagById(){
        tag.setId(UUID.fromString("badc0e82-f873-491a-b2cc-5ba6580ac71f"));
        int delete = tagDAO.delete(tag.getId());
        assertEquals(1, delete);
    }

//    private void createTestObjects(){
//        jdbcTemplate.update("create table tag\n" +
//                "(\n" +
//                "    id uuid not null primary key,\n" +
//                "    name character varying\n" +
//                ")");
//
//        jdbcTemplate.update(
//                "insert into tag values('64eeb184-972c-4bef-9879-c003d7352bd0', 'testTag');\n" +
//                "insert into tag values('badc0e82-f873-491a-b2cc-5ba6580ac71f', 'testTag2');\n" +
//                "insert into tag values('c57e4db1-6ae4-4aee-b0d1-aaee00c26f77', 'testTag3');\n" +
//                "insert into tag values('fce5b289-6029-44cf-a870-5a0c30fd6d83', 'testTag4');");
//    }

}