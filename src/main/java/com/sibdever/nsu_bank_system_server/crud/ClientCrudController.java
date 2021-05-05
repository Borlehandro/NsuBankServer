package com.sibdever.nsu_bank_system_server.crud;

import com.sibdever.nsu_bank_system_server.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crud/client")
public class ClientCrudController {

    @Autowired
    private ClientsCrudService service;

    @PostMapping("/create")
    public @ResponseBody
    Client createClient(@RequestBody Client client) {
        return service.createClient(client);
    }

    @PostMapping("/delete")
    public @ResponseBody
    void deleteClientById(@RequestParam int id) {
        service.deleteClientById(id);
    }

    @GetMapping("/find")
    public @ResponseBody
    List<Client> findAllByFullName(@RequestParam String fullName) {
        return service.findAllByFullName(fullName);
    }

    @GetMapping("/find-all")
    public @ResponseBody
    Iterable<Client> findAll() {
        return service.findAll();
    }

}
