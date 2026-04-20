//package org.example.library.exception;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
//        // Ловим ошибку и возвращаем её текст с кодом 400 (Bad Request)
//        // Вместо стандартной 500-й
//        return ResponseEntity.status(400).body(e.getMessage());
//    }
//
//}
