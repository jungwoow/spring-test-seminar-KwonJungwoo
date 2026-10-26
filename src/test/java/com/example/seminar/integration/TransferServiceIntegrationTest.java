package com.example.seminar.integration;

import com.example.seminar.domain.Account;
import com.example.seminar.exception.AccountNotFoundException;
import com.example.seminar.repository.AccountRepository;
import com.example.seminar.service.TransferService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class TransferServiceIntegrationTest {

    // TODO 0-1: @Autowired를 사용하여 실제 TransferService Bean을 주입받으세요.

    // TODO 0-2: @Autowired를 사용하여 실제 AccountRepository Bean을 주입받으세요.


    // =====================================================
    // 테스트 데이터 정리
    // =====================================================

    @AfterEach
    void cleanup() {

        // TODO 0-3: 각 테스트가 끝나면 Repository에 저장된 계좌 데이터를 삭제하세요.
    }


    // =====================================================
    // [실습 2-1] 정상 이체 통합 테스트
    // =====================================================

    @Test
    @DisplayName("실제 DB에서 정상적인 계좌 이체가 이루어진다")
    void transferSuccess() {

        // 시나리오
        // - 발신인: John, 잔액 1000원
        // - 수취인: Jane, 잔액 1000원
        // - 이체 금액: 100원
        //
        // 예상 결과
        // - 실제 DB에 발신인 잔액 900원 저장
        // - 실제 DB에 수취인 잔액 1100원 저장

        // Given
        // TODO 1-1: Account 객체 2개를 생성하고 accountRepository.save()로 H2 DB에 저장하세요.

        // TODO 1-2: 저장된 계좌의 ID를 확인하세요. (JPA가 ID를 자동 생성)


        // When
        // TODO 1-3: 저장된 계좌 ID를 사용하여 100원을 이체하는 transferMoney()를 호출하세요.


        // Then
        // TODO 1-4: findById()로 두 계좌를 실제 DB에서 다시 조회하세요.

        // TODO 1-5: assertThat()으로 발신인 잔액이 900원인지 검증하세요.

        // TODO 1-6: 수취인 잔액이 1100원인지 검증하세요.

        fail("TODO: 정상 이체 통합 테스트를 완성하세요.");
    }


    // =====================================================
    // [실습 2-2] 예외 플로우 - 발신인 계좌 없음
    // =====================================================

    @Test
    @DisplayName("발신인 계좌가 없으면 예외가 발생한다")
    void senderNotFound() {

        // 시나리오
        // - 발신인 ID: 999L (DB에 존재하지 않음)
        // - 수취인 ID: 2L
        // - 이체 금액: 100원
        //
        // 예상 결과
        // - AccountNotFoundException 발생
        // - 예외 메시지에 "Sender" 포함

        // Given
        // TODO 2-1: 테스트에 사용할 H2 DB에 ID 999인 계좌가 존재하지 않는지 확인하세요.


        // When & Then
        // TODO 2-2: 존재하지 않는 발신인 ID로 transferMoney()를 호출하세요.

        // TODO 2-3: AccountNotFoundException 발생 여부와 예외 메시지에 "Sender"가 포함되었는지 검증하세요.

        fail("TODO: 발신인 없음 통합 테스트를 완성하세요.");
    }


    // =====================================================
    // [과제 3] 예외 플로우 - 수취인 계좌 없음
    // =====================================================

    @Test
    @DisplayName("수취인 계좌가 없으면 잔액이 변경되지 않는다")
    void receiverNotFound() {

        // 시나리오
        // - 발신인: John, 잔액 1000원 (DB에 존재)
        // - 수취인 ID: 999L (DB에 존재하지 않음)
        // - 이체 금액: 100원
        //
        // 예상 결과
        // - AccountNotFoundException 발생
        // - 예외 메시지에 "Receiver" 포함
        // - DB에 저장된 발신인 잔액은 1000원으로 유지

        // Given
        // TODO 3-1: 발신인 계좌를 생성하고 실제 H2 DB에 저장하세요.

        // TODO 3-2: 존재하지 않는 수취인 계좌 ID를 준비하세요.
        //  단위 테스트와 달리 Mock을 사용할 필요는 없습니다.


        // When & Then
        // TODO 3-3: 존재하지 않는 수취인 계좌로 이체를 시도하고 예외 발생 여부를 검증하세요.

        // TODO 3-4: 예외 메시지에 "Receiver"가 포함되어 있는지 확인하세요.

        // TODO 3-5: 실제 DB에서 발신인 계좌를 다시 조회하세요.

        // TODO 3-6: 발신인의 잔액이 1000원으로 유지되었는지 검증하세요.

        fail("TODO: 수취인 없음 통합 테스트를 완성하세요.");
    }
}