package loja.application.services.categoriaservice;

import loja.application.ports.CategoriaRepositoryPort;
import loja.application.ports.ProdutoRepositoryPort;
import loja.application.services.CategoriaService;
import loja.domain.categoria.Categoria;
import loja.infrastructure.inbound.record.CategoriaRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoriaServiceIntegrationTest {

  @Autowired
  private CategoriaRepositoryPort categoriaRepositoryPort;

  @Autowired
  private ProdutoRepositoryPort produtoRepositoryPort;

  @Autowired
  private CategoriaService categoriaService;

  private CategoriaRecord categoriaRecord;

  @BeforeEach
  void setUp() {
    categoriaService = new CategoriaService(categoriaRepositoryPort, produtoRepositoryPort);
    categoriaRecord = new CategoriaRecord(null, "Categoria Teste");
  }

  @AfterEach
  void tearDown() {
    categoriaService.deleteAll();
  }

  @Test
  void shouldCreateCategoria() {
    Categoria categoria = categoriaService.create(categoriaRecord);

    assertAll(
      () -> assertNotNull(categoria.getId()),
      () -> assertEquals("Categoria Teste", categoria.getNomeCategoria())
    );
  }

  @Test
  void shouldUpdateCategoria() {
    Categoria categoria = categoriaService.create(categoriaRecord);

    Categoria categoriaAtualizada = categoriaService.update(
      new CategoriaRecord(categoria.getId(), "Categoria Atualizada"));

    assertAll(
      () -> assertEquals("Categoria Atualizada", categoriaAtualizada.getNomeCategoria()),
      () -> assertEquals(categoria.getId(), categoriaAtualizada.getId())
    );
  }

  @Test
  void shouldDeleteCategoria() {
    Categoria categoria = categoriaService.create(categoriaRecord);

    categoriaService.delete(categoria.getId());

    assertFalse(categoriaService.existsCategoriaById(categoria.getId()));
  }

  @Test
  void shouldFindCategoria() {
    Categoria categoria = categoriaService.create(categoriaRecord);

    Categoria categoriaEncontrada = categoriaService.find(categoria.getId());

    assertAll(
      () -> assertNotNull(categoriaEncontrada),
      () -> assertEquals(categoria.getId(), categoriaEncontrada.getId())
    );
  }

  @Test
  void shouldCheckIfExistsCategoriaById() {
    Categoria categoria = categoriaService.create(categoriaRecord);

    assertAll(
      () -> assertTrue(categoriaService.existsCategoriaById(categoria.getId())),
      () -> assertFalse(categoriaService.existsCategoriaById(9999L))
    );
  }

  @Test
  void shouldFindAllCategorias() {
    Categoria categoria1 = categoriaService.create(categoriaRecord);
    Categoria categoria2 = categoriaService.create(categoriaRecord);
    Categoria categoria3 = categoriaService.create(categoriaRecord);

    var categorias = categoriaService.findAllCategorias(List.of(categoria1.getId(),
      categoria2.getId(), categoria3.getId()));

    assertAll(
      () -> assertEquals(3, categorias.size()),
      () -> assertTrue(categorias.stream().anyMatch(c -> c.getId().equals(categoria1.getId()))),
      () -> assertTrue(categorias.stream().anyMatch(c -> c.getId().equals(categoria2.getId()))),
      () -> assertTrue(categorias.stream().anyMatch(c -> c.getId().equals(categoria3.getId())))
    );
  }

  @Test
  void shouldDeleteAllCategorias() {
    categoriaService.create(categoriaRecord);

    categoriaService.deleteAll();

    assertTrue(categoriaRepositoryPort.findAllByIdIn(List.of()).isEmpty());
  }

  @Test
  void shouldThrowExceptionWhenUpdateCategoriaNonexistent() {
    CategoriaRecord categoriaUpdateInexistent = new CategoriaRecord(9999L, "Categoria Inexistente");
    Exception exception = null;

    try {
      categoriaService.update(categoriaUpdateInexistent);
    } catch (Exception e) {
      exception = e;
    }

    assertNotNull(exception);
    assertTrue(exception.getMessage().contains("Categoria não encontrada"));
  }

  @Test
  void shouldThrowExceptionWhenDeleteCategoriaNonexistent() {
    Exception exception = null;

    try {
      categoriaService.delete(9999L);
    } catch (Exception e) {
      exception = e;
    }

    assertNotNull(exception);
    assertTrue(exception.getMessage().contains("Categoria não encontrada"));
  }

  @Test
  void shouldFindCategoriaInexistenteRetornaNull() {
    Categoria categoria = categoriaService.find(9999L);

    assertNull(categoria);
  }
}