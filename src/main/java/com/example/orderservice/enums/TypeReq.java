package com.example.orderservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeReq {
    FEIGN_CLIENT("feign-client"),
    ;

    final String value;


}
