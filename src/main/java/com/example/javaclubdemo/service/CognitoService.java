package com.example.javaclubdemo.service;

import com.example.javaclubdemo.dto.UserRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;

@Service
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;

    public CognitoService(@Value("${cognito.userPool}") String userPoolId) {
        this.userPoolId = userPoolId;
        this.cognitoClient = CognitoIdentityProviderClient.builder()
            .build();
    }

    public void createUser(UserRequestDto userRequestDto) {
        try {
            AttributeType userAttrs = AttributeType.builder()
                .name("email")
                .value(userRequestDto.getEmail())
                .name("name")
                .value(userRequestDto.getName())
                .build();

            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(userRequestDto.getEmail())
                .userAttributes(userAttrs)
                .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
                .build();

            cognitoClient.adminCreateUser(userRequest);
        } catch (CognitoIdentityProviderException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String email) {
        AdminDeleteUserRequest adminDeleteUserRequest = AdminDeleteUserRequest.builder()
            .username(email)
            .userPoolId(userPoolId)
            .build();
        cognitoClient.adminDeleteUser(adminDeleteUserRequest);
    }

}
