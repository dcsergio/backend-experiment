package it.sdc.restserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sdc.restserver.dto.VoucherDto;
import it.sdc.restserver.entity.Voucher;
import it.sdc.restserver.service.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vouchers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow requests from Angular frontend
public class VoucherController {

    private final VoucherRepository voucherRepository;

    private final ObjectMapper objectMapper;

    @PostMapping
    public VoucherDto saveVoucher(@RequestBody VoucherDto voucher) {
        // Ensure ID is null so Mongo generates it (if not provided)
        // or just let Mongo handle it.
        // We might want to set createdAt if it's not passed, but the frontend sends it.
        return objectMapper.convertValue(voucherRepository.save(objectMapper.convertValue(voucher, Voucher.class)), VoucherDto.class);
    }

    @GetMapping
    public List<VoucherDto> getAllVouchers() {
        return voucherRepository.findAllByOrderByCreatedAtDesc().stream().map(e -> objectMapper.convertValue(e, VoucherDto.class)).toList();
    }
}
