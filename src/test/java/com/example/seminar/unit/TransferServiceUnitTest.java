package com.example.seminar.unit;

import com.example.seminar.domain.Account;
import com.example.seminar.exception.AccountNotFoundException;
import com.example.seminar.exception.InvalidTransferException;
import com.example.seminar.repository.AccountRepository;
import com.example.seminar.service.TransferService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceUnitTest {

    // TODO 0-1: @Mock을 사용하여 AccountRepository의 Mock 객체를 생성하세요.
    @Mock
    AccountRepository accountRepository;

    // TODO 0-2: @InjectMocks를 사용하여 TransferService에 Mock Repository를 주입하세요.
    @InjectMocks
    TransferService transferService;

    // =====================================================
    // [실습 1-1] 정상 이체 플로우
    // =====================================================

    @Test
    @DisplayName("정상적인 계좌 이체에 성공한다")
    void transferSuccess() {

        // 시나리오
        // - 발신인 ID: 1L, 잔액: 1000원
        // - 수취인 ID: 2L, 잔액: 1000원
        // - 이체 금액: 100원
        //
        // 예상 결과
        // - 발신인 잔액: 900원
        // - 수취인 잔액: 1100원

        // Given
        // TODO 1-1: 발신인과 수취인 Account 객체를 생성하세요.
        Account sender = new Account(1L, "John", new BigDecimal("1000"));
        Account receiver = new Account(2L, "Jane", new BigDecimal("1000"));

        // TODO 1-2: given().willReturn()을 사용하여
        //  각 계좌 ID로 조회했을 때 해당 객체가 반환되도록 Mock Repository의 동작을 설정하세요.
        given(accountRepository.findById(1L)).willReturn(Optional.of(sender));
        given(accountRepository.findById(2L)).willReturn(Optional.of(receiver));

        // When
        // TODO 1-3: transferMoney()를 호출하여 1번 계좌에서 2번 계좌로 100원을 이체하세요.
        transferService.transferMoney(1L, 2L, new BigDecimal("100"));

        // Then
        // TODO 1-4: assertThat()으로 두 계좌의 변경된 잔액이 예상 결과와 일치하는지 검증하세요.
        assertThat(sender.getAmount())
                .isEqualByComparingTo("900");

        assertThat(receiver.getAmount())
                .isEqualByComparingTo("1100");

        // TODO 1-5: verify()를 사용하여 findById(1L), findById(2L)가 각각 한 번씩 호출되었는지 검증하세요.
        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);

    }


    // =====================================================
    // [실습 1-2] 예외 플로우 - 발신인 계좌 없음
    // =====================================================

    @Test
    @DisplayName("발신인 계좌가 없으면 예외가 발생한다")
    void senderNotFound() {

        // 시나리오
        // - 발신인 ID: 999L (존재하지 않음)
        // - 수취인 ID: 2L
        // - 이체 금액: 100원
        //
        // 예상 결과
        // - AccountNotFoundException 발생
        // - 예외 메시지에 "Sender" 포함
        // - 수취인 계좌 조회가 실행되지 않음

        // Given
        // TODO 2-1: findById(999L)이 호출되면 Optional.empty()를 반환하도록 설정하세요.
        given(accountRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        // TODO 2-2: assertThatThrownBy()로 transferMoney()를 실행하고
        //  AccountNotFoundException 발생 여부를 검증하세요.
        assertThatThrownBy(() ->
                transferService.transferMoney(999L, 2L, new BigDecimal("100")
                )
        )
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Sender");

        verify(accountRepository, never()).findById(2L);

        // TODO 2-3: 예외 메시지에 "Sender"가 포함되어 있는지 검증하세요.
        assertThatThrownBy(() ->
                transferService.transferMoney(999L, 2L, new BigDecimal("100")
                ))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Sender");

        // TODO 2-4: verify()와 never()를 사용하여 findById(2L)이 호출되지 않았음을 검증하세요.
        verify(accountRepository, never()).findById(2L);

    }


    // =====================================================
    // [실습 1-3] 예외 플로우 - 수취인 계좌 없음
    // =====================================================

    @Test
    @DisplayName("수취인 계좌가 없으면 예외가 발생한다")
    void receiverNotFound() {

        // 시나리오
        // - 발신인 ID: 1L, 잔액: 1000원
        // - 수취인 ID: 999L (존재하지 않음)
        // - 이체 금액: 100원
        //
        // 예상 결과
        // - AccountNotFoundException 발생
        // - 예외 메시지에 "Receiver" 포함
        // - 발신인의 잔액은 1000원으로 유지

        // Given
        // TODO 3-1: 잔액이 1000원인 발신인 객체를 생성하세요.
        Account sender = new Account(1L, "John", new BigDecimal("1000"));

        // TODO 3-2: 발신인 계좌 조회는 성공하고 수취인 계좌 조회는 실패하도록 Mock을 설정하세요.
        given(accountRepository.findById(1L)).willReturn(Optional.of(sender));
        given(accountRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        // TODO 3-3: 수취인 계좌가 존재하지 않는 상황에서 transferMoney()를 실행하고 예외를 검증하세요.
        // TODO 3-4: 예외 메시지에 "Receiver"가 포함되어 있는지 검증하세요.
        assertThatThrownBy(() ->
                transferService.transferMoney(1L, 999L, new BigDecimal("100")
                )
        )
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Receiver");



        // TODO 3-5: 이체 실패 후 발신인 잔액이 1000원으로 유지되었는지 검증하세요.
        assertThat(sender.getAmount())
                .isEqualByComparingTo("1000");
    }


    // =====================================================
    // [과제 1] 예외 플로우 - 잔액 부족
    // =====================================================

    @Test
    @DisplayName("잔액이 부족하면 이체에 실패한다")
    void insufficientBalance() {

        // 시나리오
        // - 발신인 ID: 1L, 잔액: 100원
        // - 수취인 ID: 2L, 잔액: 1000원
        // - 이체 금액: 500원
        //
        // 예상 결과
        // - InvalidTransferException 발생
        // - 예외 메시지에 "Insufficient balance" 포함
        // - 두 계좌의 잔액이 모두 변경되지 않음

        // Given
        // TODO 4-1: 발신인과 수취인 계좌를 생성하세요.


        // TODO 4-2: 두 계좌를 정상적으로 조회할 수 있도록 Mock Repository의 동작을 설정하세요.

        // When & Then
        // TODO 4-3: 잔액보다 큰 금액을 이체하도록 transferMoney()를 호출하고 예외를 검증하세요.

        // TODO 4-4: 예외 메시지가 올바른지 확인하세요.

        // TODO 4-5: 이체 실패 후 두 계좌의 잔액이 기존 값으로 유지되었는지 검증하세요.

        fail("TODO: 잔액 부족 테스트를 완성하세요.");
    }


    // =====================================================
    // [과제 2] 예외 플로우 - 음수 금액
    // =====================================================

    @Test
    @DisplayName("음수 금액은 이체할 수 없다")
    void negativeAmount() {

        // 시나리오
        // - 발신인 ID: 1L
        // - 수취인 ID: 2L
        // - 이체 금액: -100원
        //
        // 예상 결과
        // - InvalidTransferException 발생
        // - 예외 메시지에 "positive" 포함
        // - Repository와의 상호작용이 발생하지 않음

        // When & Then
        // TODO 5-1: -100원 이체를 시도하고 InvalidTransferException 발생 여부를 검증하세요.

        // TODO 5-2: 예외 메시지에 "positive"가 포함되어 있는지 확인하세요.

        // TODO 5-3: verifyNoInteractions()를 사용하여
        //  AccountRepository의 메서드가 호출되지 않았는지 검증하세요.

        fail("TODO: 음수 금액 테스트를 완성하세요.");
    }
}