package mate.academy.rickandmorty.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.RickAndMortyCharacterDto;
import mate.academy.rickandmorty.dto.RickAndMortyResponseDataDto;
import mate.academy.rickandmorty.mapper.CharacterMapper;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterInitializeClient {
    @Value("${rick.and.morty.api.url}")
    private String rickAndMortyApiUrl;
    private final ObjectMapper objectMapper;
    private final CharacterMapper characterMapper;
    private final CharacterRepository characterRepository;

    @PostConstruct
    public void init() {
        if (characterRepository.count() == 0) {
            syncCharactersFromExternalApi(rickAndMortyApiUrl);
        }
    }

    public void syncCharactersFromExternalApi(String url) {
        HttpClient httpClient = HttpClient.newHttpClient();
        List<RickAndMortyCharacterDto> rickAndMortyCharacterDtoList = new ArrayList<>();
        try {
            while (url != null) {
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(url))
                        .build();
                HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                        HttpResponse.BodyHandlers.ofString());
                RickAndMortyResponseDataDto responseDataDto = objectMapper.readValue(
                        httpResponse.body(), RickAndMortyResponseDataDto.class
                );
                rickAndMortyCharacterDtoList.addAll(responseDataDto.data());
                url = responseDataDto.info().next();
            }
            List<Character> characters = characterMapper.toModelList(rickAndMortyCharacterDtoList);
            characterRepository.saveAll(characters);
        } catch (IOException | InterruptedException e) {
            throw new EntityNotFoundException("Can't get data from external API", e);
        }
    }
}
