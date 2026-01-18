package it.sdc.restserver.service;

import it.sdc.restserver.entity.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends MongoRepository<Voucher, String> {
    List<Voucher> findAllByOrderByCreatedAtDesc();
}
