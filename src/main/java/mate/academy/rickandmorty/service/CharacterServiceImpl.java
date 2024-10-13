package mate.academy.rickandmorty.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private static final String RICK_AND_MORTY_API_URL = "https://rickandmortyapi.com/api/character";
    private final ObjectMapper objectMapper;
    private final CharacterMapper characterMapper;
    private final CharacterRepository characterRepository;

    @Override
    public Character getRandomCharacter() {
        long repositorySize = characterRepository.count();
        if (repositorySize == 0) {
            throw new EntityNotFoundException("No characters found in the database");
        }

        Long randomNumber = (long) (Math.random() * repositorySize) + 1;

        return characterRepository.findById(randomNumber)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with ID: "
                        + randomNumber));
    }

    @Override
    public List<Character> findByNameContaining(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void syncCharactersFromExternalApi() {
        String url = RICK_AND_MORTY_API_URL;
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
