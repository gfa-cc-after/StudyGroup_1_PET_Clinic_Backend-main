package com.greenfox.dramacsoport.petclinicbackend.config;

//@Configuration
//public class ModelMapperConfig {
//
//    public static final ModelMapper modelMapper = new ModelMapper();
//
//    @Bean
//    public ModelMapper modelMapper() {
//        addTypeMappings();
//        return modelMapper;
//    }
//
//    private void addTypeMappings() {
//        editUserToAppUser();
//
//    }
//
//    private void editUserToAppUser() {
//        TypeMap<EditUserRequestDTO, AppUser> typeMap =
//                modelMapper.typeMap(EditUserRequestDTO.class, AppUser.class);
//
//        typeMap.addMappings(mapping -> {
//
//            mapping.map(EditUserRequestDTO::email, AppUser::setEmail);
//            mapping.map(EditUserRequestDTO::displayName, AppUser::setDisplayName);
//            mapping.map(EditUserRequestDTO::password, AppUser::setPassword);
//            mapping.skip(AppUser::setId);
//            mapping.skip(AppUser::setPets);
//            mapping.skip(AppUser::setRole);
//        });
//    }
//}
