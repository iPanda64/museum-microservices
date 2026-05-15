package com.museum.export.infrastructure;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.aggregate.ArtworkImage;
import com.museum.export.domain.contracts.ExportStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DocExportStrategy implements ExportStrategy {
    @Override
    public String getFormat() { return "DOC"; }

    @Override
    public String generate(List<Artwork> artworks) {
        StringBuilder sb = new StringBuilder();
        sb.append("MUSEUM ARTWORK CATALOG EXPORT\n");
        sb.append("=============================\n\n");

        for (Artwork artwork : artworks) {
            sb.append("Artwork ID: ").append(artwork.artworkId().value()).append("\n");
            sb.append("Title:      ").append(artwork.title()).append("\n");
            sb.append("Artist ID:  ").append(artwork.artistId()).append("\n");
            sb.append("Type:       ").append(artwork.artworkType()).append("\n");
            sb.append("Images:\n");
            if (artwork.images().isEmpty()) {
                sb.append("  - No images available\n");
            } else {
                for (ArtworkImage image : artwork.images()) {
                    sb.append("  - [ID: ").append(image.imageId()).append("] ").append(image.imagePath()).append("\n");
                }
            }
            sb.append("-----------------------------\n\n");
        }

        return sb.toString();
    }
}
