package com.example.Simple.Order.Management.System.service.impl;

import com.example.Simple.Order.Management.System.dto.UserRequestDto;
import com.example.Simple.Order.Management.System.dto.UserResponseDto;
import com.example.Simple.Order.Management.System.exception.ResourceNotFoundException;
import com.example.Simple.Order.Management.System.model.User;
import com.example.Simple.Order.Management.System.repository.UserRepository;
import com.example.Simple.Order.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
        user = userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID" + id + " not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID" + id + " not found"));

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID" + id + " not found"));
        userRepository.delete(user);
    }
}
