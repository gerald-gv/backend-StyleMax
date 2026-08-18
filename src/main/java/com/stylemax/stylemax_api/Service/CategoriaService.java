package com.stylemax.stylemax_api.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.CategoriaDTO;
import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarCategoriaRequest;
import com.stylemax.stylemax_api.DTO.admin.CategoriaAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.CategoriaEstadisticasDTO;
import com.stylemax.stylemax_api.DTO.admin.CrearCategoriaRequest;
import com.stylemax.stylemax_api.Entity.Categoria;
import com.stylemax.stylemax_api.Repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Categorias activas, para la seccion "Categorias Clasicas" del home.
    public List<CategoriaDTO> listarActivas() {
        return categoriaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(CategoriaDTO::fromEntity)
                .toList();
    }
    
    // ADMIN
    
 // ADMIN

    private static final int TAMANIO_PAGINA_ADMIN = 10;


    public PaginaDTO<CategoriaAdminDTO> listarParaAdmin(String q,int pagina) {

        String query = (q == null || q.isBlank()) ? null : q.trim();

        Pageable pageable = PageRequest.of(pagina,TAMANIO_PAGINA_ADMIN);

        Page<Categoria> categorias;

        if (query == null) {
            categorias = categoriaRepository.findAllByOrderByNombreAsc(pageable);
        } else {

            categorias = categoriaRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(query,pageable);
        }

        return PaginaDTO.<CategoriaAdminDTO>builder()
                .contenido(
                        categorias.getContent()
                                .stream()
                                .map(CategoriaAdminDTO::fromEntity)
                                .toList()
                )
                .pagina(categorias.getNumber())
                .tamanio(categorias.getSize())
                .totalElementos(categorias.getTotalElements())
                .totalPaginas(categorias.getTotalPages())
                .ultima(categorias.isLast())
                .build();
    }
    
    public CategoriaEstadisticasDTO obtenerEstadisticas() {

        return CategoriaEstadisticasDTO.fromProjection(
                categoriaRepository.obtenerEstadisticasAdmin()
        );
    }


    public CategoriaAdminDTO obtenerPorIdParaAdmin(Long id) {

        Categoria categoria = categoriaRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoría no encontrada"));

        return CategoriaAdminDTO.fromEntity(categoria);
    }


    public CategoriaAdminDTO crear(CrearCategoriaRequest request) {

        String nombre = request.nombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {

            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ya existe una categoría con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(nombre)
                .activo(
                        request.activo() != null
                                ? request.activo()
                                : true
                )
                .build();

        categoria = categoriaRepository.save(categoria);

        return CategoriaAdminDTO.fromEntity(categoria);
    }


    public CategoriaAdminDTO actualizar(Long id,ActualizarCategoriaRequest request) {

        Categoria categoria = categoriaRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"
                ));

        String nombre = request.nombre().trim();

        Optional<Categoria> categoriaExistente =
                categoriaRepository.findByNombreIgnoreCase(nombre);

        if (categoriaExistente.isPresent() && !categoriaExistente.get().getId().equals(id)) {

            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(nombre);
        categoria.setActivo(
                request.activo() != null
                        ? request.activo()
                        : categoria.getActivo()
        );
        
        
        
        categoria = categoriaRepository.save(categoria);

        return CategoriaAdminDTO.fromEntity(categoria);
    }
    
    public CategoriaAdminDTO eliminar(Long id) {

        Categoria categoria = categoriaRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada"
                        )
                );

        categoria.setActivo(false);

        categoria = categoriaRepository.save(categoria);

        return CategoriaAdminDTO.fromEntity(categoria);
    }
}
