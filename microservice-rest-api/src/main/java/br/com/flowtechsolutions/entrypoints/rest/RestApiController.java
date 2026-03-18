package br.com.flowtechsolutions.entrypoints.rest;

import br.com.flowtechsolutions.core.usecase.BusinessUseCase;
import org.springframework.web.bind.annotation.RestController;

/*
TODO
  Aqui você pode implementar a interface gerada pelo openapi generator,
  essa interface está localidada raiz do projeto na pasta build/generated/src/main/resource/.../api.
  OBS: Se houver mais de uma interface separar em diferentes classes.
*/
@RestController
public class RestApiController { //TODO implements YourGeneratedControllerApi {
    private final BusinessUseCase businessUseCase;

    public RestApiController(final BusinessUseCase businessUseCase) {
        this.businessUseCase = businessUseCase;
    }
}