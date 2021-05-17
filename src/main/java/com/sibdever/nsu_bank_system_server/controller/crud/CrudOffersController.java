package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/offer")
public class CrudOffersController {

    @Autowired
    public CrudOfferService service;

    @GetMapping("/find-all")
    public Iterable<Offer> findAll() {
        return service.findAll();
    }

    @PostMapping("/create")
    public Offer create(@RequestBody Offer offer) {
        return service.createOffer(offer);
    }
}
