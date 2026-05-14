package com.nod.backend.distributed_coding_genie.account_service.mapper;

import com.nod.backend.distributed_coding_genie.account_service.dto.auth.SignupRequest;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.UserProfileResponse;
import com.nod.backend.distributed_coding_genie.account_service.entity.User;
import com.nod.backend.distributed_coding_genie.common_lib.dto.UserDto;
import com.nod.backend.distributed_coding_genie.common_lib.security.JwtUserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest signupRequest);

    @Mapping(source = "userId", target = "id")
    UserProfileResponse toUserProfileResponse(JwtUserPrincipal user);

    UserDto toUserDto(User user);

}
