package loja.infrastructure.handlers;

import loja.infrastructure.exceptions.CategoriaNotFoundException;
import loja.infrastructure.exceptions.ProdutoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerException {

  @ExceptionHandler(ProdutoNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProdutoException(ProdutoNotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Produto não encontrado ou inexistente");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CategoriaNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleCategoriaException(CategoriaNotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Categoria não encontrado ou inexistente");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }
}
