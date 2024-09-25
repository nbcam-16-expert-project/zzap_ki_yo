package com.nbacm.zzap_ki_yo.domain.exception;

//아직 완료되지 않은 건에 대해서 요청할 때
public class UncompletedException extends RuntimeException {
    public UncompletedException(String message) {super(message);}
}
