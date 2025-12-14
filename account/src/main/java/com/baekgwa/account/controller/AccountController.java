package com.baekgwa.account.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baekgwa.account.service.AccountService;

import dto.AccountTransferDto;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.account.controller
 * FileName    : AccountController
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/withdraw")
	public AccountTransferDto.AccountWithdrawResponse withdraw(
		@RequestBody AccountTransferDto.AccountWithdrawRequest request
	) {
		return accountService.withdraw(request);
	}

	@PostMapping("/deposit")
	public AccountTransferDto.AccountDepositResponse deposit(
		@RequestBody AccountTransferDto.AccountDepositRequest request
	) {
		return accountService.deposit(request);
	}

	@PostMapping("/withdraw/rollback")
	public AccountTransferDto.AccountWithdrawRollbackResponse withdrawRollback(
		@RequestBody AccountTransferDto.AccountWithdrawRollbackRequest request
	) {
		return accountService.withdrawRollback(request);
	}
}
