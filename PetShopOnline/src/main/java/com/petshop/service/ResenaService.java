package com.petshop.service;

import com.petshop.domain.Producto;
import com.petshop.domain.Resena;
import com.petshop.domain.Usuario;
import com.petshop.repository.ResenaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public List<Resena> listarPorProducto(Integer idProducto) {

        return resenaRepository
                .findByProductoIdProductoOrderByFechaDesc(idProducto);
    }

    public boolean guardar(
            Usuario usuario,
            Producto producto,
            Integer calificacion,
            String comentario) {

        if (usuario == null || producto == null) {
            return false;
        }

        if (calificacion == null
                || calificacion < 1
                || calificacion > 5) {
            return false;
        }

        boolean yaExiste =
                resenaRepository
                        .existsByUsuarioIdUsuarioAndProductoIdProducto(
                                usuario.getIdUsuario(),
                                producto.getIdProducto()
                        );

        if (yaExiste) {
            return false;
        }

        Resena resena = new Resena();

        resena.setUsuario(usuario);
        resena.setProducto(producto);
        resena.setCalificacion(calificacion);
        resena.setComentario(comentario);
        resena.setFecha(LocalDateTime.now());

        resenaRepository.save(resena);

        return true;
    }
}