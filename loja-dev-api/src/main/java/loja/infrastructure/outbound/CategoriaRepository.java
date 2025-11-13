package loja.infrastructure.outbound;

import loja.application.ports.CategoriaRepositoryPort;
import loja.domain.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, CategoriaRepositoryPort {

}
