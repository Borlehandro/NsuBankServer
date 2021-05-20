package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.entities.DayStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayStatisticRepo extends JpaRepository<DayStatistic, Integer> {

}