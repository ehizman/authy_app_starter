 package com.twilio.authy_app_starter.user_management;

 import com.twilio.authy_app_starter.twilio.VerificationService;
 import jakarta.validation.Valid;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.ModelAttribute;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestParam;

 import java.util.Map;

 @Controller
 public class UserController {
     private final Logger log = LoggerFactory.getLogger(UserController.class);

     private final UserService userService;
     private final VerificationService verificationService;

     public UserController(UserService userService, VerificationService verificationService) {
         this.userService = userService;
         this.verificationService = verificationService;
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

     @GetMapping("/setup-2fa")
     public String show2FASetupPage(Model model) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         String username = userDetails.getUsername();
         try {
             Map<String, Object> binding = verificationService.createFactor(username);
             model.addAttribute("uri", binding.get("uri"));
         } catch (Exception e) {
             // if an error occurred while creating factor
             log.error("Error while creating factor as a result of the following exception: {}", e.getLocalizedMessage());
             model.addAttribute("error", "Error while creating factor");
         }
         return "setup-2fa";
     }

     @GetMapping("/verify-factor")
     public String displayInput2faCodePageTOVerifyFactor(Model model){
         model.addAttribute("verify_factor", true);
         return "verify_code";
     }

     @PostMapping("/verify-factor")
     public String verifyFactor(@RequestParam("code") String authCode, Model model){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         String username = userDetails.getUsername();
         boolean isVerified = verificationService.verifyFactor(username, authCode);
         if (isVerified){
             model.addAttribute("username", username);
             return "redirect:/dashboard";
         } else{
             // if the user passed an invalid verification code
             model.addAttribute("verifyError", "Invalid verification code");
             model.addAttribute("verify_factor", true);
             return "verify_code";
         }
     }

     @GetMapping("/verify-token")
     public String displayInput2faCodePageToVerifyToken(Model model){
         model.addAttribute("verify_token", true);
         return "verify_code";
     }

     @PostMapping("/verify-token")
     public String verifyToken(@RequestParam("code") String authCode, Model model){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         String username = userDetails.getUsername();
         boolean isVerified = verificationService.verifyToken(username, authCode);
         if (isVerified){
             model.addAttribute("username", username);
             return "redirect:/dashboard";
         } else{
             model.addAttribute("verifyError", "Invalid verification code");
             model.addAttribute("verify_token", true);
             return "verify_code";
         }
     }
 }
