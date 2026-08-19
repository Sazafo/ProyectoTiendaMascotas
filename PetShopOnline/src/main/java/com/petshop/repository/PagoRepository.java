package com.petshop.repository;

import com.petshop.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository
        extends JpaRepository<Pago, Integer> {
}