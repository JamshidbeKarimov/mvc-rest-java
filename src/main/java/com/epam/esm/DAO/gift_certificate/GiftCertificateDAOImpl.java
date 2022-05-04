package com.epam.esm.DAO.gift_certificate;

import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.model.gift_certificate.GiftCertificate;
import com.epam.esm.model.gift_certificate.GiftCertificateMapper;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDAOImpl(DataSource dataSource, ModelMapper modelMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public int create(GiftCertificate certificate) {
        String QUERY_CREATE_CERTIFICATE = "insert into gift_certificate(id, name, description, price, duration," +
                "create_date, last_update_date) values(?, ?, ?, ?, ?, ?, ?);";

        return jdbcTemplate.update(QUERY_CREATE_CERTIFICATE,
                certificate.getId(), certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getCreateDate(),certificate.getCreateDate());
    }

    @Override
    public GiftCertificate get(UUID id) {
        String QUERY_GET_CERTIFICATE = "select * from gift_certificate where id = ?;";

        return jdbcTemplate.queryForObject(
                QUERY_GET_CERTIFICATE,
                new GiftCertificateMapper(), id
        );
    }

    @Override
    public List<GiftCertificate> getAll() {
        String QUERY_GET_ALL = "select * from gift_certificate;";
        return jdbcTemplate.query(QUERY_GET_ALL, new GiftCertificateMapper());
    }

    @Override
    public int delete(UUID id) {
        String QUERY_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";
//        String QUERY_DELETE_CONNECTIONS = "delete from gift_certificate_tag where gift_certificate_id = ?";
//        jdbcTemplate.update(QUERY_DELETE_CONNECTIONS, id);
        return jdbcTemplate.update(QUERY_DELETE_CERTIFICATE, id);
    }

    @Override
    public int update(GiftCertificate update) {
        String QUERY_UPDATE_CERTIFICATE = """
                update gift_certificate 
                set name = ?,
                description = ?,
                price = ?, 
                duration = ?,
                last_update_date = ?
                where id = ?;
                """;

        return jdbcTemplate.update(
                QUERY_UPDATE_CERTIFICATE,
                update.getName(),
                update.getDescription(),
                update.getPrice(),
                update.getDuration(),
                update.getLastUpdateDate(),
                update.getId());
    }

    @Override
    public List<GiftCertificate> getByTagName(String tagName) {
        String QUERY_GET_BY_TAG_NAME = """
                select * from gift_certificate gc where gc.id in 
                (select gt.gift_certificate_id from gift_certificate_tag gt 
                    where gt.tag_id = 
                        (select t.id from tag t where t.name = ?)
                );""";

        return jdbcTemplate.query(QUERY_GET_BY_TAG_NAME, new GiftCertificateMapper(), tagName);
    }

    @Override
    public List<GiftCertificate> search(String keyWord) {
        String QUERY_SEARCH = "select * from search_by_name(?);";
        return jdbcTemplate.query(QUERY_SEARCH, new GiftCertificateMapper(), keyWord);
    }
}
