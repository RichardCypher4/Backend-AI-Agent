
package com.verifi.repository;

import com.verifi.model.Dispute;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DisputeRepository extends MongoRepository<Dispute, String> {
    List<Dispute> findByStatusIn(List<String> statuses);
    List<Dispute> findByTransactionId(String transactionId);

    List<Dispute> findByStatus(String status);
}
