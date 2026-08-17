package com.stylemax.stylemax_api.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.ProductoCardDTO;
import com.stylemax.stylemax_api.DTO.ProductoDetalleDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarProductoRequest;
import com.stylemax.stylemax_api.DTO.admin.CrearProductoRequest;
import com.stylemax.stylemax_api.DTO.admin.ProductoAdminDTO;
import com.stylemax.stylemax_api.Entity.Categoria;
import com.stylemax.stylemax_api.Entity.Marca;
import com.stylemax.stylemax_api.Entity.Producto;
import com.stylemax.stylemax_api.Repository.CategoriaRepository;
import com.stylemax.stylemax_api.Repository.MarcaRepository;
import com.stylemax.stylemax_api.Repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {
	
	private static final int TAMANIO_PAGINA = 16;
	
    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    //Catalogo principal
    public PaginaDTO<ProductoCardDTO> listarCatalogo(Long categoriaId, Long marcaId, String q, int pagina) {
        String query = (q == null || q.isBlank()) ? null : q.trim();
        
        Pageable pageable = PageRequest.of(pagina, TAMANIO_PAGINA);
        
        Page<Producto> productos = productoRepository.buscarCatalogo(categoriaId, marcaId, query, pageable);
        
        return PaginaDTO.<ProductoCardDTO>builder()
                .contenido(
                        productos.getContent()
                                .stream()
                                .map(ProductoCardDTO::fromEntity)
                                .toList()
                )
                .pagina(productos.getNumber())
                .tamanio(productos.getSize())
                .totalElementos(productos.getTotalElements())
                .totalPaginas(productos.getTotalPages())
                .ultima(productos.isLast())
                .build();
    }
    
    // Detalle de producto para la ruta dinamica
    public ProductoDetalleDTO obtenerPorSlug(String slug) {
        Producto producto = productoRepository.findBySlug(slug)
                .filter(Producto::getActivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + slug));
        return ProductoDetalleDTO.fromEntity(producto);
    }
    
    // ADMIN
    
    public List<ProductoAdminDTO> listarTodosParaAdmin() {

        return productoRepository
                .findAllParaAdmin()
                .stream()
                .map(this::toAdminDTO)
                .toList();
    }


    public ProductoAdminDTO obtenerPorIdParaAdmin(Long id) {

        Producto producto = productoRepository
                .findByIdParaAdmin(id)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Producto no encontrado"));

        return toAdminDTO(producto);
    }


    public ProductoAdminDTO crear(CrearProductoRequest request) {

        Marca marca = marcaRepository
                .findById(request.marcaId())
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Marca no encontrada"));

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        String slug = generarSlugUnico(request.nombre());

        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .slug(slug)
                .descripcion(request.descripcion())
                .precio(request.precio())
                .stock(request.stock())
                .color(request.color())
                .fit(request.fit())
                .imagen(request.imagen())
                .destacado( request.destacado() != null ? request.destacado() : false)
                .activo(true)
                .marca(marca)
                .categoria(categoria)
                .build();

        producto = productoRepository.save(producto);
        return toAdminDTO(producto);
    }


    public ProductoAdminDTO actualizar(Long id, ActualizarProductoRequest request) {

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Producto no encontrado"));

        Marca marca = marcaRepository
                .findById(request.marcaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada"));

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoría no encontrada"));

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setColor(request.color());
        producto.setFit(request.fit());
        producto.setImagen(request.imagen());

        producto.setDestacado( request.destacado() != null? request.destacado(): false);

        producto.setMarca(marca);
        producto.setCategoria(categoria);

        producto = productoRepository.save(producto);

        return toAdminDTO(producto);
    }


    public ProductoAdminDTO eliminar(Long id) {

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producto no encontrado"));

        producto.setActivo(false);
        producto = productoRepository.save(producto);

        return toAdminDTO(producto);
    }


    // MAPPER

    private ProductoAdminDTO toAdminDTO(Producto producto) {

        return ProductoAdminDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .slug(producto.getSlug())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .color(producto.getColor())
                .fit(producto.getFit())
                .imagen(producto.getImagen())
                .destacado(producto.getDestacado())
                .activo(producto.getActivo())

                .marcaId(producto.getMarca().getId())
                .marca(producto.getMarca().getNombre())

                .categoriaId(producto.getCategoria().getId())
                .categoria(producto.getCategoria().getNombre())

                .build();
    }
    
    // METODOS AUXILIARES

    private String generarSlug(String nombre) {

        String slug = Normalizer
                .normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se pudo generar un slug válido para el producto");
        }

        return slug;
    }
    
    private String generarSlugUnico(String nombre) {

        String slugBase = generarSlug(nombre);
        String slug = slugBase;

        int contador = 2;

        while (productoRepository.existsBySlug(slug)) {
            slug = slugBase + "-" + contador;
            contador++;
        }

        return slug;
    }
}
