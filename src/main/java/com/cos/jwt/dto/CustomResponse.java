package com.cos.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
    private boolean success;

    private T result;

    private Error error;


    //    public static CommonResponse response(Object data){
//        CommonResponse commonResponse = CommonResponse.builder().data(data).build();
//        return commonResponse;
//    }
    public static <T> CustomResponse<T> success(T data){
        return new CustomResponse<>(true,data,null);
    }
    public static <T> CustomResponse<T> successHeader(T data){
        return new CustomResponse<>(true, data, null);
    }
    public static <T> CustomResponse<T> fail(String code, String message){
        return new CustomResponse<>(false, null, new Error(code, message));
    }
    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;

    }
}
