package mate.academy.rickandmorty.service;

import java.util.List;
import mate.academy.rickandmorty.model.Character;

public interface CharacterService {
    Character getRandomCharacter();

    List<Character> findByNameContaining(String name);

    void syncCharactersFromExternalApi();
}
