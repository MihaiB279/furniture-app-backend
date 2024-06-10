package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.User.Address;
import com.back.web.furniture.Domain.User.User;
import com.back.web.furniture.Dto.UserDto;
import com.back.web.furniture.Repository.RepositoryAddress;
import com.back.web.furniture.Repository.RepositoryPaypalPayment;
import com.back.web.furniture.Repository.RepositoryUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.back.web.furniture.Service.ServiceUtils.setAddressDetails;

@Service
@RequiredArgsConstructor
public class ServiceUserImpl implements ServiceUser{
    private final RepositoryUser repositoryUser;
    private final RepositoryAddress repositoryAddress;
    private final RepositoryPaypalPayment repositoryPaypalPayment;
    private final ModelMapper modelMapper;

    @Override
    public UserDto getUser(String username) {
        User user = repositoryUser.findByUsername(username).orElse(null);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = repositoryUser.findByUsername(userDto.getUsername()).orElse(null);
        if (user != null) {
            Address currentAddress = user.getAddress();
            if (repositoryPaypalPayment.existsByDeliveryAddress(currentAddress)) {
                Address newAddress = new Address();
                setAddressDetails(newAddress, modelMapper.map(userDto.getAddress(), Address.class));
                repositoryAddress.save(newAddress);
                user.setAddress(newAddress);
            } else {
                setAddressDetails(currentAddress, modelMapper.map(userDto.getAddress(), Address.class));
                repositoryAddress.save(currentAddress);
            }

            repositoryUser.save(user);
            return userDto;
        }
        return null;
    }
}
