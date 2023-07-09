package lk.epic.springBoot_middleware.controller;

import lk.epic.springBoot_middleware.dto.ISO8583FieldsDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/springBoot_middleware")
@CrossOrigin
public class SpringBoot_Middleware {
    @PostMapping
    public void workWithJsonAndISO8583(@RequestBody ISO8583FieldsDTO allFields) {

    }
}
