package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.Role;
import ke.or.karp.middleware.repositories.RoleRepository;
import ke.or.karp.middleware.repositories.UserRepository;
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
