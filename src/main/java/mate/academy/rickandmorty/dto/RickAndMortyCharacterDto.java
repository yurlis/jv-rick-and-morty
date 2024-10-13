package mate.academy.rickandmorty.dto;

public record RickAndMortyCharacterDto(
        Long id,
        String name,
        String status,
        String gender
) {
}
