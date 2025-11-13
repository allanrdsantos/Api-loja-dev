package loja.application.services;

import loja.application.ports.ProdutoRepositoryPort;
import loja.application.usecases.ProdutoUC;
import loja.domain.produto.Produto;
import loja.infrastructure.exceptions.ProdutoNotFoundException;
import loja.infrastructure.inbound.record.ProdutoRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProdutoService implements ProdutoUC {

  private final ProdutoRepositoryPort produtoRepositoryPort;

  public ProdutoService(ProdutoRepositoryPort produtoRepositoryPort) {
    this.produtoRepositoryPort = produtoRepositoryPort;
  }

  @Override
  public Produto create(ProdutoRecord produtoRecord) {
    Produto produto = produtoRecord.toEntity();
    return produtoRepositoryPort.save(produto);
  }

  @Override
  public Produto update(ProdutoRecord produtoRecord) {
    Produto produtoCurrent = produtoRepositoryPort.findAllById(produtoRecord.id());
    if (Objects.isNull(produtoCurrent)) {
      throw new ProdutoNotFoundException("Produto não encontrado com id: " + produtoRecord.id());
    }
    produtoCurrent = produtoRecord.toEntity();
    return produtoRepositoryPort.save(produtoCurrent);
  }

  @Override
  public void delete(Long idProduto) {
    if (!produtoRepositoryPort.existsById(idProduto)) {
      throw new ProdutoNotFoundException("Produto não encontrado com id: " + idProduto);
    }
    produtoRepositoryPort.deleteById(idProduto);
  }

  @Override
  public Produto find(Long idProduto) {
    return produtoRepositoryPort.findAllById(idProduto);
  }

  @Override
  public List<Produto> findAllProdutos(List<Long> idsProdutos) {
    return produtoRepositoryPort.findAllByIdIn(idsProdutos);
  }

  @Override
  public void deleteAll() {
    produtoRepositoryPort.deleteAll();
  }
}
