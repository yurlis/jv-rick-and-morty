package mate.academy.rickandmorty.initialization;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.repository.CharacterRepository;
import mate.academy.rickandmorty.service.CharacterService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final CharacterService characterService;
    private final CharacterRepository characterRepository;

    @PostConstruct
    public void init() {
        if (characterRepository.count() == 0) {
            characterService.syncCharactersFromExternalApi();
        }
    }
}
