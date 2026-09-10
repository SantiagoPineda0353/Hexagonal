package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class UserUseCase implements IUserServicePort {

    private static final int idRol=2;
    private static final int minAge=18;
    private static final Pattern emainPattern= Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern cellphonePattern = Pattern.compile("^\\+?[0-9]{1,13}$");
    private static final Pattern documentPattern=Pattern.compile("^[0-9]+$");

    private final IUserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public UserUseCase(IUserPersistencePort userPersistencePort, PasswordEncoder passwordEncoder) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveOwer(UserModel userModel) {
        validateEmail(userModel.getEmail());
        validateAge(userModel.getBirthDate());
        validateCellphone(userModel.getCellphone());
        validateDocument(userModel.getDocument());

        if(userPersistencePort.existsByEmail(userModel.getEmail())){
            throw new InvalidEmailDuplicate();
        }
        userModel.setIdRole(idRol);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userPersistencePort.saveUser(userModel);
    }

    private void validateEmail(String email){
        if (email==null ||!emainPattern.matcher(email).matches()){
            throw new InvalidEmailException();
        }
    }

    private void validateAge(LocalDate birthDate){
        if (birthDate==null || Period.between(birthDate,LocalDate.now()).getYears() < minAge){
            throw new InvalidUserAgeException();
        }
    }

    private void validateCellphone(String cellphone){
        if (cellphone==null|| cellphone.length()>13 || !cellphonePattern.matcher(cellphone).matches()){
            throw new InvalidCellphoneException();
        }
    }

    private void validateDocument(String document){
        if (document.isEmpty() || !documentPattern.matcher(document).matches()){
            throw new InvalidDocumentException();
        }
    }
}
