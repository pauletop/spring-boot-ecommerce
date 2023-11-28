package vn.com.ecommerce.springcommerce.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.service.AccountService;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping({"/", ""})
    String index(){
//        return "redirect:/account/login";
        return "account";
    }


    @GetMapping("/login")
    String login(HttpServletRequest request, HttpServletResponse response, Model model){
        Cookie[] cookies = request.getCookies();
        boolean isRemembered = false;
        String email, password;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    isRemembered = true;
                    email = cookie.getValue();
                    model.addAttribute("email", email);
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                    model.addAttribute("password", password);
                }
            }
        }
        if (isRemembered) {
            model.addAttribute("remember", "checked");
        } else {
            model.addAttribute("remember", "");
            model.addAttribute("email", "");
            model.addAttribute("password", "");
        }
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            request.getSession().removeAttribute("error");
        }
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/register")
    String signup(HttpServletRequest request, Model model){
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            request.getSession().removeAttribute("error");
        }
        model.addAttribute("error", error);
        return "register";
    }

    @PostMapping("/register")
    RedirectView register(
            @RequestParam("name") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("pass") String password,
            @RequestParam("repass") String rePassword,
            @RequestParam(value = "agree-term", required = false) boolean agreeTerm,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String error = validateRegister(fullName, email, phone, password, rePassword, agreeTerm);
        if (error != null) {
            request.getSession().setAttribute("error", error);
            return new RedirectView("/account/register");
        }
        Account account = new Account();
        account.setFullname(fullName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setPassword(password);
        accountService.register(account);
        return new RedirectView("/account/login");
    }


    @PostMapping("/login")
    RedirectView login(
            @RequestParam("email") String email,
            @RequestParam("pass") String password,
            @RequestParam(value = "remember-me", required = false) boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String error = validateLogin(email, password);
        if (error != null) {
            request.getSession().setAttribute("error", error);
            return new RedirectView("/account/login");
        }
        Account account = accountService.getAccount(email);
        request.getSession().setAttribute("accountId", account.getIdBase64());
        if (rememberMe) {
            Cookie emailCookie = new Cookie("email", email);
            Cookie passwordCookie = new Cookie("password", password);
            emailCookie.setMaxAge(60 * 60 * 24 * 30);
            passwordCookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(emailCookie);
            response.addCookie(passwordCookie);
        }
        return new RedirectView("/");
    }
    /*---------------------------------*\
    |          UTILITY FUNCTIONS        |
    \*---------------------------------*/
    private boolean isEmailValid(String email){
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$");
    }
    private boolean isPhoneValid(String phone, String countryCode){
        if (countryCode.equals("vn")) {
            return phone.matches("^(84|0[3|5|7|8|9])+([0-9]{8})$");
        }
        if (countryCode.equals("us")) {
            return phone.matches("^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$");
        }
        if (countryCode.equals("uk")) {
            return phone.matches("^(\\+?44|0)7\\d{9}$");
        }
        return phone.matches("^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$" +
                "|^\\(?([0-9]{2})\\)?[-.\\s]?([0-9]{4})[-.\\s]?([0-9]{4})$" +
                "|^\\(?([0-9]{4})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{3})$");
    }
    private String validateRegister(String fullName, String email, String phone, String password, String confirmPassword, boolean agreeTerm) {
        if (fullName == null || fullName.isEmpty()) {
            return "Please complete all required fields!";
        }
        if (password == null || password.isEmpty()) {
            return "Please complete all required fields!";
        }
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return "Please complete all required fields!";
        }
        if (email == null || email.isEmpty()) {
            return "Please complete all required fields!";
        }
        if (!password.equals(confirmPassword)) {
            return "Password and confirm password must be the same!";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters!";
        }
        if (!isEmailValid(email)) {
            return "Invalid email!";
        }
        if (!isPhoneValid(phone, "vn")) {
            return "Invalid phone number!";
        }
        if (!agreeTerm) {
            return "Please agree to the terms and conditions!";
        }
        if (accountService.getAccount(email) != null) {
            return "Email already exists!";
        }
        return null;
    }

    private String validateLogin(String email, String password) {
        if (email == null || email.isEmpty()) {
            return "Please complete all required fields!";
        }
        if (password == null || password.isEmpty()) {
            return "Please complete all required fields!";
        }
        if (!isEmailValid(email)) {
            return "Invalid email!";
        }
        if (!accountService.login(email, password)) {
            return "Email or password is incorrect!";
        }
        return null;
    }


    /*---------------------------------*\
    |   Đăng nhập bằng tài khoản Google  |
    \*---------------------------------*/
//    TODO




}
