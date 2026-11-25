package com.resumeai.backend.auth.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleTestController {

    @GetMapping("/admin/test")
    public String adminTest() {
        return "ADMIN access successful";
    }

    @GetMapping("/recruiter/test")
    public String recruiterTest() {
        return "RECRUITER access successful";
    }

    @GetMapping("/candidate/test")
    public String candidateTest() {
        return "CANDIDATE access successful";
    }
}