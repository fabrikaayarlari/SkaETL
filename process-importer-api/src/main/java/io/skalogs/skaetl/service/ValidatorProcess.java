package io.skalogs.skaetl.service;


import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class ValidatorProcess {

    private final TypeValidation type;

    public abstract ValidateData process(ProcessValidation processValidation, JsonNode jsonValue, String value);

    public Boolean type(TypeValidation typeValidation) {
        return type.equals(typeValidation);
    }
}
