package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.entity.User;
import com.wnsrich.reggie.service.UserService;
import com.wnsrich.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 发送短信
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        if(user.getPhone() != null) {
            String phone = user.getPhone();

            String code = ValidateCodeUtils.generateValidateCode4String(4);

            log.info("code =>> " + code);

            session.setAttribute(phone, code);
            return R.success("发送成功");
        }
        return R.error("发送失败");
    }

//    @PostMapping("/login")
//    public R<User> login(@RequestBody Map map,HttpSession session){
//        // 获取传过来的phone和code值
//        String phone = map.get("phone").toString();
//        String code = map.get("code").toString();
//
//        Object phone1 = session.getAttribute("phone");
//        if (phone1.equals(code)){
//            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper();
//            lqw.eq(User::getPhone,phone);
//            User one = userService.getOne(lqw);
//            if (one == null){
//                one = new User();
//                one.setPhone(phone);
//                userService.save(one);
//            }
//            session.setAttribute("user",one.getId());
//            return R.success(one);
//        }
//
//        return R.error("验证码错误");
//    }

    @PostMapping("/login")
    public R<User> login(@RequestBody User user,HttpSession session){
        // 获取传过来的phone和code值
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper();
        lqw.eq(User::getPhone,user.getPhone());
        User one = userService.getOne(lqw);
        if (one == null){
            one = new User();
            one.setPhone(user.getPhone());
            userService.save(one);
        }
        session.setAttribute("user",one.getId());
        return R.success(one);

    }
}
