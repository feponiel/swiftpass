package com.feponiel.swiftpass.infrastructure.http.exception;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.feponiel.swiftpass.domain.application.usecases.exceptions.DomainException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
  private final DomainExceptionMapper domainExceptionMapper;

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ProblemDetail> handleDomainException(DomainException exception, HttpServletRequest request) {
    var exceptionMeta = domainExceptionMapper.resolve(exception);

    var problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.valueOf(exceptionMeta.status()),
      exception.getMessage()
    );

    problem.setType(URI.create(exceptionMeta.typeURI()));
    problem.setTitle(HttpStatus.valueOf(exceptionMeta.status()).getReasonPhrase());
    problem.setInstance(URI.create(request.getRequestURI()));

    problem.setProperty("errorCode", exception.getCode());
    problem.setProperty("timestamp", Instant.now());

    return ResponseEntity
      .status(exceptionMeta.status())
      .contentType(MediaType.APPLICATION_PROBLEM_JSON)
      .body(problem);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
    var problem = ProblemDetail.forStatusAndDetail(
      HttpStatusCode.valueOf(422),
      "One or more fields are invalid!"
    );

    problem.setType(URI.create("/errors/validation-failed"));
    problem.setTitle(HttpStatus.valueOf(422).getReasonPhrase());
    problem.setInstance(URI.create(request.getRequestURI()));

    List<Map<String, String>> violations = exception
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .map(error -> Map.of(
        "field", error.getField(),
        "message", error.getDefaultMessage()
      ))
      .toList();

    problem.setProperty("violations", violations);
    problem.setProperty("timestamp", Instant.now());

    return ResponseEntity
      .status(422)
      .contentType(MediaType.APPLICATION_PROBLEM_JSON)
      .body(problem);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleUnexpected(Exception exception, HttpServletRequest request) {
    log.error("Unexpected error on {} {}", request.getMethod(), request.getRequestURI(), exception);

    var problem = ProblemDetail.forStatusAndDetail(
      HttpStatusCode.valueOf(500),
      "An unexpected error occurred"
    );

    problem.setType(URI.create("/errors/internal"));
    problem.setTitle(HttpStatus.valueOf(500).getReasonPhrase());
    problem.setInstance(URI.create(request.getRequestURI()));

    problem.setProperty("timestamp", Instant.now());

    return ResponseEntity.status(500)
      .contentType(MediaType.APPLICATION_PROBLEM_JSON)
      .body(problem);
  }
}
