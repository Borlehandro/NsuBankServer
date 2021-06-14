package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.data.repo.CrudOffersRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CrudOfferService {

    @Autowired
    private CrudOffersRepo repo;

    public Page<Offer> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Offer createOffer(Offer offer) {
        return repo.save(offer);
    }

    public void edit(Offer offer) {
        repo.save(offer);
    }

    public Offer find(int id) {
        var opt = repo.findById(id);
        if(opt.isPresent()) {
            return opt.get();
        } else throw new WrongCredentialsException("Offer with id=" + id + " does not exist");
    }
}
