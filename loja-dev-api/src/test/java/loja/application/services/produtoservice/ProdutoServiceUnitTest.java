package loja.application.services.produtoservice;

import loja.application.ports.ProdutoRepositoryPort;
import loja.application.services.ProdutoService;
import loja.domain.categoria.Categoria;
import loja.domain.produto.Produto;
import loja.infrastructure.exceptions.ProdutoNotFoundException;
import loja.infrastructure.inbound.record.ProdutoRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceUnitTest {

  @Mock
  private ProdutoRepositoryPort produtoRepositoryPort;

  @InjectMocks
  private ProdutoService produtoService;

  private ProdutoRecord produtoRecord;

  @BeforeEach
  void setUp() {
    produtoRecord = new ProdutoRecord(
      new Categoria(),
      1L,
      "Smartphone",
      new BigDecimal("1000.00"),
      "Smartphone Description");
  }

  @Test
  void shouldCreateProduto() {
    ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);

    Assertions.assertDoesNotThrow(() -> produtoService.create(produtoRecord));
    verify(produtoRepositoryPort, Mockito.times(1)).save(Mockito.any(Produto.class));
    verify(produtoRepositoryPort).save(produtoCaptor.capture());
    Produto produto = produtoCaptor.getValue();

    Assertions.assertEquals(produtoRecord.descricao(), produto.getDescricao());
    Assertions.assertEquals(produtoRecord.preco(), produto.getPreco());
    Assertions.assertEquals(produtoRecord.nome(), produto.getNome());
    Assertions.assertEquals(produtoRecord.id(), produto.getId());
  }

  @Test
  void shouldUpdateProdutoWhenExists() {
    ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
    Produto produtoAtual = new Produto();
    produtoAtual.setId(1L);
    produtoAtual.setPreco(new BigDecimal("1000.00"));
    produtoAtual.setNome("Smartphone 1");
    produtoAtual.setDescricao("Smartphone Description");
    produtoAtual.setCategoria(new Categoria());

    when(produtoRepositoryPort.findAllById(Mockito.anyLong())).thenReturn(produtoAtual);
    Assertions.assertDoesNotThrow(() -> produtoService.update(produtoRecord));

    verify(produtoRepositoryPort, Mockito.times(1)).save(Mockito.any(Produto.class));
    verify(produtoRepositoryPort).save(produtoCaptor.capture());
    Produto produto = produtoCaptor.getValue();

    Assertions.assertEquals(produtoRecord.descricao(), produto.getDescricao());
    Assertions.assertEquals(produtoRecord.preco(), produto.getPreco());
    Assertions.assertEquals(produtoRecord.nome(), produto.getNome());
    Assertions.assertEquals(produtoRecord.id(), produto.getId());
  }

  @Test
  void shouldNotUpdateProdutoWhenNotExists() {
    when(produtoRepositoryPort.findAllById(Mockito.anyLong())).thenReturn(null);

    ProdutoNotFoundException ex = assertThrows(ProdutoNotFoundException.class, () -> produtoService.update(produtoRecord));
    assertTrue(ex.getMessage().contains("Produto não encontrado"));
  }

  @Test
  void shouldDeleteProdutoWhenExistente() {
    ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
    Long id = 1L;
    when(produtoRepositoryPort.existsById(id)).thenReturn(true);

    assertDoesNotThrow(() -> produtoService.delete(id));
    verify(produtoRepositoryPort).deleteById(longCaptor.capture());
    Long capturedId = longCaptor.getValue();
    assertEquals(id, capturedId);
  }

  @Test
  void shouldNotDeleteProdutoWhenNotExist() {
    Long id = 1L;
    when(produtoRepositoryPort.existsById(id)).thenReturn(false);

    ProdutoNotFoundException ex = assertThrows(ProdutoNotFoundException.class, () -> produtoService.delete(id));
    verify(produtoRepositoryPort, Mockito.times(0)).deleteById(Mockito.anyLong());
    assertTrue(ex.getMessage().contains("Produto não encontrado"));
  }

  @Test
  void shouldFindProduto() {
    Long id = 1L;
    Produto produto = mock(Produto.class);
    when(produtoRepositoryPort.findAllById(id)).thenReturn(produto);

    assertDoesNotThrow(() -> produtoService.find(id));
    verify(produtoRepositoryPort, Mockito.times(1)).findAllById(id);
  }

  @Test
  void shouldReturnNullWhenProdutoNotFound() {
    Long id = 99L;
    when(produtoRepositoryPort.findAllById(id)).thenReturn(null);
    Produto result = produtoService.find(id);
    assertNull(result);
  }

  @Test
  void shouldFindAllProdutos() {
    List<Long> ids = Arrays.asList(1L, 2L);
    Produto p1 = mock(Produto.class);
    Produto p2 = mock(Produto.class);
    List<Produto> produtos = Arrays.asList(p1, p2);
    when(produtoRepositoryPort.findAllByIdIn(ids)).thenReturn(produtos);

    List<Produto> result = produtoService.findAllProdutos(ids);
    assertEquals(produtos, result);
  }

  @Test
  void shouldNotFindAllProdutosWhenNotFound() {
    List<Long> ids = Arrays.asList(1L, 2L);
    when(produtoRepositoryPort.findAllByIdIn(ids)).thenReturn(emptyList());

    List<Produto> result = produtoService.findAllProdutos(ids);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldDeleteAllProdutos() {
    doNothing().when(produtoRepositoryPort).deleteAll();
    assertDoesNotThrow(() -> produtoService.deleteAll());
    verify(produtoRepositoryPort).deleteAll();
  }
}