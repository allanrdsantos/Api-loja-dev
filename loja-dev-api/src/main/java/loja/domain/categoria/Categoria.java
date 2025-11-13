package loja.domain.categoria;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "m_categoria")
@SequenceGenerator(name = "m_categorias_seq", allocationSize = 1)
public class Categoria {

  @Id
  @GeneratedValue(generator = "m_categorias_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id_categoria")
  private Long id;

  @NotBlank
  @Size(max = 100)
  @Column(name = "nome_categoria", nullable = false)
  private String nomeCategoria;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNomeCategoria() {
    return nomeCategoria;
  }

  public void setNomeCategoria(String nomeCategoria) {
    this.nomeCategoria = nomeCategoria;
  }
}
