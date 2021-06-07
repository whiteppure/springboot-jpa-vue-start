package com.github.springboot.template.exception;

import com.github.springboot.template.support.Status;

/**
 * Exception of entity not found.
 *
 * @author johnniang
 */
public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(Status.REQUEST_NOT_FOUND.getCode(),message);
    }


}
