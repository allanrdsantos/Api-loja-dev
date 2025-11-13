package loja.application.services;

import loja.application.ports.CategoriaRepositoryPort;
import loja.application.ports.ProdutoRepositoryPort;
import loja.application.usecases.CategoriaUC;
import loja.domain.categoria.Categoria;
import loja.infrastructure.exceptions.CategoriaNotFoundException;
import loja.infrastructure.inbound.record.CategoriaRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService implements CategoriaUC {

  private final CategoriaRepositoryPort categoriaRepositoryPort;

  private final ProdutoRepositoryPort produtoRepositoryPort;

  public CategoriaService(CategoriaRepositoryPort categoriaRepositoryPort, ProdutoRepositoryPort produtoRepositoryPort) {
    this.categoriaRepositoryPort = categoriaRepositoryPort;
    this.produtoRepositoryPort = produtoRepositoryPort;
  }

  @Override
  @Transactional
  public Categoria create(CategoriaRecord categoriaRecord) {
    Categoria categoria = categoriaRecord.toEntity();
    return categoriaRepositoryPort.save(categoria);
  }

  @Override
  public Categoria update(CategoriaRecord categoriaRecord) {
    Categoria categoriaCurrent = categoriaRepositoryPort.findAllById(categoriaRecord.id());
    if (categoriaCurrent == null) {
      throw new CategoriaNotFoundException("Categoria não encontrada com id: " + categoriaRecord.id());
    }
    categoriaCurrent = categoriaRecord.toEntity();
    return categoriaRepositoryPort.save(categoriaCurrent);
  }

  @Override
  @Transactional
  public void delete(Long idCategoria) {
    if (!categoriaRepositoryPort.existsById(idCategoria)) {
      throw new CategoriaNotFoundException("Categoria não encontrada com id: " + idCategoria);
    }
    produtoRepositoryPort.deleteByCategoriaId(idCategoria);
    categoriaRepositoryPort.deleteById(idCategoria);

  }

  @Override
  public Categoria find(Long idCategoria) {
    return categoriaRepositoryPort.findAllById(idCategoria);
  }

  @Override
  public boolean existsCategoriaById(Long idCategoria) {
    return categoriaRepositoryPort.existsById(idCategoria);
  }

  @Override
  public List<Categoria> findAllCategorias(List<Long> idsCategorias) {
    return categoriaRepositoryPort.findAllByIdIn(idsCategorias);
  }

  @Override
  public void deleteAll() {
    produtoRepositoryPort.deleteAll();
    categoriaRepositoryPort.deleteAll();
  }
}
