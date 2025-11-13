package loja.application.usecases;

import loja.domain.categoria.Categoria;
import loja.infrastructure.inbound.record.CategoriaRecord;

import java.util.List;

public interface CategoriaUC {

  Categoria create(CategoriaRecord categoriaRecord);

  Categoria update(CategoriaRecord categoriaRecord);

  void delete(Long idCategoria);

  Categoria find(Long idCategoria);

  boolean existsCategoriaById(Long idCategoria);

  List<Categoria> findAllCategorias(List<Long> idsCategorias);

  void deleteAll();
}
