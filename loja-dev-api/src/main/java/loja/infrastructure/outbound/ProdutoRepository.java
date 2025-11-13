package loja.infrastructure.outbound;

import loja.application.ports.ProdutoRepositoryPort;
import loja.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryPort {
}
