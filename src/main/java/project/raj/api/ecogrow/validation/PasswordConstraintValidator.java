package project.raj.api.ecogrow.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // Length rule: min=8, max=128 chars
                new LengthRule(8, 128),
                // At least one upper case letter
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // At least one lower-case letter
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // At least one digit
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // At least one special character
                new CharacterRule(EnglishCharacterData.Special, 1),
                // No whitespace
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(",", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}