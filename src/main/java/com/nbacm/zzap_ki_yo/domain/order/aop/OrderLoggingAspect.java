package com.nbacm.zzap_ki_yo.domain.order.aop;

import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
public class OrderLoggingAspect {

    // 포인트컷: 주문 생성 메소드와 주문 상태 변경 메소드
    @Pointcut("execution(* com.nbacm.zzap_ki_yo.domain.order.service.OrderServiceImpl.saveOrder(..)) || " +
            "execution(* com.nbacm.zzap_ki_yo.domain.order.service.OrderServiceImpl.updateOrder(..))")
    public void orderMethods() {}

    // 주문 생성 및 상태 변경 시 로그 남기기
    @AfterReturning(value = "orderMethods()", returning = "result")
    public void logOrder(JoinPoint joinPoint, Object result) {
        // 요청 시각 기록
        String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 메소드의 파라미터 가져오기
        Object[] args = joinPoint.getArgs();

        Long storeId = null;
        Long orderId = null;

        // 메소드 이름에 따라 파라미터에서 가게 ID 및 주문 ID 추출
        if (joinPoint.getSignature().getName().equals("saveOrder")) {
            storeId = (Long) args[1]; // saveOrder의 두 번째 파라미터가 storeId
            if (result instanceof OrderSaveResponse response) {
                orderId = response.getId(); // 결과에서 주문 ID 가져오기
            }
        } else if (joinPoint.getSignature().getName().equals("updateOrder")) {
            storeId = (Long) args[0]; // updateOrder의 첫 번째 파라미터가 storeId
            orderId = (Long) args[1]; // updateOrder의 두 번째 파라미터가 orderId
        }

        // 로그 출력
        log.info("Order Log - Request Time: {}, Store ID: {}, Order ID: {}", requestTime, storeId, orderId);
    }
}
