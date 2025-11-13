package loja.application.services.produtoservice;

import loja.application.ports.CategoriaRepositoryPort;
import loja.application.ports.ProdutoRepositoryPort;
import loja.application.services.ProdutoService;
import loja.domain.categoria.Categoria;
import loja.domain.produto.Produto;
import loja.infrastructure.inbound.record.ProdutoRecord;
import loja.infrastructure.outbound.CategoriaRepository;
import loja.infrastructure.outbound.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("test")
class ProdutoServiceIntegrationTest {

  @Autowired
  private CategoriaRepositoryPort categoriaRepositoryPort;

  @Autowired
  private ProdutoRepositoryPort produtoRepositoryPort;

  @Autowired
  private CategoriaRepository categoriaRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private ProdutoService produtoService;

  private ProdutoRecord produtoRecord;

  @BeforeEach
  void setUp() {
    produtoService = new ProdutoService(produtoRepositoryPort);

    Categoria categoria = new Categoria();
    categoria.setNomeCategoria("Eletrônicos");
    categoriaRepositoryPort.save(categoria);

    produtoRecord = new ProdutoRecord(
      categoria, null, "Smartphone", BigDecimal.valueOf(1000), "Smartphone 32Gb");
  }

  @AfterEach
  void tearDown() {
    produtoRepository.deleteAll();
    categoriaRepository.deleteAll();
  }

  @Test
  void shouldCreateProduto() {
    Assertions.assertDoesNotThrow(() -> produtoService.create(produtoRecord));
  }

  @Test
  void shouldUpdateProduto() {
    Produto produto = produtoRepositoryPort.save(produtoRecord.toEntity());

    ProdutoRecord produtoRecordUpdate = new ProdutoRecord(
      produto.getCategoria(),
      produto.getId(),
      "Smartphone Atualizado",
      produto.getPreco(),
      produto.getDescricao()
    );

    Assertions.assertDoesNotThrow(() -> produtoService.update(produtoRecordUpdate));
  }

  @Test
  void shouldDeleteProduto() {
    Produto produto = produtoRepositoryPort.save(produtoRecord.toEntity());

    Assertions.assertDoesNotThrow(() -> produtoService.delete(produto.getId()));
  }

  @Test
  void shouldFindProduto() {
    Produto produto = produtoRepositoryPort.save(produtoRecord.toEntity());

    Assertions.assertDoesNotThrow(() -> produtoService.find(produto.getId()));
  }

  @Test
  void shouldFindAllProdutos() {
    Produto produto = produtoRepositoryPort.save(produtoRecord.toEntity());

    Assertions.assertDoesNotThrow(() -> produtoService.findAllProdutos(Collections.singletonList(produto.getId())));
  }

  @Test
  void shouldDeleteAll() {
    produtoRepositoryPort.save(produtoRecord.toEntity());

    Assertions.assertDoesNotThrow(() -> produtoService.deleteAll());
  }
}