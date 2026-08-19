package com.stylemax.stylemax_api.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.MarcaDTO;
import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarMarcaRequest;
import com.stylemax.stylemax_api.DTO.admin.CrearMarcaRequest;
import com.stylemax.stylemax_api.DTO.admin.MarcaAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.MarcaEstadisticasDTO;
import com.stylemax.stylemax_api.Entity.Marca;
import com.stylemax.stylemax_api.Repository.MarcaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarcaService {
	
	private static final int TAMANIO_PAGINA_ADMIN = 10;
    private final MarcaRepository marcaRepository;

    // Marcas activas, para filtros del catalogo
    public List<MarcaDTO> listarActivas() {
        return marcaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(MarcaDTO::fromEntity)
                .toList();
    }
    // ADMIN
    
    public PaginaDTO<MarcaAdminDTO> listarParaAdmin(String q,int pagina) {

        String query = (q == null || q.isBlank()) ? null : q.trim();
        Pageable pageable = PageRequest.of(pagina,TAMANIO_PAGINA_ADMIN);
        Page<Marca> marcas;


        if (query == null) {
            marcas = marcaRepository.findAllByOrderByNombreAsc(pageable);
        } else {
            marcas =marcaRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(query, pageable);
        }


        return PaginaDTO
                .<MarcaAdminDTO>builder()
                .contenido(
                        marcas
                        	.getContent()
                        	.stream()
                        	.map(MarcaAdminDTO::fromEntity)
                        	.toList()
                )
                .pagina(marcas.getNumber())
                .tamanio(marcas.getSize())
                .totalElementos( marcas.getTotalElements())
                .totalPaginas(marcas.getTotalPages())
                .ultima(marcas.isLast())

                .build();
    }


    public MarcaEstadisticasDTO obtenerEstadisticas() {
        return MarcaEstadisticasDTO.fromProjection(marcaRepository.obtenerEstadisticasAdmin());
    }



    public MarcaAdminDTO crear(CrearMarcaRequest request) {

        String nombre = request.nombre().trim();


        if (marcaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ya existe una marca con ese nombre");
        }


        Marca marca =Marca.builder()
        				.nombre(nombre)
                        .activo( request.activo() != null ? request.activo() : true)
                        .build();

        marca = marcaRepository.save(marca);

        return MarcaAdminDTO.fromEntity(marca);
    }


    // ACTUALIZAR

    public MarcaAdminDTO actualizar(Long id,ActualizarMarcaRequest request) {

        Marca marca = marcaRepository.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,"Marca no encontrada")
                        );


        String nombre = request.nombre().trim();


        Optional<Marca> marcaExistente = marcaRepository.findByNombreIgnoreCase(nombre);

        if ( marcaExistente.isPresent() && !marcaExistente.get().getId().equals(id) ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una marca con ese nombre");
        }


        marca.setNombre(nombre);


        marca.setActivo(request.activo() != null? request.activo(): false);


        marca = marcaRepository.save(marca);

        return MarcaAdminDTO.fromEntity(marca);
    }

    public MarcaAdminDTO eliminar( Long id) {

        Marca marca = marcaRepository.findById(id)
        						.orElseThrow(() ->
	                                new ResponseStatusException( HttpStatus.NOT_FOUND,"Marca no encontrada"));

        marca.setActivo(false);
        marca = marcaRepository.save(marca);

        return MarcaAdminDTO.fromEntity(marca);
    }

}
