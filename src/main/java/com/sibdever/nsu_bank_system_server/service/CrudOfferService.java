package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Offer;
import com.sibdever.nsu_bank_system_server.data.repo.CrudOffersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrudOfferService {

    @Autowired
    private CrudOffersRepo repo;

    public Iterable<Offer> findAll() {
        return repo.findAll();
    }

    public Offer createOffer(Offer offer) {
        return repo.save(offer);
    }
}
