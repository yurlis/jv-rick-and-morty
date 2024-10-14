package mate.academy.rickandmorty.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {
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
}
