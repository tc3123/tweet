package com.tc3.sp_mvc.tweet.profile;

import com.tc3.sp_mvc.tweet.date.USLocalDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
public class ProfileController {
    private UserProfileSession userProfileSession;

    @Autowired
    public ProfileController(UserProfileSession userProfileSession) {
        this.userProfileSession = userProfileSession;
    }

    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm) {
        return "profile/profilePage";
    }

    //
    @RequestMapping(value = "/profile", params={"save"},method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/profilePage";//保留了表单信息？
        }
        userProfileSession.saveForm(profileForm);//保存
        System.out.println("save ok" + profileForm);
        return "redirect:/search/mixed;keywords=" + String.join(",", profileForm.getTastes());
    }

    @RequestMapping(value = "/profile", params={"addTaste"},method = RequestMethod.POST)
    public String addRow(ProfileForm profileForm) {
        profileForm.getTastes().add(null);
        return "profile/profilePage";
    }

    @RequestMapping(value="/profile", params={"removeTaste"},method = RequestMethod.POST)
    public String removeRow(ProfileForm profileForm,HttpServletRequest req){
        Integer rowId = Integer.valueOf(req.getParameter("removeTaste"));
        profileForm.getTastes().remove(rowId.intValue());
        return "profile/profilePage";
    }

    //暴露一个属性给 Web 页面
    @ModelAttribute("dateFormat")
    public String localeFormat(Locale locale) {
        return USLocalDateFormatter.getPattern(locale);
    }
}