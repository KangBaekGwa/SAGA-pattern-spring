package com.baekgwa.transfer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baekgwa.transfer.service.TransferService;

import dto.TransferDto;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.transfer.controller
 * FileName    : TransferController
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/transfer")
public class TransferController {

	private final TransferService transferService;

	@PostMapping
	public TransferDto.TransferResponse transfer(
		@RequestBody TransferDto.TransferRequest transferRequest
	) {
		return transferService.transfer(transferRequest);
	}
}
