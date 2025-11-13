package loja.infrastructure.inbound.rest.categoriacontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import loja.application.ports.CategoriaRepositoryPort;
import loja.application.services.CategoriaService;
import loja.domain.categoria.Categoria;
import loja.infrastructure.inbound.record.CategoriaRecord;
import loja.infrastructure.inbound.rest.CategoriaController;
import loja.infrastructure.outbound.CategoriaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoriaControllerIntegrationTest {

  @Autowired
  private CategoriaRepositoryPort categoriaRepositoryPort;

  @Autowired
  private CategoriaRepository categoriaRepository;

  @Autowired
  private CategoriaController categoriaController;

  public static final String URL = "/categorias";

  ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private CategoriaService categoriaService;

  private CategoriaRecord categoriaRecord;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    categoriaRecord = CategoriaControllerHelperTest.createRecordCategoria();
  }

  @AfterEach
  void tearDown() {
    categoriaRepository.deleteAll();
  }

  @Test
  void shouldCreateCategoria() throws Exception {
    mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(categoriaRecord)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.nomeCategoria").value(categoriaRecord.nomeCategoria()));
  }

  @Test
  void shouldUpdateCategoria() throws Exception {
    Categoria categoria = CategoriaControllerHelperTest.createRecordCategoriaEntity();
    categoriaRepositoryPort.save(categoria);

    CategoriaRecord categoriaUpdate = new CategoriaRecord(
      categoria.getId(),
      "Categoria Teste Atualizada"
    );

    mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(categoriaUpdate)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nomeCategoria").value(categoriaUpdate.nomeCategoria()));
  }

  @Test
  void shouldFindCategoria() throws Exception {
    Categoria categoria = CategoriaControllerHelperTest.createRecordCategoriaEntity();
    categoriaRepositoryPort.save(categoria);

    mockMvc.perform(get(URL + "/" + categoria.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldFindAllCategorias() throws Exception {
    Categoria categoria = CategoriaControllerHelperTest.createRecordCategoriaEntity();
    categoriaRepositoryPort.save(categoria);

    mockMvc.perform(get(URL + "/all?idsCategorias=" + categoria.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldNotFindAllCategorias() throws Exception {
    mockMvc.perform(get(URL + "/all?idsCategorias=" + 999L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldDelete() throws Exception {
    Categoria categoria = CategoriaControllerHelperTest.createRecordCategoriaEntity();
    categoriaRepositoryPort.save(categoria);

    mockMvc.perform(delete(URL + "/{idCategoria}", categoria.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldDeleteAllCategorias() throws Exception {
    Categoria categoria = CategoriaControllerHelperTest.createRecordCategoriaEntity();
    categoriaRepositoryPort.save(categoria);

    mockMvc.perform(delete(URL + "/deleteAll")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenCategoriaDoesNotExist() throws Exception {
    long idInexistente = 9999L;

    mockMvc.perform(get(URL + "/" + idInexistente)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestWhenInvalidData() throws Exception {
    CategoriaRecord categoriaInvalida = new CategoriaRecord(null, ""); // nome vazio

    mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(categoriaInvalida)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateCategoriaDoesNotExist() throws Exception {
    CategoriaRecord categoriaUpdate = new CategoriaRecord(9999L, "Nova Categoria");

    mockMvc.perform(put(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(categoriaUpdate)))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnNotFoundWhenDeleteCategoriaDoesNotExist() throws Exception {
    mockMvc.perform(delete(URL + "/{idCategoria}", 9999L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnUnsupportedMediaType() throws Exception {
    mockMvc.perform(post(URL)
        .content("{\"nomeCategoria\":\"Teste\"}")
        .contentType(MediaType.TEXT_PLAIN))
      .andExpect(status().isUnsupportedMediaType());
  }
}