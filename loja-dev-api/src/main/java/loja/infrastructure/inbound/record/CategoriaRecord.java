package loja.infrastructure.inbound.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import loja.domain.categoria.Categoria;

public record CategoriaRecord(

  Long id,

  @Size(max = 100, message = "O campo nome deve ter no máximo 100 caracteres")
  @NotBlank(message = "O campo nome categoria é obrigatório")
  String nomeCategoria
) {

  public Categoria toEntity() {
    Categoria categoria = new Categoria();
    categoria.setId(this.id);
    categoria.setNomeCategoria(this.nomeCategoria);
    return categoria;
  }
}
