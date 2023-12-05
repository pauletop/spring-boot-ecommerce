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
    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
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
        try{
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            return accountRepository.save(account);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public boolean login(String email, String password) {
        Iterable<Account> accounts = accountRepository.findByEmail(email);
        if (accounts.iterator().hasNext()) {
            Account account = accounts.iterator().next();
            return passwordEncoder.matches(password, account.getPassword());
        }
        return false;
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
}
