package com.baekgwa.transfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dto.AccountTransferDto;

/**
 * PackageName : com.baekgwa.transfer.client
 * FileName    : AccountClient
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@Component
@FeignClient(name = "account-service", url = "${api.url.account-service}")
public interface AccountClient {

	@PostMapping("/account/withdraw")
	AccountTransferDto.AccountWithdrawResponse withdraw(
		@RequestBody AccountTransferDto.AccountWithdrawRequest request);

	@PostMapping("/account/deposit")
	AccountTransferDto.AccountDepositResponse deposit(
		@RequestBody AccountTransferDto.AccountDepositRequest request);

	@PostMapping("/account/withdraw/rollback")
	AccountTransferDto.AccountWithdrawRollbackResponse withdrawRollback(
		@RequestBody AccountTransferDto.AccountWithdrawRollbackRequest request);
}
