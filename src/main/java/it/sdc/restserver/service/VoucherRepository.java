package it.sdc.restserver.service;

import it.sdc.restserver.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    List<Voucher> findAllByOrderByCreatedAtDesc();
}
