package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.Role;
import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.entity.VerificationToken;
import net.stevencai.stevenweb.entity.VerificationTokenType;
import net.stevencai.stevenweb.exception.EmailAlreadyExistException;
import net.stevencai.stevenweb.exception.UsernameAlreadyExistException;
import net.stevencai.stevenweb.frontendResource.UserResource;
import net.stevencai.stevenweb.repository.AccountRepository;
import net.stevencai.stevenweb.repository.RoleRepository;
import net.stevencai.stevenweb.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private ApplicationEventPublisher eventPublisher;


    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setVerificationTokenRepository(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        Role role =roleRepository.findRoleById(1);
        user.setAuthorities(Collections.singletonList(role));
        return accountRepository.save(user);
    }

    @Override
    public User saveVerifiedUser(User user, VerificationToken token) {
        verificationTokenRepository.delete(token);
        return saveUser(user);
    }

    @Override
    public User createUser(UserResource userResource)throws EmailAlreadyExistException, UsernameAlreadyExistException {
        if(isEmailExist(userResource.getEmail())){
            throw new EmailAlreadyExistException("There is an account associated with this email: "+userResource.getEmail());
        }
        if(isUsernameExist(userResource.getUsername())){
            throw new UsernameAlreadyExistException("This username is already token.");
        }
        User user = buildUser(userResource);

        return saveUser(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return accountRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByUsername(String username){
        return accountRepository.findUserByUsername(username);
    }

    @Override
    public User loadUserByUsername(String username) {
        User user =accountRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Cannot find this user");
        }
        return user;
    }

    @Override
    public VerificationToken createVerificationTokenForUser(User user, String token, VerificationTokenType type) {
        VerificationToken verificationToken = new VerificationToken(token,user);
        verificationToken.setType(type);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public VerificationToken findVerificationTokenByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void updateUserPassword(User user, String password) {
        user.setPassword(encryptPassword(password));
        saveUser(user);
    }

    @Override
    public void removeVerificationToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    @Transactional
    @Override
    public void removeAllVerificationTokenForUser(User user) {
        verificationTokenRepository.deleteAllByUser(user);
    }

    @Override
    public VerificationToken findVerificationTokenBefore(User user, LocalDateTime dateTime) {
        return verificationTokenRepository.findByUserAndCreateDateTimeAfter(user, dateTime);
    }

    private boolean isEmailExist(String email){
        return findUserByEmail(email) != null;
    }
    private boolean isUsernameExist(String username){
        try {
            loadUserByUsername(username);
        }
        catch(UsernameNotFoundException ex){
            return false;
        }
        return true;
    }

    private boolean isUserResourceExist(UserResource userResource){
        return isEmailExist(userResource.getEmail())
                || isUsernameExist(userResource.getUsername());
    }
    /**
     * build user object from userResource object. password is encoded in ByCrypt.
     * @param userResource original user resource obj.
     * @return final user object.
     */
    private User buildUser(UserResource userResource){
        User user = new User();
        user.setUsername(userResource.getUsername());
        user.setPassword(encryptPassword(userResource.getConfirmPassword()));
        user.setEmail(userResource.getEmail());
        return user;
    }
    private String encryptPassword(String rawPw){
        return passwordEncoder.encode(rawPw);
    }
}
