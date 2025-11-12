package com.mvsaude.clinic.integracao;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCepResponse buscar(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, ViaCepResponse.class);
    }

    public record ViaCepResponse(
            String cep,
            String logradouro,
            String complemento,
            String bairro,
            String localidade,
            String uf,
            String ibge
    ) {}
}
