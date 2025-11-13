package loja.infrastructure.inbound.rest;

import jakarta.validation.Valid;
import loja.application.usecases.CategoriaUC;
import loja.domain.categoria.Categoria;
import loja.infrastructure.exceptions.CategoriaNotFoundException;
import loja.infrastructure.inbound.record.CategoriaRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

  private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

  private final CategoriaUC categoriaUC;

  public CategoriaController(CategoriaUC categoriaUC) {
    this.categoriaUC = categoriaUC;
  }

  @PostMapping()
  public ResponseEntity<Categoria> create(@Valid @RequestBody CategoriaRecord categoriaRecord) {
    logger.info("Criando categoria: {}", categoriaRecord);
    Categoria categoria = categoriaUC.create(categoriaRecord);
    return Objects.nonNull(categoria) ? ResponseEntity.status(HttpStatus.CREATED).body(categoria) :
      ResponseEntity.badRequest().build();
  }

  @PutMapping()
  public ResponseEntity<Categoria> update(@Valid @RequestBody CategoriaRecord categoriaRecord) {
    Categoria categoria = categoriaUC.update(categoriaRecord);
    logger.info("Atualizando categoria: {}", categoriaRecord);
    return Objects.nonNull(categoria) ? ResponseEntity.ok().body(categoria) :
      ResponseEntity.badRequest().build();
  }

  @GetMapping("/{idCategoria}")
  public ResponseEntity<Categoria> find(@Valid @PathVariable Long idCategoria) {
    logger.info("Buscando categoria: {}", idCategoria);
    Categoria categoria = categoriaUC.find(idCategoria);
    if (categoria != null) {
      return ResponseEntity.ok().body(categoria);
    } else
      throw new CategoriaNotFoundException("Categoria não encontrada para o id: " + idCategoria);
  }

  @GetMapping("/all")
  public ResponseEntity<List<Categoria>> findAll(@Valid @RequestParam List<Long> idsCategorias) {
    logger.info("Buscando todas as categoria: {}", idsCategorias);
    List<Categoria> categorias = categoriaUC.findAllCategorias(idsCategorias);
    if (!categorias.isEmpty()) {
      return ResponseEntity.ok().body(categorias);
    } else
      return ResponseEntity.ok(Collections.emptyList());
  }

  @DeleteMapping("/{idCategoria}")
  public ResponseEntity<Void> delete(@Valid @PathVariable Long idCategoria) {
    if (categoriaUC.existsCategoriaById(idCategoria)) {
      logger.info("Excluindo categoria: {}", idCategoria);
      categoriaUC.delete(idCategoria);
      return ResponseEntity.noContent().build();
    } else {
      throw new CategoriaNotFoundException("Categoria não encontrada para o id: " + idCategoria);
    }
  }

  @DeleteMapping("/deleteAll")
  public ResponseEntity<Void> deleteAll() {
    logger.info("Excluindo todas as categorias");
    categoriaUC.deleteAll();
    return ResponseEntity.noContent().build();
  }
}
