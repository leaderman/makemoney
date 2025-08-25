package io.github.leaderman.makemoney.hustle.domain.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Response<T> {
  private int code;
  private String message;
  private T data;

  public static Response<Void> unauthorized() {
    return new Response<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
  }

  public static Response<Void> unauthorized(String message) {
    return new Response<>(HttpStatus.UNAUTHORIZED.value(), message, null);
  }

  public static Response<Void> forbidden() {
    return new Response<>(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null);
  }

  public static Response<Void> forbidden(String message) {
    return new Response<>(HttpStatus.FORBIDDEN.value(), message, null);
  }

  public static Response<Void> badRequest() {
    return new Response<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
  }

  public static Response<Void> badRequest(String message) {
    return new Response<>(HttpStatus.BAD_REQUEST.value(), message, null);
  }

  public static Response<Void> ok() {
    return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
  }

  public static Response<Void> ok(String message) {
    return new Response<>(HttpStatus.OK.value(), message, null);
  }

  public static <T> Response<T> ok(T data) {
    return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
  }

  public static Response<Void> internalServerError() {
    return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        null);
  }

  public static Response<Void> internalServerError(String message) {
    return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
  }
}
