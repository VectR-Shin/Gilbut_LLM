package com.gilbut.llmService.Repository;

import com.gilbut.llmService.Domain.Hint.Hint;
import com.gilbut.llmService.Domain.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HintRepository {
    @PersistenceContext
    private EntityManager em;

    // 저장
    public Hint save(Hint hint) {
        em.persist(hint);
        return hint;
    }

    // ID 조회
    public Hint findById(Long id) {
        return em.find(Hint.class, id);
    }

    // Location 기반 조회
    public List<Hint> findByLocation(Location location) {
        return em.createQuery("SELECT H FROM Hint H WHERE H.location = :location", Hint.class)
                .setParameter("location", location)
                .getResultList();
    }

    // Keyword 기반 조회
    public List<Hint> findByKeyword(String keyword) {
        return em.createQuery("SELECT H FROM Hint H WHERE H.keyword = :keyword", Hint.class)
                .setParameter("keyword", keyword)
                .getResultList();
    }

    // 삭제
    public void delete(Long id) {
        Hint hint = em.find(Hint.class, id);
        if (hint != null)
            em.remove(hint);
    }
}
