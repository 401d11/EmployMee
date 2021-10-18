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

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/")
    public String getHomePage() {return "index.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {return "login.html";}

    @GetMapping("/signup/candidate")
    public String getSignupCandidatePage() {return "signupCandidate.html";}

    @GetMapping("/signup/business")
    public String getSignupBusinessPage() {return "signupBusiness.html";}

//    @GetMapping("/discover")
//    public String getDiscoverPage() {return "discover.html";}
//
//    @GetMapping("/discover/{candidateID}")
//    public String getCandidateProfile() {return "discoverCandidate.html";}
//
//    @PostMapping("/recruit/{candidateID}")
//    public String recruitCandidate(){return "recruitForm.html"}
//
//    @GetMapping("/account")
//    public String getAccountPage(Model m, Principal p, @PathVariable String username){
//
//    }
//

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