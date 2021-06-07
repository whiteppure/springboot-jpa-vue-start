package com.github.springboot.template.utils;

import org.junit.Test;

public class StringTest {

    @Test
    public void context() {
        // 使用 %% 对'%'进行转义
        String like = String.format("%%%s%%", "123");
        String likeLeft = String.format("%%%s", "123");
        String likeRight = String.format("%s%%", "123");

        // print
        System.out.println(like);
        System.out.println();
        System.out.println(likeLeft);
        System.out.println();
        System.out.println(likeRight);
    }
}
