package com.ruoyi.common.exception;


public class FlowException extends RuntimeException {
    /**
     * 错误消息
     */
    private String defaultMessage;

    public FlowException(String message) {
        this.defaultMessage = message;
    }

    @Override
    public String getMessage() {

        return this.defaultMessage;
    }

}
