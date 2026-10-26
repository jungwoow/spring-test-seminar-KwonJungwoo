package com.example.seminar.service;

import com.example.seminar.domain.Account;
import com.example.seminar.exception.AccountNotFoundException;
import com.example.seminar.exception.InvalidTransferException;
import com.example.seminar.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(
            Long senderId,
            Long receiverId,
            BigDecimal amount
    ) {

        // 1. 이체 금액 검증
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException(
                    "Transfer amount must be positive"
            );
        }

        // 2. 발신인 계좌 조회
        Account sender = accountRepository.findById(senderId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Sender account not found with id: "
                                        + senderId
                        )
                );

        // 3. 수취인 계좌 조회
        Account receiver = accountRepository.findById(receiverId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Receiver account not found with id: "
                                        + receiverId
                        )
                );

        // 4. 동일 계좌 이체 방지
        if (senderId.equals(receiverId)) {
            throw new InvalidTransferException(
                    "Cannot transfer to the same account"
            );
        }

        // 5. 잔액 부족 검증
        if (sender.getAmount().compareTo(amount) < 0) {
            throw new InvalidTransferException(
                    "Insufficient balance"
            );
        }

        // 6. 새로운 잔액 계산
        BigDecimal senderNewAmount =
                sender.getAmount().subtract(amount);

        BigDecimal receiverNewAmount =
                receiver.getAmount().add(amount);

        // 7. 계좌 잔액 변경
        sender.changeAmount(senderNewAmount);
        receiver.changeAmount(receiverNewAmount);

        // JPA 변경 감지로 트랜잭션 커밋 시 DB에 반영
    }
}