package com.github.springboot.template.utils;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class BeanValidatorTest {

//    @Test
    public void setUp() {
        Account account = new Account();
        account.setAlias("kalakala");
        account.setUserName("wokalakala");
        account.setPassWord("密码");
        BeanValidator.ValidResult validResult = BeanValidator.validateBean(account);
        if(validResult.hasErrors()){
            String errors = validResult.getErrors();
            System.out.println(errors);
        }
    }

}

@Data
class Account{

    private String id;
    @NotNull
    @Length(max = 20)
    private String userName;

    @NotNull
    @Pattern(regexp = "[A-Z][a-z][0-9]")
    private String passWord;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;

    private String alias;

    @Max(10)
    @Min(1)
    private Integer level;
    private Integer vip;

}