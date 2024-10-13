package mate.academy.rickandmorty.dto;

public record RickAndMortyPageInfoDto(
        int pages,
        int count,
        String next,
        String prev
) {
}
