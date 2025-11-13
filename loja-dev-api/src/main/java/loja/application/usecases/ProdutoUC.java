package loja.application.usecases;

import loja.domain.produto.Produto;
import loja.infrastructure.inbound.record.ProdutoRecord;

import java.util.List;

public interface ProdutoUC {

  Produto create(ProdutoRecord produtoRecord);

  Produto update(ProdutoRecord produtoRecord);

  void delete(Long idProduto);

  Produto find(Long idProduto);

  List<Produto> findAllProdutos(List<Long> idsProdutos);

  void deleteAll();
}
