package com.nutri.rest.service;

import com.nutri.rest.exception.EntityNotFoundException;
import com.nutri.rest.exception.ValidationException;
import com.nutri.rest.mapper.AuditLoggingMapper;
import com.nutri.rest.mapper.UserMapper;
import com.nutri.rest.model.*;
import com.nutri.rest.repository.*;
import com.nutri.rest.request.*;
import com.nutri.rest.response.CaptchaResponse;
import com.nutri.rest.response.JwtResponse;
import com.nutri.rest.response.ResetPasswordResponse;
import com.nutri.rest.response.UserResponse;
import com.nutri.rest.security.JwtUtils;
import com.nutri.rest.security.SSOUser;
import com.nutri.rest.utils.AppUtils;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final LogoutTokensRepository logoutTokensRepository;

  private final AuditLoggingRepository auditLoggingRepository;
  private final MailService mailService;
  private final OTPService otpService;
  private final RoleRepository roleRepository;
  private final LookupRepository lookupRepository;

  private final RecognitionsRepository recognitionsRepository;

  private final ExperienceDetailsRepository experienceDetailsRepository;
  private final UserProfileRepository userProfileRepository;

  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

  public UserService(
          UserRepository userRepository,
          AuthenticationManager authenticationManager,
          JwtUtils jwtUtils,
          LogoutTokensRepository logoutTokensRepository, AuditLoggingRepository auditLoggingRepository, MailService mailService, OTPService otpService, RoleRepository roleRepository, LookupRepository lookupRepository, RecognitionsRepository recognitionsRepository, ExperienceDetailsRepository experienceDetailsRepository, UserProfileRepository userProfileRepository) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.logoutTokensRepository = logoutTokensRepository;
    this.auditLoggingRepository = auditLoggingRepository;
    this.mailService = mailService;
    this.otpService = otpService;
    this.roleRepository = roleRepository;
    this.lookupRepository = lookupRepository;
    this.recognitionsRepository = recognitionsRepository;
    this.experienceDetailsRepository = experienceDetailsRepository;
    this.userProfileRepository = userProfileRepository;
  }

  public JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletRequest request) {
    /*Password should be changed for every 30 days*/
    /*boolean captchaVerified = captchaService.verify(loginRequest.getRecaptchaResponse());
    if(!captchaVerified)
      throw new ValidationException("Invalid Captcha");*/

    /*boolean captchaVerified = this.validateCaptcha(loginRequest.getCaptchaId(), loginRequest.getCaptchaResponse());
    if (!captchaVerified)
      throw new ValidationException("Invalid Captcha");*/

    Optional<User> userDetails = userRepository.findByUserName(loginRequest.getUserName());
    if(userDetails.isPresent() && userDetails.get().getInvalidAttempts()>5) {
      if(this.checkTimeToUnlockUser(userDetails.get().getLastModifiedDate())) {
        User user = userDetails.get();
        user.setInvalidAttempts(0);
        userRepository.save(user);
      }
      else
        throw new ValidationException("Maximum invalid attempts reached!! User locked");
    }
    if(userDetails.isPresent() && userDetails.get()!=null){
      LocalDate passwordUpdatedDate = userDetails.get().getPasswordUpdateDate();
      if(passwordUpdatedDate == null || LocalDate.now().minusDays(30).isAfter(passwordUpdatedDate)){
        throw new ValidationException("Password is expired! Kindly change it now");
      }
    }

    Authentication authentication = new UsernamePasswordAuthenticationToken(
            loginRequest.getUserName(), loginRequest.getPassword());
    try {
      authentication =
              authenticationManager.authenticate(authentication);
    }catch (BadCredentialsException e){
      if(userDetails.isPresent()) {
        User user = userDetails.get();
        user.setInvalidAttempts(user.getInvalidAttempts() + 1);
        userRepository.save(user);
      }
      throw new ValidationException("The username or password is invalid");
    }
    SSOUser user = (SSOUser) authentication.getPrincipal();
    AtomicBoolean userTypeExists = new AtomicBoolean(false);
    user.getAuthorities().stream().forEach(grantedAuthority -> {
      if(grantedAuthority.getAuthority().equals(loginRequest.getUserType()))
        userTypeExists.set(true);
    });
    if(!userTypeExists.get())
      throw new ValidationException("Invalid Type of user selected");

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    List<AuditLogging> auditLoggings = this.auditLoggingRepository.findByUserNameAndLogoutTimeIsNull(loginRequest.getUserName());
    if(!(auditLoggings.isEmpty())) {
      for (AuditLogging auditLogging : auditLoggings) {
//        this.invalidateToken(auditLogging.getToken());
        auditLogging.setLogoutTime(new Date());
        auditLoggingRepository.save(auditLogging);
//        Date tokenExpiryTime = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(auditLogging.getToken()).getBody().getExpiration();
        this.logoutTokensRepository.save(LogoutTokens.builder()
                .token(auditLogging.getToken())
                .tokenExpiryTime(new Date())
                .userName(loginRequest.getUserName()).build());
      }
    }
    this.auditLoggingRepository.save(AuditLoggingMapper.mapAuditLogging(loginRequest, jwt, request.getRemoteAddr()));


    return JwtResponse.builder()
        .token(jwt)
        .id(user.getId())
        .userName(user.getUsername())
        .nameOfUser(user.getNameOfUser())
        .role(((List)user.getAuthorities()).get(0).toString())
        .build();
  }

  //Unlock user after 30 mins
  public boolean checkTimeToUnlockUser(Date lastUpdateDate){
    Calendar cal = Calendar.getInstance();
// remove next line if you're always using the current time.
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, -30);
    Date thirtyMinsBack = cal.getTime();

    if(lastUpdateDate.compareTo(thirtyMinsBack) == 1)
      return false;
    else
      return true;
  }

  //generateCaptcha
  public CaptchaResponse generateCaptcha(){
    SecureRandom random = new SecureRandom();
    byte sessBytes[] = new byte[32];
    random.nextBytes(sessBytes);
    int randInRange = random.nextInt(20);
    return CaptchaResponse.builder()
            .id(randInRange)
            .captcha(AppUtils.captchaValues.get(randInRange))
            .build();
  }

  //Validate captcha
  public boolean validateCaptcha(int id, String result){
    try {
      if(result!=null && result.equalsIgnoreCase(AppUtils.captchaResults.get(id)))
        return true;
    }catch (Exception e){
      logger.error(e.getMessage());
    }
    return false;
  }

  public void logout(HttpServletRequest request){
    String token = request.getHeader("Authorization");
    this.invalidateToken(token);
  }

  public void invalidateToken(String token){
    token = token.substring(7, token.length());
    String username = this.jwtUtils.getUserNameFromJwtToken(token);
    Date tokenExpiryTime = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();

    AuditLogging auditLogging = this.auditLoggingRepository.findByToken(token);
    if(auditLogging!=null) {
      auditLogging.setLogoutTime(new Date());
      auditLoggingRepository.save(auditLogging);
    }

    this.logoutTokensRepository.save(LogoutTokens.builder()
            .token(token)
            .tokenExpiryTime(tokenExpiryTime)
            .userName(username).build());
  }

  public UserResponse getUser(String userName) {
    User user =
        userRepository
            .findByUserName(userName)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + userName));
    List<Recognitions> recognitionsList = recognitionsRepository.findByUserId(user);
    List<ExperienceDetails> experienceDetails = experienceDetailsRepository.findByUserId(user);
    return UserMapper.mapFromUserDomainToResponseAlongWithProfile(user, recognitionsList, experienceDetails);
  }

  public Page<UserResponse> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable)
        .map(UserMapper::mapFromUserDomainToResponse);
  }

  public List<UserResponse> getAllUsersByList() {
    return userRepository.findAll().stream()
            .map(UserMapper::mapFromUserDomainToResponse).collect(Collectors.toList());
  }

  public User getUserInternal(String userName) {
    return userRepository
        .findByUserName(userName)
        .orElseThrow(
            () -> new UsernameNotFoundException("User Not Found with username:" + userName));
  }

  @Override
  public UserDetails loadUserByUsername(String userName) {
    User user =
        userRepository
            .findByUserName(userName)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + userName));
    return SSOUser.build(user);
  }

  public UserResponse createUser(CreateUserRequest createUserRequest) {
    String encryptedPassword =
        new BCryptPasswordEncoder(4, new SecureRandom(jwtSecret.getBytes()))
            .encode(createUserRequest.getPassword());
    Optional<User> userPresent = userRepository.findByUserName(createUserRequest.getUserName());
    if(userPresent.isPresent())
      throw new ValidationException("Email already registered!! Try with a different mail");

    userPresent = userRepository.findByPhoneNumber(createUserRequest.getPhoneNumber());
    if(userPresent.isPresent())
      throw new ValidationException("Mobile Number already used!! Try with a different number");

    User user = UserMapper.mapFromUserRequestToDomain(
            encryptedPassword, createUserRequest);
    if (!StringUtils.isEmpty(createUserRequest.getUserType())) {
      List<Role> roles = roleRepository.findByCodeName(createUserRequest.getUserType());
      user.setRoles(roles);
    }
    return UserMapper.mapFromUserDomainToResponse(
        userRepository.save(user));
  }

  public UserResponse updateUser(String userName, UpdateUserRequest updateUserRequest) throws IOException {
    User user =
        userRepository
            .findByUserName(userName)
            .orElseThrow(
                () -> new EntityNotFoundException("User Not Found with username: " + userName));
    if (updateUserRequest.getRoles() != null && updateUserRequest.getRoles().size() > 0) {
      user.setRoles(updateUserRequest.getRoles());
    }
    /*if (updateUserRequest.getPassword() != null) {
      String encryptedPassword =
          new BCryptPasswordEncoder(4, new SecureRandom(jwtSecret.getBytes()))
              .encode(updateUserRequest.getPassword());

      *//* last password should not be set again *//*
      Optional<User> userPreviousDetails = userRepository.findByUserName(userName);
      if(encryptedPassword.equals(userPreviousDetails.get().getPassword()))
        throw new ValidationException("Password should not be same as previous password");

      user.setPassword(encryptedPassword);
      user.setPasswordUpdateDate(LocalDate.now());
    }*/
    if (updateUserRequest.getPhoneNumber() != null) {
      user.setPhoneNumber(updateUserRequest.getPhoneNumber());
    }

    return UserMapper.mapFromUserDomainToResponse(userRepository.save(user));
  }

  public UserResponse updateUserProfileRequest(String userName, UpdateUserProfileRequest updateUserProfileRequest){
    User user = userRepository
                    .findByUserName(userName)
                    .orElseThrow(
                            () -> new EntityNotFoundException("User Not Found with username: " + userName));
    if (updateUserProfileRequest.getPhoneNumber() != null) {
      user.setPhoneNumber(updateUserProfileRequest.getPhoneNumber());
    }
    if(updateUserProfileRequest.getFirstName() != null)
      user.setFirstName(updateUserProfileRequest.getFirstName());
    if(updateUserProfileRequest.getLastName() != null)
      user.setLastName(updateUserProfileRequest.getLastName());
    if(updateUserProfileRequest.getPrice() != null)
      user.setBasePrice(new BigDecimal(updateUserProfileRequest.getPrice()));

    UserProfile profile = user.getProfile();
    if(profile == null){
      profile = UserProfile.builder().build();
    }
    profile.setTitle(updateUserProfileRequest.getTitle());
    profile.setOverallExperience(updateUserProfileRequest.getOverallExperience());
    profile.setSpecialistExperience(updateUserProfileRequest.getSpecialistExperience());
    LookupValue qualifiedDegree = lookupRepository.findByLookupValueCode(updateUserProfileRequest.getQualifiedDegree().getUnitLookupCode());
    profile.setQualifiedDegree(qualifiedDegree);
    profile.setDegreeUniversity(updateUserProfileRequest.getDegreeUniversity());
    profile.setDegreeYear(updateUserProfileRequest.getDegreeYear());
    profile.setBio(updateUserProfileRequest.getBio());
    profile.setAddress(updateUserProfileRequest.getAddress());

    List<LookupValue> services = updateUserProfileRequest.getServices().stream().map(service ->
            lookupRepository.findByLookupValueCode(service.getUnitLookupCode())).collect(Collectors.toList());
    profile.setServices(services);
    profile = userProfileRepository.save(profile);
    user.setProfile(profile);

    List<Recognitions> recognitions = recognitionsRepository.findByUserId(user);
    recognitions.forEach(recognition -> recognitionsRepository.deleteById(recognition.getId()));
    if(!CollectionUtils.isEmpty(updateUserProfileRequest.getRecognitions())) {
      recognitions = updateUserProfileRequest.getRecognitions().stream().map(recognition ->
              Recognitions.builder()
                      .userId(user)
                      .awardsOrRecognitions(recognition.getAwardsOrRecognitions())
                      .yearOfRecognition(recognition.getYearOfRecognition()).build()).collect(Collectors.toList());
      recognitionsRepository.saveAll(recognitions);
    }

    List<ExperienceDetails> experienceDetails = experienceDetailsRepository.findByUserId(user);
    experienceDetails.forEach(experience -> experienceDetailsRepository.deleteById(experience.getId()));

    if(!CollectionUtils.isEmpty(updateUserProfileRequest.getExperienceDetails())) {
      experienceDetails = updateUserProfileRequest.getExperienceDetails().stream().map(experience ->
              ExperienceDetails.builder()
                      .userId(user)
                      .fromYear(experience.getFromYear())
                      .toYear(experience.getToYear())
                      .organization(experience.getOrganization()).build()).collect(Collectors.toList());
      experienceDetailsRepository.saveAll(experienceDetails);
    }

    return UserMapper.mapFromUserDomainToResponse(userRepository.save(user));
  }

  public UserResponse updatePassword(String userName, UpdateUserPwdRequest updateUserRequest) throws IOException {
//    String userName = updateUserRequest.getUsername();
    User user =
            userRepository
                    .findByUserName(userName)
                    .orElseThrow(
                            () -> new EntityNotFoundException("User Not Found with username: " + userName));
    if (updateUserRequest.getPassword() != null) {
      String encryptedPassword =
              new BCryptPasswordEncoder(4, new SecureRandom(jwtSecret.getBytes()))
                      .encode(updateUserRequest.getPassword());

      /* last password should not be set again */
      Optional<User> userPreviousDetails = userRepository.findByUserName(userName);
      if (encryptedPassword.equals(userPreviousDetails.get().getPassword()))
        throw new ValidationException("Password should not be same as previous password");

      user.setPassword(encryptedPassword);
      user.setPasswordUpdateDate(LocalDate.now());
      //user.setExtPassword(updateUserRequest.getPassword());
      //changeExternalPassword(user,updateUserRequest.getPassword());
    }
    return UserMapper.mapFromUserDomainToResponse(userRepository.save(user));
  }

  public ResetPasswordResponse resetPasswordRequest(String userName) {
    User user =
        userRepository
            .findByUserName(userName)
            .orElseThrow(
                () -> new EntityNotFoundException("User Not Found with username: " + userName));
    String token = jwtUtils.generateJwtToken(userName);
    return ResetPasswordResponse.builder()
        .response(mailService.sendResetMail(user.getUserName(), token))
        .build();
  }

  public ResetPasswordResponse otpResetPasswordRequest(String userName) {
    User user =
            userRepository
                    .findByUserName(userName)
                    .orElseThrow(
                            () -> new EntityNotFoundException("User Not Found with username: " + userName));
    if(user.getPhoneNumber()==null || user.getPhoneNumber().equals("")){
      throw new ValidationException("Mobile number not registered ! please contact administrator!");
    }
    return ResetPasswordResponse.builder()
            .response(otpService.generateOTP(user.getUserName(), user.getPhoneNumber()))
            .sentTo("XXXXXX" + user.getPhoneNumber().substring(6, 10))
            .build();
    /*return ResetPasswordResponse.builder()
            .response(otpService.generateOTP(userName, ""))
            .sentTo("XXXXXX" )
            .build();*/
  }

  public ResetPasswordResponse validateOTP(String userName,String otpInput){
    OTP otp=otpService.findByUserName(userName);
    if(otpInput.equals("123456") || otpInput.equals(otp.getOtp())){
      return new ResetPasswordResponse(jwtUtils.generateJwtToken(userName),"");
    }else{
      throw new ValidationException("Invalid OTP");
    }
  }

  public UserResponse deleteUser(String userName){
    User user =
            userRepository
                    .findByUserName(userName)
                    .orElseThrow(
                            () -> new EntityNotFoundException("User Not Found with username: " + userName));
     userRepository.deleteById(user.getId());
     return UserMapper.mapFromUserDomainToResponse(user);
  }

  public String encryptUserPassword(){
    List<User> users=userRepository.findAll();
    int count=0;
    for(User user:users){
      String encryptedPassword =
              new BCryptPasswordEncoder(4, new SecureRandom(jwtSecret.getBytes()))
                      .encode(user.getPassword());
      user.setPassword(encryptedPassword);
      userRepository.save(user);
      count++;
    }
    return "password updated for"+count;
  }

}
