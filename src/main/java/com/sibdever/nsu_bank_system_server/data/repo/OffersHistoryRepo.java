package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.OfferHistoryRecord;
import com.sibdever.nsu_bank_system_server.data.model.OffersHistoryId;
import org.springframework.data.repository.CrudRepository;

public interface OffersHistoryRepo extends CrudRepository<OfferHistoryRecord, OffersHistoryId> {

}
