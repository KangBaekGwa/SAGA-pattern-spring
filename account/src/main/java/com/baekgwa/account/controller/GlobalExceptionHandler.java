package com.baekgwa.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * PackageName : com.baekgwa.account.controller
 * FileName    : GlobalExceptionHandler
 * Author      : Baekgwa
 * Date        : 25. 12. 10.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 10.     Baekgwa               Initial creation
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getMessage());
	}
}
