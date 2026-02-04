package com.app.service;

import org.slf4j.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.repository.RefreshTokenRepository;
import com.app.repository.UserRepository;

import lombok.*;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImplementation.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		
		log.debug("Loading user by username: {}", username);
		
		com.app.entity.UserDetails user = userRepository.findByUsername(username);
		
		if(user==null) {
			log.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
		}
		
		return new CustomUserDetails(user);
	}
	
	public com.app.entity.UserDetails checkIfUserAlreadyExist(UserInfoDto userInfoDto){
		return userRepository.findByUsername(userInfoDto.getUsername());
	}
	
	public Boolean signupUser(UserInfoDto userInfoDto){
		userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
		
		if (userRepository.findByUsername(userInfoDto.getUsername()) != null) {
            return false;
        }
		
		String userId = UUID.randomUUID().toString();
		com.app.entity.UserDetails user =
                new com.app.entity.UserDetails(
                        userId,
                        userInfoDto.getUsername(),
                        passwordEncoder.encode(userInfoDto.getPassword()),
                        new HashSet<>()
                );
		userRepository.save(user);
        return true;
	}
	
}
