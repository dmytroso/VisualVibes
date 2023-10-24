package com.vv.VisualVibes.facade;

import com.vv.VisualVibes.dto.UserDTO;
import com.vv.VisualVibes.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBio(user.getBio());
        return userDTO;
    }
}
