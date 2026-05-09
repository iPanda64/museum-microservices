package com.museum.art.infrastructure.dao;

import com.museum.art.domain.daocontracts.ArtworkDAO;
import com.museum.art.domain.aggregate.Artwork;
import com.museum.art.domain.aggregate.ArtworkId;
import com.museum.art.infrastructure.entities.ArtworkEntity;
import com.museum.art.infrastructure.mappers.ArtworkMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ArtworkDAOImpl implements ArtworkDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final ArtworkMapper artworkMapper;

    public ArtworkDAOImpl(ArtworkMapper artworkMapper) {
        this.artworkMapper = artworkMapper;
    }

    @Override
    public List<Artwork> findAll(String sortBy, String order) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtworkEntity> cq = cb.createQuery(ArtworkEntity.class);
        Root<ArtworkEntity> root = cq.from(ArtworkEntity.class);

        if (sortBy != null && !sortBy.isBlank()) {
            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(root.get(sortBy)));
            } else {
                cq.orderBy(cb.asc(root.get(sortBy)));
            }
        }

        TypedQuery<ArtworkEntity> query = entityManager.createQuery(cq);
        return query.getResultList().stream()
                .map(artworkMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Artwork> findById(ArtworkId artworkId) {
        ArtworkEntity entity = entityManager.find(ArtworkEntity.class, artworkId.value());
        return Optional.ofNullable(artworkMapper.toDomain(entity));
    }

    @Override
    public List<Artwork> findByTitle(String title) {
        List<ArtworkEntity> entities = entityManager
                .createQuery("SELECT a FROM ArtworkEntity a WHERE a.title LIKE :title", ArtworkEntity.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();

        return entities.stream()
                .map(artworkMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Artwork save(Artwork artwork) {
        ArtworkEntity entity = artworkMapper.toEntity(artwork);

        if (entity.getArtworkId() == null || !existsById(new ArtworkId(entity.getArtworkId()))) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return artworkMapper.toDomain(entity);
    }

    @Override
    public void deleteById(ArtworkId artworkId) {
        ArtworkEntity entity = entityManager.find(ArtworkEntity.class, artworkId.value());
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public boolean existsById(ArtworkId artworkId) {
        if (artworkId == null || artworkId.value() == null) return false;
        Long count = entityManager.createQuery(
                        "SELECT count(a) FROM ArtworkEntity a WHERE a.artworkId = :aid", Long.class)
                .setParameter("aid", artworkId.value())
                .getSingleResult();
        return count > 0;
    }
}
