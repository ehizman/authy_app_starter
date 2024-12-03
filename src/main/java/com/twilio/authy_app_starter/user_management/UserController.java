 package com.twilio.authy_app_starter.user_management;

 import jakarta.validation.Valid;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.ModelAttribute;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 @Controller
 public class UserController {
     private Logger log = LoggerFactory.getLogger(UserController.class);

     private UserService userService;
     private AuthenticationManager authenticationManager;

     public UserController(UserService userService, AuthenticationManager authenticationManager) {
         this.userService = userService;
         this.authenticationManager = authenticationManager;
     }

     @GetMapping("/signup")
     public String signup(){
         return "signup";
     }

     @PostMapping("/signup")
     public String signUp(@ModelAttribute  @Valid SignupForm signupForm, Model model) {
         try {
             userService.createNewUser(signupForm);
         } catch (Exception e) {
             model.addAttribute("loginError", "User already exists");
             return "login";
         }
         model.addAttribute("loginRequired", "Please login");
         return "login";
     }

     @GetMapping("/login")
     public String showLoginForm(
             @RequestParam(value = "logoutSuccess", defaultValue = "false", required = false) String logoutSuccess,
             @RequestParam(value = "loginRequired", defaultValue = "false", required = false) String loginRequired,
             @RequestParam(value = "loginError", defaultValue = "false", required = false) String loginError,
             Model model) {
         if (logoutSuccess.equalsIgnoreCase("true")){
             model.addAttribute("logoutSuccess", "You have been successfully logged out.");
         }
         if (loginRequired.equalsIgnoreCase("true")){
             log.info("Login Required");
             model.addAttribute("loginRequired", "Please login.");
         }
         if (loginError.equalsIgnoreCase("true")){
             model.addAttribute("loginError", "Invalid username or password");
         }
         return "login";
     }

     @GetMapping("/dashboard")
     public String showDashboard(Model model){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         String username = userDetails.getUsername();
         model.addAttribute("username", username);
         return "dashboard";
     }
 }
