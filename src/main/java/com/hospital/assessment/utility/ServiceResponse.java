package com.hospital.assessment.utility;

import java.io.Serializable;

public interface ServiceResponse<T extends Serializable> extends Serializable{

    enum ResponseCode {
        ERROR("99", "Operation Error"), USER_NOT_FOUND("404", "USER NOT FOUND"),BAD_REQUEST("400", "BAD REQUEST"), SUCCESS("00", "Operation Successful"), INVALID_AGE_RANGE("02", "Age Must be Greater or Equal to %s");

        protected String code;

        protected String defaultMessage;



        ResponseCode(String code, String defaultMessage) {
            this.code = code;
            this.defaultMessage = defaultMessage;
        }


        public String getCode() {
            return code;
        }


        public String getDefaultMessage() {
            return defaultMessage;
        }


        @Override
        public String toString() {
            return getCode();
        }
    }


    String getResponseCode();

    String getResponseMsg();

    void setResponseCode(String responseCode);


    void setResponseMsg(String responseMsg);
}
