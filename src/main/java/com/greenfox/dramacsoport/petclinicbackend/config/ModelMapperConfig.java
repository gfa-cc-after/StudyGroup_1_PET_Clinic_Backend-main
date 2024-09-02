package com.greenfox.dramacsoport.petclinicbackend.config;

import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    public static final ModelMapper modelMapper = new ModelMapper();

    @Bean
    public ModelMapper modelMapper() {
        addTypeMappings();
        return modelMapper;
    }

    private void addTypeMappings() {
        editUserToAppUser();

    }

    private void editUserToAppUser() {
        TypeMap<EditUserRequestDTO, AppUser> typeMap =
                modelMapper.typeMap(EditUserRequestDTO.class, AppUser.class);

        typeMap.addMappings(mapping -> {

            mapping.map(EditUserRequestDTO::email, AppUser::setEmail);
            mapping.map(EditUserRequestDTO::displayName, AppUser::setDisplayName);
            mapping.map(EditUserRequestDTO::password, AppUser::setPassword);
            mapping.skip(AppUser::setId);
            mapping.skip(AppUser::setPets);
            mapping.skip(AppUser::setRole);
        });
    }
}
