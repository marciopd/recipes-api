package com.marciopd.recipesapi.configuration.db;

import com.marciopd.recipesapi.persistence.UserRepository;
import com.marciopd.recipesapi.persistence.entity.RoleEnum;
import com.marciopd.recipesapi.persistence.entity.UserEntity;
import com.marciopd.recipesapi.persistence.entity.UserRoleEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseDummyDataInitializer {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void populateDatabaseInitialDummyData() {
        if (isDatabaseEmpty()) {
            insertAdminUser();
            insertCustomer();
        }
    }

    private boolean isDatabaseEmpty() {
        return userRepository.count() == 0;
    }

    private void insertAdminUser() {
        UserEntity adminUser = UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("test123"))
                .build();
        UserRoleEntity adminRole = UserRoleEntity.builder().role(RoleEnum.ADMIN).user(adminUser).build();
        adminUser.setUserRoles(Set.of(adminRole));
        userRepository.save(adminUser);
    }

    private void insertCustomer() {
        UserEntity customerUser = UserEntity.builder()
                .username("customer")
                .password(passwordEncoder.encode("test123"))
                .build();
        UserRoleEntity adminRole = UserRoleEntity.builder().role(RoleEnum.CUSTOMER).user(customerUser).build();
        customerUser.setUserRoles(Set.of(adminRole));
        userRepository.save(customerUser);
    }
}
