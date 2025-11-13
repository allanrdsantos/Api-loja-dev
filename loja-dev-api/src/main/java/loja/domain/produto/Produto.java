package loja.domain.produto;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import loja.domain.categoria.Categoria;

import java.math.BigDecimal;

@Entity
@Table(name = "d_produtos")
@SequenceGenerator(name = "d_produtos_seq", allocationSize = 1)
public class Produto {

  @ManyToOne
  @JoinColumn(name = "id_categoria")
  private Categoria categoria;

  @Id
  @GeneratedValue(generator = "d_produtos_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id_produto")
  private Long id;

  @NotBlank
  @Size(max = 100)
  @Column(name = "nome", nullable = false)
  private String nome;

  @NotBlank
  @DecimalMin("0.00")
  @DecimalMax("9999999.99")
  @Column(name = "preco")
  private BigDecimal preco = BigDecimal.ZERO;

  @Size(max = 100)
  @Column(name = "descricao")
  private String descricao;

  public BigDecimal getPreco() {
    return preco;
  }

  public void setPreco(BigDecimal preco) {
    this.preco = preco;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }
}
