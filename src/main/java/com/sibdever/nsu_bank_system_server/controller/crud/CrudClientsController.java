package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Client;
import com.sibdever.nsu_bank_system_server.service.CrudClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/client")
public class CrudClientsController {

    @Autowired
    private CrudClientService service;

    @PostMapping("/create")
    public @ResponseBody
    Client createClient(@RequestBody Client client) {
        return service.saveClient(client);
    }

    @PostMapping("/delete")
    public @ResponseBody
    void deleteClientById(@RequestParam int id) {
        service.deleteClientById(id);
    }

    @GetMapping("/find")
    public @ResponseBody
    Client findById(@RequestParam int id) {
        return service.findById(id);
    }

    // Todo Pageable!
    @GetMapping("/find-all")
    public @ResponseBody
    Page<Client> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @PostMapping("/edit")
    public
    void editUser(@RequestBody Client client) {
        service.saveClient(client);
    }

}
