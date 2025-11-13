package loja.infrastructure.inbound.rest;

import jakarta.validation.Valid;
import loja.application.usecases.ProdutoUC;
import loja.domain.produto.Produto;
import loja.infrastructure.exceptions.ProdutoNotFoundException;
import loja.infrastructure.inbound.record.ProdutoRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("categorias/{idCategoria}/produtos")
public class ProdutoController {

  private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
  private final ProdutoUC produtoUC;

  public ProdutoController(ProdutoUC produtoUC) {
    this.produtoUC = produtoUC;
  }

  @PostMapping()
  public ResponseEntity<Produto> create(@Valid @RequestBody ProdutoRecord produtoRecord) {
    logger.info("Criando produto: {}", produtoRecord);
    Produto produto = produtoUC.create(produtoRecord);
    return Objects.nonNull(produto) ? ResponseEntity.status(HttpStatus.CREATED).body(produto)
      : ResponseEntity.badRequest().build();
  }

  @PutMapping()
  public ResponseEntity<Produto> update(@Valid @RequestBody ProdutoRecord produtoRecord) {
    Produto produto = produtoUC.update(produtoRecord);
    logger.info("Atualizando produto: {}", produtoRecord);
    return Objects.nonNull(produto) ? ResponseEntity.ok().body(produto)
      : ResponseEntity.badRequest().build();
  }

  @GetMapping("/{idProduto}")
  public ResponseEntity<Produto> find(@PathVariable Long idProduto) {
    logger.info("Buscando produto: {}", idProduto);
    Produto produto = produtoUC.find(idProduto);
    if (produto != null) {
      return ResponseEntity.ok().body(produto);
    } else {
      throw new ProdutoNotFoundException("Produto não encontrado para o id: " + idProduto);
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<Produto>> findAll(@RequestParam List<Long> idsProdutos) {
    logger.info("Buscando todos os produtos: {}", idsProdutos);
    List<Produto> produtos = produtoUC.findAllProdutos(idsProdutos);
    return ResponseEntity.ok().body(produtos.isEmpty() ? Collections.emptyList() : produtos);
  }

  @DeleteMapping("/{idProduto}")
  public ResponseEntity<Void> delete(@PathVariable Long idProduto) {
      logger.info("Excluindo produto: {}", idProduto);
      produtoUC.delete(idProduto);
      return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/deleteAll")
  public ResponseEntity<Void> delete() {
      produtoUC.deleteAll();
      return ResponseEntity.noContent().build();
  }
}
