package loja.application.services.categoriaservice;

import loja.application.ports.CategoriaRepositoryPort;
import loja.application.ports.ProdutoRepositoryPort;
import loja.application.services.CategoriaService;
import loja.domain.categoria.Categoria;
import loja.infrastructure.exceptions.CategoriaNotFoundException;
import loja.infrastructure.inbound.record.CategoriaRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceUnitTest {

  @Mock
  private CategoriaRepositoryPort categoriaRepositoryPort;

  @Mock
  private ProdutoRepositoryPort produtoRepositoryPort;

  private CategoriaService categoriaService;

  private CategoriaRecord categoriaRecord;

  @BeforeEach
  void setUp() {
    categoriaService = new CategoriaService(categoriaRepositoryPort, produtoRepositoryPort);
    categoriaRecord = new CategoriaRecord(null, "Categoria Teste");
  }

  @Test
  void shouldCreateCategoria() {
    ArgumentCaptor<Categoria> categoriaCaptor = ArgumentCaptor.forClass(Categoria.class);

    categoriaService.create(categoriaRecord);

    verify(categoriaRepositoryPort).save(categoriaCaptor.capture());
    Categoria result = categoriaCaptor.getValue();

    assertEquals("Categoria Teste", result.getNomeCategoria());
  }

  @Test
  void shouldUpdateCategoria() {
    ArgumentCaptor<Categoria> novaCategoria = ArgumentCaptor.forClass(Categoria.class);

    Categoria categoriaAtual = new Categoria();
    categoriaAtual.setId(1L);
    categoriaAtual.setNomeCategoria("Categoria Atual");

    when(categoriaRepositoryPort.findAllById(1L)).thenReturn(categoriaAtual);

    CategoriaRecord novaCategoriaRecord = new CategoriaRecord(
      categoriaAtual.getId(), "Categoria Teste");

    categoriaService.update(novaCategoriaRecord);

    verify(categoriaRepositoryPort).save(novaCategoria.capture());
    Categoria result = novaCategoria.getValue();
    assertEquals("Categoria Teste", result.getNomeCategoria());
  }

  @Test
  void shouldNotUpdateWhenCategoriaNotFound() {
    CategoriaNotFoundException ex = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.update(categoriaRecord));
    assertTrue(ex.getMessage().contains("Categoria não encontrada"));
  }

  @Test
  void shouldDelete() {
    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
    Long id = 1L;
    when(categoriaRepositoryPort.existsById(id)).thenReturn(true);

    categoriaService.delete(id);

    verify(produtoRepositoryPort).deleteByCategoriaId(captor.capture());
    assertEquals(id, captor.getValue());

    verify(categoriaRepositoryPort).deleteById(captor.capture());
    assertEquals(id, captor.getValue());
  }

  @Test
  void shouldNotDeleteWhenCategoriaNotFound() {
    Long id = 2L;
    when(categoriaRepositoryPort.existsById(id)).thenReturn(false);

    CategoriaNotFoundException ex = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.delete(id));
    assertTrue(ex.getMessage().contains("Categoria não encontrada"));
  }

  @Test
  void shouldFindCategoria() {
    Long id = 1L;
    Categoria categoria = new Categoria();
    categoria.setId(id);
    categoria.setNomeCategoria("Categoria Teste");

    when(categoriaRepositoryPort.findAllById(id)).thenReturn(categoria);

    Categoria result = categoriaService.find(id);
    assertEquals(categoria, result);

    verify(categoriaRepositoryPort).findAllById(id);
  }

  @Test
  void shouldCheckIfExistsCategoriaById() {
    Long id = 1L;
    when(categoriaRepositoryPort.existsById(id)).thenReturn(true);
    assertTrue(categoriaService.existsCategoriaById(id));

    when(categoriaRepositoryPort.existsById(id)).thenReturn(false);
    assertFalse(categoriaService.existsCategoriaById(id));
  }

  @Test
  void shouldFindAllCategorias() {
    Categoria categoria1 = new Categoria();
    categoria1.setId(1L);
    categoria1.setNomeCategoria("Categoria Teste1");

    Categoria categoria2 = new Categoria();
    categoria2.setId(2L);
    categoria2.setNomeCategoria("Categoria Teste2");

    List<Categoria> categorias = new ArrayList<>();
    categorias.add(categoria1);
    categorias.add(categoria2);

    when(categoriaRepositoryPort.findAllByIdIn(List.of(categoria1.getId(), categoria2.getId()))).thenReturn(categorias);

    List<Categoria> result = categoriaService.findAllCategorias(List.of(categoria1.getId(), categoria2.getId()));
    assertEquals(categorias, result);

    verify(categoriaRepositoryPort).findAllByIdIn(List.of(categoria1.getId(), categoria2.getId()));
  }

  @Test
  void shouldDeleteAll() {
    categoriaService.deleteAll();

    verify(produtoRepositoryPort).deleteAll();
    verify(categoriaRepositoryPort).deleteAll();
  }
}