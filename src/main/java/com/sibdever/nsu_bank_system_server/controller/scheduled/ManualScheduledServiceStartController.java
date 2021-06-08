package com.sibdever.nsu_bank_system_server.controller.scheduled;

import com.sibdever.nsu_bank_system_server.service.DailyScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/daily-service")
public class ManualScheduledServiceStartController {

    @Autowired
    private DailyScheduledService dailyScheduledService;

    @GetMapping("/start")
    public void startScheduledService() {
        dailyScheduledService.manageDailyPayments();
    }

   /* @GetMapping("/start")
    public void startScheduledService(@RequestParam LocalDateTime startTime) {
        dailyScheduledService.setClock(Clock.fixed(startTime.toInstant(ZoneOffset.ofHours(0)), ZoneId.systemDefault()));
        dailyScheduledService.manageDailyPayments();
    }*/
}
