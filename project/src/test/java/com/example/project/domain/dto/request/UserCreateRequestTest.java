package com.example.project.domain.dto.request;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * UserCreateRequestTest
 */
@RunWith(MockitoJUnitRunner.class)
public class UserCreateRequestTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static final UserCreateRequest usrInvalidNull = UserCreateRequest.builder() //
            .email(null).password(null).isAdmin(null).build();

    public static final UserCreateRequest usrInvalidEmail = UserCreateRequest.builder() //
            .email("email").password("senha12345").isAdmin(false).build();

    public static final UserCreateRequest usrValidEmail1 = UserCreateRequest.builder() //
            .email("email@email.com").password("senha12345").isAdmin(false).build();

    public static final UserCreateRequest usrValidEmail2 = UserCreateRequest.builder() //
            .email("email@email.com.br").password("senha12345").isAdmin(false).build();

    @Test
    public void should_NotBeValid_WhenNull() {
        UserCreateRequest createDto = usrInvalidNull;

        Set<ConstraintViolation<UserCreateRequest>> constraintViolations = validator.validate(createDto);

        assertTrue("Modelo deve ser inválido", constraintViolations.size() > 0);
    }

    @Test
    public void should_NotBeValid_WhenInvalidEmail() {
        UserCreateRequest createDto = usrInvalidEmail;

        Set<ConstraintViolation<UserCreateRequest>> constraintViolations = validator.validate(createDto);

        assertTrue("Modelo deve ser inválido", constraintViolations.size() == 1);
    }

    @Test
    public void should_BeValid_WhenValidData() {
        UserCreateRequest createDto = usrValidEmail1;

        Set<ConstraintViolation<UserCreateRequest>> constraintViolations = validator.validate(createDto);

        assertTrue("Modelo deve ser inválido", constraintViolations.size() == 0);
    }

    @Test
    public void should_BeValid_WhenValidData_2() {
        
        UserCreateRequest createDto = usrValidEmail2;

        Set<ConstraintViolation<UserCreateRequest>> constraintViolations = validator.validate(createDto);

        assertTrue("Modelo deve ser inválido", constraintViolations.size() == 0);
    }
}