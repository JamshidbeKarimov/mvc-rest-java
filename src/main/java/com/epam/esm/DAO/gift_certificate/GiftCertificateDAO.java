package com.epam.esm.DAO.gift_certificate;

import com.epam.esm.DAO.BaseDAO;
import com.epam.esm.model.gift_certificate.GiftCertificate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateDAO extends BaseDAO<GiftCertificate> {

    GiftCertificate update(GiftCertificate updateCertificate);

    List<GiftCertificate> searchAndGetByTagName(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending);







}
