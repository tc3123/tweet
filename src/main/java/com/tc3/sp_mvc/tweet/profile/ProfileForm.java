package com.tc3.sp_mvc.tweet.profile;


import com.tc3.sp_mvc.tweet.date.PastLocalDate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 匹配 Web 表单中的域并描述校验规则
 */
public class ProfileForm {
    @Size(min = 2,message = "姓名不能少于2个字符")
    private String twitterHandle;

    @Email(message = "邮箱格式不正确")
    @NotEmpty(message="邮箱不能为空")
    private String email;

    @PastLocalDate
//    @Past(message = "日期不正确")
    @NotNull(message="日期不能为空")
    private LocalDate birthDate;
    @NotEmpty(message="tastes不能为空")
    private List<String> tastes = new ArrayList<>();


    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getTastes() {
        return tastes;
    }

    public void setTastes(List<String> tastes) {
        this.tastes = tastes;
    }

    @Override
    public String toString() {
        return "ProfileForm{" +
                "twitterHandle='" + twitterHandle + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
//                ", tastes=" + tastes +
                '}';
    }
}