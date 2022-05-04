package com.epam.esm.DAO.gift_certificate;

import com.epam.esm.DAO.BaseDAO;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.model.gift_certificate.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.epam.esm.DTO.GiftCertificateDto;

import javax.sql.DataSource;
import java.util.List;

@Repository
public interface GiftCertificateDAO extends BaseDAO<GiftCertificate> {

    int update(GiftCertificate updateCertificate);

    List<GiftCertificate> getByTagName(String tagName);

    List<GiftCertificate> search(String keyWord);





}
