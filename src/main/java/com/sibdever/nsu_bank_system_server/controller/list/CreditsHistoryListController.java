package com.sibdever.nsu_bank_system_server.controller.list;

import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditsHistorySpecification;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_table.CreditsTableSpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditHistory;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.service.filtering.CreditTableFilteringService;
import com.sibdever.nsu_bank_system_server.service.filtering.CreditsHistoryFilteringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/list/credit-history")
public class CreditsHistoryListController {
    @Autowired
    private CreditsHistoryFilteringService filteringService;

    @GetMapping
    public Page<CreditHistory> getCreditTableListBySpecification(Pageable pageable, @RequestParam String filter) {
        var builder = CreditsHistorySpecification.builder();

        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|!:|<:|>:)(\\w+?),");

        Matcher matcher = pattern.matcher(filter + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        CreditsHistorySpecification spec = builder.build().get();

        return filteringService.getCreditsHistoryBySpecification(spec, pageable);
    }
}
