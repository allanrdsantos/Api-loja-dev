package loja.application.ports;

import loja.domain.produto.Produto;

import java.util.List;

public interface ProdutoRepositoryPort {

  Produto save(Produto produto);

  Produto findAllById(Long id);

  void deleteById(Long produto);

  boolean existsById(Long id);

  void deleteByCategoriaId(Long idCategoria);

  List<Produto> findAllByIdIn(List<Long> idsProdutos);

  void deleteAll();
}
