package loja.infrastructure.inbound.record;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import loja.domain.categoria.Categoria;
import loja.domain.produto.Produto;

import java.math.BigDecimal;

public record ProdutoRecord(

  @NotNull(message = "O campo id da Categoria é obrigatório")
  Categoria categoria,

  Long id,

  @NotNull(message = "O campo nome e? obrigato?rio")
  @Size(max = 100)
  String nome,

  @NotNull(message = "O campo preco e? obrigato?rio")
  @DecimalMin("0.00")
  @DecimalMax("9999999.99")
  BigDecimal preco,

  @Size(max = 100)
  String descricao
) {

  public Produto toEntity() {
    Produto produto = new Produto();
    produto.setCategoria(this.categoria);
    produto.setId(this.id);
    produto.setNome(this.nome);
    produto.setPreco(this.preco);
    produto.setDescricao(this.descricao);
    return produto;
  }
}
