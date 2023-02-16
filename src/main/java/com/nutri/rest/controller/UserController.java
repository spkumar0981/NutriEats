package com.nutri.rest.controller;

import com.nutri.rest.request.*;
import com.nutri.rest.response.*;
import com.nutri.rest.service.CurrentUserService;
import com.nutri.rest.service.SubscriptionService;
import com.nutri.rest.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/*@CrossOrigin(origins = "*", maxAge = 3600)*/
@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User Controller")
public class UserController {

  private final UserService userService;

  private final SubscriptionService subscriptionService;

  public UserController(UserService userService, SubscriptionService subscriptionService) {
    this.userService = userService;
    this.subscriptionService = subscriptionService;
  }


  @GetMapping("/username/{username}")
  @ApiOperation(value = "Get user by user name for Admin")
  public UserResponse getUserByUserName(@PathVariable String username){
    return userService.getUser(username);
  }

  @GetMapping("/details")
  @ApiOperation(value = "Get logged user details")
  public UserResponse getUserByToken(){
    String loggedUser = CurrentUserService.getLoggedUserName();
    return userService.getUser(loggedUser);
  }

  @GetMapping
  @ApiOperation(value = "Get all users")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Page<UserResponse> getAllUsers(Pageable pageable){
    return userService.getAllUsers(pageable);
  }

  @GetMapping("/allUsersByList")
  @ApiOperation(value = "Get all users")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserResponse> getAllUsersByList(){
    return userService.getAllUsersByList();
  }



  @PostMapping("/signIn")
  @ApiOperation(value = "Sign In")
  public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
    return userService.authenticateUser(loginRequest, request);
  }

  @PostMapping("/signOut")
  @ApiOperation(value = "Sign Out")
  public void logoutUser(HttpServletRequest request) {
    userService.logout(request);
    return;
  }

  @PostMapping("/createUser")
  @ApiOperation(value = "Create User")
  public UserResponse create(@Validated @RequestBody CreateUserRequest createUserRequest) {
    return userService.createUser(createUserRequest);
  }

  @PostMapping("/username/{username}")
  @ApiOperation(value = "Update user details")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserResponse adminUpdate(@Valid @RequestBody UpdateUserRequest updateUserRequest, @PathVariable String username) throws IOException {
    return userService.updateUser(username, updateUserRequest);
  }

  @PostMapping("/update")
  @ApiOperation(value = "Update user details")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserResponse updateProfile(@Valid @RequestBody UpdateUserRequest updateUserRequest) throws IOException {
    String loggedUser = CurrentUserService.getLoggedUserName();
    return userService.updateUser(loggedUser, updateUserRequest);
  }


  @PostMapping("/resetPassword")
  @ApiOperation(value = "Reset the password")
  public UserResponse updatePassword(@RequestBody UpdateUserPwdRequest updateUserRequest) throws IOException {
    String loggedUser = CurrentUserService.getLoggedUserName();
    return userService.updatePassword(loggedUser, updateUserRequest);
  }

  //Below is mail service
  @PostMapping("/resetPassword/request")
  @ApiOperation(value = "Password reset request")
  public ResetPasswordResponse resetPasswordRequest(ResetPasswordRequest resetPasswordRequest) {
    return userService.resetPasswordRequest(resetPasswordRequest.getUserName());
  }

  @PostMapping("/resetPassword/otp/request")
  @ApiOperation(value = "Password reset request")
  public ResetPasswordResponse otpResetPasswordRequest(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    return userService.otpResetPasswordRequest(resetPasswordRequest.getUserName());
  }

  @PostMapping("/resetPassword/otp/validate")
  @ApiOperation(value = "Password reset request")
  public ResetPasswordResponse otpResetPasswordValidate(@Valid @RequestBody ValidateOTPRequest validateOTPRequest) {
    return userService.validateOTP(validateOTPRequest.getUserName(),validateOTPRequest.getOtp());
  }

  @GetMapping("/encryptPassword")
  @ApiOperation(value = "Encrypt user password")
  public String encryptUserPassword(){
    return userService.encryptUserPassword();
  }

  @GetMapping("/captcha")
  @ApiOperation(value = "Generate Captcha")
  public CaptchaResponse generateCaptcha(){
    return userService.generateCaptcha();
  }

  @GetMapping("/dietitians")
  @ApiOperation(value = "Get all users")
  public Page<DietitansListResponse> getAllUsersByRole(Pageable pageable){
    return subscriptionService.getAllDietitians(pageable);
  }

  @PostMapping("/hireDietitian")
  @ApiOperation(value = "Hire a Dietitian")
  public ResponseEntity<Object> hireDietitian(@Valid @RequestBody DietitianRequest dietitianRequest){
    subscriptionService.hireDietitian(dietitianRequest);
    return new ResponseEntity<>("Dietitian hired successfully", HttpStatus.OK);
  }
}
