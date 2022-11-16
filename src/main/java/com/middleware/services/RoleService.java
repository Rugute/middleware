package com.middleware.services;

import com.middleware.repositories.RoleRepository;
import com.middleware.repositories.UserRepository;
import com.middleware.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("roleService")
public class RoleService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    Date nowDate = new Date();
    @Autowired
    public RoleService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public List<Role> getAllRoles(){return  roleRepository.findAll();}
    public Role getRole(String rolename){
        return  roleRepository.findByRole(rolename);
    }
    public Role saveRole(Role role) {
       return roleRepository.save(role);
    }
}
