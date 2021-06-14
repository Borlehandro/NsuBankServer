package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/offer")
public class CrudOffersController {

    @Autowired
    public CrudOfferService service;

    @GetMapping("/find-all")
    public Page<Offer> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @PostMapping("/create")
    public Offer create(@RequestBody Offer offer) {
        return service.createOffer(offer);
    }

    @PostMapping("/edit")
    public void edit(@RequestBody Offer offer) {
        service.edit(offer);
    }

    @GetMapping("/find")
    public Offer find(@RequestParam int id) {
        return service.find(id);
    }
}
