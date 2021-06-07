package com.github.springboot.template.utils;

import lombok.Data;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * facade接口注解， 用于统一对facade进行参数校验及异常捕获
 * <pre>
 *      注意，使用该注解需要注意，该方法的返回值必须是BaseResponse的子类
 * </pre>
 *
 * <p>
 * <span>常用校验注解:</span>
 * @AssertFalse @AssertTrue  检验boolean类型的值
 * @DecimalMax @DecimalMin  限定被标注的属性的值的大小
 * @Digits(intege=,fraction=) 限定被标注的属性的整数位数和小数位数
 * @Future 检验给定的日期是否比现在晚
 * @Past 校验给定的日期是否比现在早
 * @Max 检查被标注的属性的值是否小于等于给定的值
 * @Min 检查被标注的属性的值是否大于等于给定的值
 * @NotNull 检验被标注的值不为空
 * @Null 检验被标注的值为空
 * @Pattern(regex=,flag=) 检查该字符串是否能够在match指定的情况下被regex定义的正则表达式匹配
 * @Size(min=,max=) 检查被标注元素的长度
 * @Valid 递归的对关联的对象进行校验
 * </p>
 */
public class BeanValidator {


    private static final Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure().failFast(true)
            .buildValidatorFactory().getValidator();

    /**
     * 校验对象
     *
     * @param object object
     * @param groups groups
     */
    public static void validateObject(Object object, Class<?>... groups) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (constraintViolations.stream().findFirst().isPresent()) {
            throw new ValidationException(constraintViolations.stream().findFirst().get().getMessage());
        }
    }


    /**
     * 校验对象bean
     *
     * @param t      bean
     * @param groups 校验组
     * @return ValidResult
     */
    public static <T> ValidResult validateBean(T t, Class<?>... groups) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验bean的某一个属性
     *
     * @param obj          bean
     * @param propertyName 属性名称
     * @return ValidResult
     */
    public static <T> ValidResult validateProperty(T obj, String propertyName) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validateProperty(obj, propertyName);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(propertyName, violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验结果类
     */
    @Data
    public static class ValidResult {

        /**
         * 是否有错误
         */
        private boolean hasErrors;

        /**
         * 错误信息
         */
        private List<ErrorMessage> errors;

        public ValidResult() {
            this.errors = new ArrayList<>();
        }

        public boolean hasErrors() {
            return hasErrors;
        }

        public void setHasErrors(boolean hasErrors) {
            this.hasErrors = hasErrors;
        }

        /**
         * 获取所有验证信息
         *
         * @return 集合形式
         */
        public List<ErrorMessage> getAllErrors() {
            return errors;
        }

        /**
         * 获取所有验证信息
         *
         * @return 字符串形式
         */
        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            for (ErrorMessage error : errors) {
                sb.append(error.getPropertyPath()).append(":").append(error.getMessage()).append(" ");
            }
            return sb.toString();
        }

        public void addError(String propertyName, String message) {
            this.errors.add(new ErrorMessage(propertyName, message));
        }
    }

    @Data
    public static class ErrorMessage {

        private String propertyPath;

        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(String propertyPath, String message) {
            this.propertyPath = propertyPath;
            this.message = message;
        }
    }


}
