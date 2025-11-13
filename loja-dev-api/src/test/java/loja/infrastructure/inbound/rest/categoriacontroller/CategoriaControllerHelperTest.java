package loja.infrastructure.inbound.rest.categoriacontroller;

import loja.domain.categoria.Categoria;
import loja.infrastructure.inbound.record.CategoriaRecord;

public class CategoriaControllerHelperTest {

  public static CategoriaRecord createRecordCategoria() {
    return new CategoriaRecord(
      null,
      "Categoria Teste"
    );
  }

  public static Categoria createRecordCategoriaEntity() {
    Categoria categoria = new Categoria();
    categoria.setId(null);
    categoria.setNomeCategoria("Categoria Teste");
    return categoria;
  }
}
