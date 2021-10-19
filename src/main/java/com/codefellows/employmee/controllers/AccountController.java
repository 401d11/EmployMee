package com.codefellows.employmee.controllers;

import com.codefellows.employmee.models.Account;
import com.codefellows.employmee.models.Business;
import com.codefellows.employmee.models.Candidate;
import com.codefellows.employmee.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/")
    public String getHomePage() {return "index.html";}

    @GetMapping("/login")
    public String getLoginPage() {return "login.html";}

    @GetMapping("/signup")
    public String getSignupPage() {return "signup.html";}

    @GetMapping("/signup/candidate")
    public String getSignupCandidatePage() {return "signupCandidate.html";}

    @GetMapping("/signup/business")
    public String getSignupBusinessPage() {return "signupBusiness.html";}

    @GetMapping("/discover")
    public String getCandidateByLanguage(Model m, String language) {
        List<Account> filteredCandidates = accountRepository.findAll().stream().filter(s -> ! s.isBusiness()).collect(toList());
        List<Candidate> candidateList = new ArrayList<>();
        for(Account candidate: filteredCandidates){
            Candidate currentCandidate = (Candidate) candidate;
            if(currentCandidate.getLanguage().equals(language)){
                candidateList.add(currentCandidate);
            };
        }
        m.addAttribute("filteredCandidates", candidateList);
        return ("discover.html");
    }

    @GetMapping("/myprofile")
    public String getAccountPage(Model m, Principal p){
        if (p != null) {
            String username = p.getName();
            m.addAttribute("username", username);
        }
        Account currentAccount = accountRepository.findByUsername(p.getName());
        Set<Account> candidates = currentAccount.getCandidates();
        m.addAttribute("currentAccount", currentAccount);
        m.addAttribute("candidates", candidates);
        return "profile.html";
    }

    @PostMapping("/signup/business")
    public RedirectView createBusinessAccount(RedirectAttributes ra, String username,  String password, String email, String phone, String company) {
        Account existingUser = accountRepository.findByUsername(username);
        if(existingUser != null){
            ra.addFlashAttribute("errorMessage", "A profile for this Company has already been made.");
            return new RedirectView("/signup");
        }
        String hashedPassword = passwordEncoder.encode(password);
        Business newUser = new Business(username, hashedPassword, email, phone, true, company);
        accountRepository.save(newUser);
        authWithHttpServlet(username, password);
        return new RedirectView("/");
    }

    @PostMapping("/signup/candidate")
    public RedirectView createCandidateAccount(RedirectAttributes ra, String username, String firstname,  String lastname, String email, String language, String phone, String bio,  String password, int experience) {
        Account existingUser = accountRepository.findByUsername(username);
        if(existingUser != null){
            ra.addFlashAttribute("errorMessage", "That username is already taken please choose another name..");
            return new RedirectView("/signup");
        }
        String hashedPassword = passwordEncoder.encode(password);
        Candidate newUser = new Candidate(username, hashedPassword, email, phone, false, firstname, lastname, language,  bio, experience);
        accountRepository.save(newUser);
        authWithHttpServlet(username, password);
        return new RedirectView("/");
    }

    // Helper class
    public void authWithHttpServlet (String username, String password){
        try{
            request.login(username, password);
        } catch (ServletException se){
            // print out stack trace
            se.printStackTrace();
        }
    }
}
