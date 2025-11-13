package loja.infrastructure.inbound.rest.produtocontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import loja.application.ports.CategoriaRepositoryPort;
import loja.application.ports.ProdutoRepositoryPort;
import loja.application.services.ProdutoService;
import loja.domain.categoria.Categoria;
import loja.domain.produto.Produto;
import loja.infrastructure.inbound.record.ProdutoRecord;
import loja.infrastructure.inbound.rest.ProdutoController;
import loja.infrastructure.inbound.rest.categoriacontroller.CategoriaControllerHelperTest;
import loja.infrastructure.outbound.CategoriaRepository;
import loja.infrastructure.outbound.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerIntegrationTest {

  public static final String URL = "/categorias/{idCategoria}/produtos";

  @Autowired
  private CategoriaRepositoryPort categoriaRepositoryPort;

  @Autowired
  private ProdutoRepositoryPort produtoRepositoryPort;

  @Autowired
  private CategoriaRepository categoriaRepository;

  ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private ProdutoController produtoController;

  @Autowired
  private ProdutoService produtoService;

  private ProdutoRecord produtoRecord;

  private Categoria categoria;

  private Produto produto;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    categoria = CategoriaControllerHelperTest.createRecordCategoriaEntity();
    categoriaRepositoryPort.save(categoria);

    produtoRecord = new ProdutoRecord(
      categoria, null, "Placa de Video", BigDecimal.valueOf(1000), "Placa de Video 32Gb");

    produto = ProdutoControllerHelperTest.createProdutoEntity();
    produto.setCategoria(categoria);
  }

  @AfterEach
  void tearDown() {
    produtoRepository.deleteAll();
    categoriaRepository.deleteAll();
  }

  @Test
  void shouldDelete() throws Exception {
    produtoRepositoryPort.save(produto);

    mockMvc.perform(delete(URL + "/{idProduto}", categoria.getId(), produto.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenDeleteProdutoDoesNotExist() throws Exception {
    mockMvc.perform(delete(URL + "/{idProduto}", categoria.getId(), 9999L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteAllProdutos() throws Exception {
    produtoRepositoryPort.save(produto);

    mockMvc.perform(delete(URL + "/deleteAll", categoria.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenProdutoDoesNotExist() throws Exception {
    long idInexistente = 9999L;

    mockMvc.perform(get(URL + "/{idProduto}", categoria.getId(), idInexistente)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldFindProduto() throws Exception {
    produtoRepositoryPort.save(produto);

    mockMvc.perform(get(URL + "/{idProduto}", categoria.getId(), produto.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldFindAllProduto() throws Exception {
    produtoRepositoryPort.save(produto);

    mockMvc.perform(get(URL + "/all?idsProdutos=", categoria.getId(), produto.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldCreateProdutos() throws Exception {
    mockMvc.perform(post(URL, categoria.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(produtoRecord)))
      .andExpect(status().isCreated());
  }

  @Test
  void shouldReturnBadRequestWhenInvalidData() throws Exception {
    ProdutoRecord produtoInvalido = new ProdutoRecord(categoria, null, null, null, null);

    mockMvc.perform(post(URL, categoria.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(produtoInvalido)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdateProduto() throws Exception {
    produtoRepositoryPort.save(produto);

    Produto produtoUpdate = new Produto();
    produtoUpdate.setCategoria(categoria);
    produtoUpdate.setId(produto.getId());
    produtoUpdate.setNome("Placa de Video");
    produtoUpdate.setPreco(BigDecimal.valueOf(1000));
    produtoUpdate.setDescricao("Placa de Video 32Gb");

    mockMvc.perform(put(URL, categoria.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(produtoUpdate)))
      .andExpect(status().isOk());
  }

  @Test
  void shouldNotUpdateWhenProdutoDoesNotExist() throws Exception {
    produtoRepositoryPort.save(produto);

    Produto produtoUpdate = new Produto();
    produtoUpdate.setCategoria(categoria);
    produtoUpdate.setId(null);
    produtoUpdate.setNome("Placa de Video");
    produtoUpdate.setPreco(BigDecimal.valueOf(1000));
    produtoUpdate.setDescricao("Placa de Video 32Gb");

    mockMvc.perform(put(URL, categoria.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(produtoUpdate)))
      .andExpect(status().isNotFound());
  }


  @Test
  void shouldReturnUnsupportedMediaType() throws Exception {
    mockMvc.perform(post(URL, categoria.getId())
        .content("{\"nomeProduto\":\"Teste\"}")
        .contentType(MediaType.TEXT_PLAIN))
      .andExpect(status().isUnsupportedMediaType());
  }
}