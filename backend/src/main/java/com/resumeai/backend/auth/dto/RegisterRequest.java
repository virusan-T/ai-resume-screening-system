package com.resumeai.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 150, message = "Name must contain 3-150 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        @Size(max = 150)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100,
                message = "Password must contain at least 8 characters")
        String password,

        @NotBlank(message = "Role is required")
        @Pattern(
                regexp = "CANDIDATE|RECRUITER",
                message = "Role must be CANDIDATE or RECRUITER"
        )
        String role

) {
}