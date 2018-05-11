package io.skalogs.skaetl.web;

import io.skalogs.skaetl.generator.GeneratorErrorService;
import io.skalogs.skaetl.generator.GeneratorService;
import io.skalogs.skaetl.web.domain.PayloadTopic;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/generator")
@AllArgsConstructor
public class GeneratorController {

    private final GeneratorService generatorService;
    private final GeneratorErrorService generatorErrorService;

    @ResponseStatus(CREATED)
    @PutMapping("/inputTopic")
    public void inputTopic(@Valid @RequestBody PayloadTopic payload) {
        generatorService.createRandom(payload.getNbElemBySlot(), payload.getNbSlot());
    }

    @ResponseStatus(CREATED)
    @PutMapping("/errorTopic")
    public void errorTopic(@Valid @RequestBody PayloadTopic payload) {
        generatorErrorService.createRandom(payload.getNbElemBySlot());
    }
}
