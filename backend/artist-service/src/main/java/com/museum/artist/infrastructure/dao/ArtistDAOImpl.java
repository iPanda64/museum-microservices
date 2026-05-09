package com.museum.artist.infrastructure.dao;

import com.museum.artist.domain.daocontracts.ArtistDAO;
import com.museum.artist.domain.aggregate.Artist;
import com.museum.artist.domain.aggregate.ArtistId;
import com.museum.artist.infrastructure.entities.ArtistEntity;
import com.museum.artist.infrastructure.mappers.ArtistMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ArtistDAOImpl implements ArtistDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final ArtistMapper artistMapper;

    public ArtistDAOImpl(ArtistMapper artistMapper) {
        this.artistMapper = artistMapper;
    }

    @Override
    public List<Artist> findAll() {
        List<ArtistEntity> entities = entityManager
                .createQuery("SELECT a FROM ArtistEntity a", ArtistEntity.class)
                .getResultList();

        return entities.stream()
                .map(artistMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Artist> findById(ArtistId artistId) {
        ArtistEntity entity = entityManager.find(ArtistEntity.class, artistId.value());
        return Optional.ofNullable(artistMapper.toDomain(entity));
    }

    @Override
    public List<Artist> findByName(String name) {
        List<ArtistEntity> entities = entityManager
                .createQuery("SELECT a FROM ArtistEntity a WHERE a.fullName LIKE :name", ArtistEntity.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();

        return entities.stream()
                .map(artistMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Artist save(Artist artist) {
        ArtistEntity entity = artistMapper.toEntity(artist);

        if (entity.getArtistId() == null || !existsById(new ArtistId(entity.getArtistId()))) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return artistMapper.toDomain(entity);
    }

    @Override
    public void deleteById(ArtistId artistId) {
        ArtistEntity entity = entityManager.find(ArtistEntity.class, artistId.value());
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public boolean existsById(ArtistId artistId) {
        if (artistId == null || artistId.value() == null) return false;
        Long count = entityManager.createQuery(
                        "SELECT count(a) FROM ArtistEntity a WHERE a.artistId = :aid", Long.class)
                .setParameter("aid", artistId.value())
                .getSingleResult();
        return count > 0;
    }
}
