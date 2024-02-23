package com.observatory.observationscheduler.domain.useraccount.dto;

import com.observatory.observationscheduler.domain.useraccount.UserAccount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAccountDtoMapper {
    UserAccount userAccountCreateDtoToUserAccount(CreateUserAccountDto userAccountCreateDto);

    UserAccount getUserAccountDtoToUserAccount(GetUserAccountDto getUserAccountDto);

    GetUserAccountDto userAccountToGetDto(UserAccount userAccount);

    List<GetUserAccountDto> userAccountListToGetDtoList(List<UserAccount> userAccountList);
}


