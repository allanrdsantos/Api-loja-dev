package loja.application.ports;

import loja.domain.categoria.Categoria;

import java.util.List;

public interface CategoriaRepositoryPort {

  Categoria save(Categoria categoria);

  Categoria findAllById(Long id);

  boolean existsById(Long idCategoria);

  void deleteById(Long idCategoria);

  List<Categoria> findAllByIdIn(List<Long> idsCategorias);

  void deleteAll();
}
