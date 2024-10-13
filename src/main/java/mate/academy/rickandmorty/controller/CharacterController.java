package mate.academy.rickandmorty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.service.CharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Character search",
        description = "Endpoints for searching characters from Rick and Morty")
@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
public class CharacterController {
    private final CharacterService characterService;

    @Operation(summary = "Get random character",
            description = "Get information about random character")
    @GetMapping("/random")
    public Character getRandomCharacter() {
        return characterService.getRandomCharacter();
    }

    @Operation(summary = "Search characters",
            description = "Get all characters whose names include requested part")
    @GetMapping("/search")
    public List<Character> getCharactersByNamePart(@RequestParam String namePart) {
        return characterService.findByNameContaining(namePart);
    }
}
