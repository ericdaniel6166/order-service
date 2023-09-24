package com.example.orderservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeReq {
    WEB_CLIENT("web-client"),
    FEIGN_CLIENT("feign-client"),
    ;

    final String value;


}
