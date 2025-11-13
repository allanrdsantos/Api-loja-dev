package loja.infrastructure.inbound.rest.produtocontroller;

import loja.domain.produto.Produto;

import java.math.BigDecimal;

public class ProdutoControllerHelperTest {

  public static Produto createProdutoEntity() {
    Produto produto = new Produto();
    produto.setId(null);
    produto.setNome("Placa de Video");
    produto.setPreco(BigDecimal.valueOf(1000));
    produto.setDescricao("Placa de Video 32Gb");
    return produto;
  }
}
