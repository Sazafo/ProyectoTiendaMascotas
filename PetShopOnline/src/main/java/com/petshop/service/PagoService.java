package com.petshop.service;

import com.petshop.domain.Pago;
import com.petshop.domain.Pedido;
import com.petshop.repository.PagoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public Pago registrarPago(
            Pedido pedido,
            String metodo) {

        Pago pago = new Pago();

        pago.setPedido(pedido);
        pago.setMetodo(metodo);
        pago.setMonto(pedido.getTotal());
        pago.setFecha(LocalDateTime.now());
        pago.setEstado("APROBADO");

        pago.setReferencia(
                "PET-" + pedido.getIdPedido()
        );

        return pagoRepository.save(pago);
    }
}