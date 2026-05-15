package com.museum.export.domain.contracts;

import com.museum.export.domain.aggregate.Artwork;
import java.util.List;

public interface ExportStrategy {
    String getFormat();
    String generate(List<Artwork> artworks);
}
