package loja.infrastructure.configs;

import loja.application.ports.CategoriaRepositoryPort;
import loja.application.ports.ProdutoRepositoryPort;
import loja.application.services.CategoriaService;
import loja.application.services.ProdutoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  public ProdutoService produtoService(ProdutoRepositoryPort produtoRepository) {
    return new ProdutoService(produtoRepository);
  }

  @Bean
  public CategoriaService categoriaService(CategoriaRepositoryPort categoriaRepositoryPort,
                                           ProdutoRepositoryPort produtoRepositoryPort) {
    return new CategoriaService(categoriaRepositoryPort, produtoRepositoryPort);
  }
}
