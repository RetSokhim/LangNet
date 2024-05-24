package org.example.langnet.service.serviceimpl;

import org.example.langnet.model.entity.Role;
import org.example.langnet.repository.RoleRepository;
import org.example.langnet.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.getAllRole();
    }
}
