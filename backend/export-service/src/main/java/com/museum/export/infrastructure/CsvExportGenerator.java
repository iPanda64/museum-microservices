package com.museum.export.infrastructure;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.aggregate.ArtworkImage;
import com.museum.export.domain.contracts.ExportGenerator;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvExportGenerator implements ExportGenerator {
    @Override
    public String getFormat() { return "CSV"; }

    @Override
    public String generate(List<Artwork> artworks) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,ArtistID,Title,Type,Images\n");

        for (Artwork artwork : artworks) {
            String images = artwork.images().stream()
                    .map(ArtworkImage::imagePath)
                    .collect(Collectors.joining(";"));

            sb.append(artwork.artworkId().value()).append(",")
              .append(artwork.artistId()).append(",")
              .append("\"").append(artwork.title().replace("\"", "\"\"")).append("\",")
              .append(artwork.artworkType()).append(",")
              .append("\"").append(images).append("\"\n");
        }
        return sb.toString();
    }
}
