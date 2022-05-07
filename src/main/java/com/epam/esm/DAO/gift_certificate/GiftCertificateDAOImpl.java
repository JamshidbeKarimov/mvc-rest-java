package com.epam.esm.DAO.gift_certificate;

import com.epam.esm.exception.BaseException;
import com.epam.esm.model.gift_certificate.GiftCertificate;
import com.epam.esm.model.gift_certificate.GiftCertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private final JdbcTemplate jdbcTemplate;

    public GiftCertificateDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int create(GiftCertificate certificate) {
        String QUERY_CREATE_CERTIFICATE = """
                insert into gift_certificate(id, name, description, price, duration, create_date, last_update_date)
                            values(?, ?, ?, ?, ?, ?, ?);""";

        return jdbcTemplate.update(QUERY_CREATE_CERTIFICATE,
                certificate.getId(), certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getCreateDate(), certificate.getCreateDate());
    }

    @Override
    public GiftCertificate get(UUID id) {
        String QUERY_GET_CERTIFICATE = "select * from gift_certificate where id = ?;";

        try{
        return jdbcTemplate.queryForObject(
                QUERY_GET_CERTIFICATE,
                new GiftCertificateMapper(), id
        );
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            throw new BaseException(10100, "no data  with id" + id);
        }
    }

    @Override
    public List<GiftCertificate> getAll() {
        String QUERY_GET_ALL = "select * from gift_certificate;";
        return jdbcTemplate.query(QUERY_GET_ALL, new GiftCertificateMapper());
    }

    @Override
    public int delete(UUID id) {
        String QUERY_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";
        String QUERY_DELETE_CONNECTIONS = "delete from gift_certificate_tag where gift_certificate_id = ?";
        jdbcTemplate.update(QUERY_DELETE_CONNECTIONS, id);
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
    public List<GiftCertificate> searchAndGetByTagName(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending)
    {
        String QUERY_SEARCH_AND_GET_BY_TAG_NAME = getAllQuery(doNameSort, doDateSort, isDescending);

        return jdbcTemplate.query(QUERY_SEARCH_AND_GET_BY_TAG_NAME, new GiftCertificateMapper(), searchWord, tagName);
    }



    private String getAllQuery(
            boolean doNameSort, boolean doDateSort, boolean isDescending
    ) {
        if (doNameSort) {
            if (doDateSort) {
                if (isDescending)
                    return "select *from get_certificates(?, ?) order by name, create_date desc;";
                return "select *from get_certificates(?, ?) order by name, create_date;";
            }
            if (isDescending)
                return "select *from get_certificates(?, ?) order by name desc;";

            return "select *from get_certificates(?, ?) order by name;";
        } else if (doDateSort) {
            if (isDescending)
                return "select *from get_certificates(?, ?) order by create_date desc;";
            return "select *from get_certificates(?, ?) order by create_date;";
        }
        return "select *from get_certificates(?, ?)";
    }
}
