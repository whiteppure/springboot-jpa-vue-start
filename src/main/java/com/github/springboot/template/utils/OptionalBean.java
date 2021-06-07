package com.github.springboot.template.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 链式调用 bean 中 value 的方法,优雅对对象进行判空
 *
 * <pre>
 *     提供了和 Optional 一样的扩展方法，如 ifPresent()、orElse()等等，使用示例：
 *     
 *     User axin = new User();
 *     User.School school = new User.School();
 *     axin.setName("hello");
 *     
 *     // 1. 基本调用
 *     String value1 = OptionalBean.ofNullable(axin)
 *             .getBean(User::getSchool)
 *             .getBean(User.School::getAdress).get();
 *     System.out.println(value1);
 *
 *     // 2. 扩展的 isPresent方法 用法与 Optional 一样
 *     boolean present = OptionalBean.ofNullable(axin)
 *             .getBean(User::getSchool)
 *             .getBean(User.School::getAdress).isPresent();
 *     System.out.println(present);
 *
 *
 *     // 3. 扩展的 ifPresent 方法
 *     OptionalBean.ofNullable(axin)
 *             .getBean(User::getSchool)
 *             .getBean(User.School::getAdress)
 *             .ifPresent(adress -> System.out.println(String.format("地址存在:%s", adress)));
 *
 *     // 4. 扩展的 orElse
 *     String value2 = OptionalBean.ofNullable(axin)
 *             .getBean(User::getSchool)
 *             .getBean(User.School::getAdress).orElse("家里蹲");
 *
 *     System.out.println(value2);
 *
 *     // 5. 扩展的 orElseThrow
 *     try {
 *         String value3 = OptionalBean.ofNullable(axin)
 *                 .getBean(User::getSchool)
 *                 .getBean(User.School::getAdress).orElseThrow(() -> new RuntimeException("空指针了"));
 *     } catch (Exception e) {
 *         System.out.println(e.getMessage());
 *     }
 *
 * </pre>
 *
 *
 * @author Axin
 * @since 2020-09-10
 * @link {https://www.hollischuang.com/archives/5605}
 * @description  主要参考 {@link java.util.Optional} 实现，再加上对链式调用的扩展就是上述的 #OptionalBean
 */
public final class OptionalBean<T> {

    private static final OptionalBean<?> EMPTY = new OptionalBean<>();

    //
    private final T value;

    private OptionalBean() {
        this.value = null;
    }

    /**
     * 空值会抛出空指针
     *
     * @param value 任意对象
     */
    private OptionalBean(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * 包装一个不能为空的 bean
     */
    public static <T> OptionalBean<T> of(T value) {
        return new OptionalBean<>(value);
    }

    /**
     * 包装一个可能为空的 bean
     */
    public static <T> OptionalBean<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * 取出具体的值
     */
    public T get() {
        return Objects.isNull(value) ? null : value;
    }

    /**
     * 取出一个可能为空的对象
     */
    public <R> OptionalBean<R> getBean(Function<? super T, ? extends R> fn) {
        return Objects.isNull(value) ? OptionalBean.empty() : OptionalBean.ofNullable(fn.apply(value));
    }

    /**
     * 如果目标值为空 获取一个默认值
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    /**
     * 如果目标值为空 通过lambda表达式获取一个值
     */
    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    /**
     * 如果目标值为空 抛出一个异常
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 是否为空
     */
    public boolean isPresent() {
        return value != null && value != "";
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * 空值常量
     */
    public static<T> OptionalBean<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalBean<T> none = (OptionalBean<T>) EMPTY;
        return none;
    }

}
