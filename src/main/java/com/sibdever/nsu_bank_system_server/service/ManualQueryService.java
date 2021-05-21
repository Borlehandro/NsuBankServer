package com.sibdever.nsu_bank_system_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sibdever.nsu_bank_system_server.data.repo.ManualQueryRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManualQueryService {
    @Autowired
    private ManualQueryRepoImpl manualRequestRepo;

    public List<ObjectNode> executeManualQuery(String queryText) {
        return tupleToJson(manualRequestRepo.executeQuery(queryText));
    }

    // Todo fix for null values
    private List<ObjectNode> tupleToJson(List<Tuple> results) {

        List<ObjectNode> json = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        for (Tuple t : results) {
            List<TupleElement<?>> cols = t.getElements();

            ObjectNode one = mapper.createObjectNode();

            for (TupleElement col : cols) {
                one.put(col.getAlias(), t.get(col.getAlias()).toString());
            }

            json.add(one);
        }

        return json;
    }
}
