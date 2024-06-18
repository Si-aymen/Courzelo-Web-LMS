package org.example.courzelo.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {
UserDetails loadUserByEmail(String email);

 boolean ValidUser(String email);
}
