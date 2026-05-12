package com.museum.export.infrastructure;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.aggregate.ArtworkImage;
import com.museum.export.domain.contracts.ExportGenerator;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class XmlExportGenerator implements ExportGenerator {
    @Override
    public String getFormat() { return "XML"; }

    @Override
    public String generate(List<Artwork> artworks) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<artworks>\n");

        for (Artwork artwork : artworks) {
            sb.append("  <artwork>\n");
            sb.append("    <id>").append(artwork.artworkId().value()).append("</id>\n");
            sb.append("    <artistId>").append(artwork.artistId()).append("</artistId>\n");
            sb.append("    <title>").append(escapeXml(artwork.title())).append("</title>\n");
            sb.append("    <type>").append(artwork.artworkType()).append("</type>\n");
            sb.append("    <images>\n");
            for (ArtworkImage image : artwork.images()) {
                sb.append("      <image>\n");
                sb.append("        <id>").append(image.imageId()).append("</id>\n");
                sb.append("        <path>").append(escapeXml(image.imagePath())).append("</path>\n");
                sb.append("      </image>\n");
            }
            sb.append("    </images>\n");
            sb.append("  </artwork>\n");
        }

        sb.append("</artworks>");
        return sb.toString();
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
