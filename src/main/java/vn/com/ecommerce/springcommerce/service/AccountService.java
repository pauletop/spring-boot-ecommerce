package vn.com.ecommerce.springcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.ecommerce.springcommerce.domain.Account;
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

    public String register(Account account) {
        if (accountRepository.findByEmail(account.getEmail()).iterator().hasNext()) {
            return "Email already exists";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return null;
    }

    public boolean login(String email, String password) {
        Iterable<Account> accounts = accountRepository.findByEmail(email);
        if (accounts.iterator().hasNext()) {
            Account account = accounts.iterator().next();
            return passwordEncoder.matches(password, account.getPassword());
        }
        return false;
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

}
