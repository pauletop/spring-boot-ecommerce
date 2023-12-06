package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Account getAccount(String email) {
        Iterable<Account> accounts = accountRepository.findByEmail(email);
        if (accounts.iterator().hasNext()) {
            return accounts.iterator().next();
        }
        return null;
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account register(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        emailService.sendEmail(account.getEmail(), "Register Successfully!", "Welcome to our shop :) !");
        return accountRepository.save(account);


    }

    public boolean login(String email, String password) {
        Iterable<Account> accounts = accountRepository.findByEmail(email);
        if (accounts.iterator().hasNext()) {
            Account account = accounts.iterator().next();
            return passwordEncoder.matches(password, account.getPassword());
        }
        return false;
    }
    public Page<Account> searchAdmin(String name, int page){
        return accountRepository.findAllByEmailContainingIgnoreCase(name,PageRequest.of(page, 15));
    }
    public void delete(Long id){
        accountRepository.deleteById(id);
    }
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Iterable<Account> accounts = accountRepository.findByEmail(email);
        if (accounts.iterator().hasNext()) {
            Account account = accounts.iterator().next();
            if (passwordEncoder.matches(oldPassword, account.getPassword())) {
                account.setPassword(passwordEncoder.encode(newPassword));
                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }

    public void updateAddress(String email, String address) {
        Account account = getAccount(email);
        if (account != null) {
            account.setAddress(address);
            accountRepository.save(account);
        }
    }
    public long count(){
        return accountRepository.count();
    }
    public Page<Account> getAllOrderById(int page){
        Pageable pageable = PageRequest.of(page, 15);
        return accountRepository.findAllByOrderById(pageable);
    }

    public boolean addWishList(String email, Product product) {
        Account account = getAccount(email);
        if (account.getWishList().contains(product)) {
            return false;
        }
        account.addWishList(product);
        accountRepository.save(account);
        return true;
    }

    public boolean removeWishList(String email, Product product) {
        Account account = getAccount(email);
        if (account != null) {
            account.removeWishList(product);
            accountRepository.save(account);
            return true;
        }
        return false;
    }
}
