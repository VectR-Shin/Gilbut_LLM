package com.gilbut.llmService.Repository;

import com.gilbut.llmService.Domain.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Location 엔티티에 대한 기본 조작
 */

@Repository
public class LocationRepository {
    @PersistenceContext
    private EntityManager em;

    // 저장 및 업데이트
    // Id 가 null 이라면 저장, 존재한다면 업데이트
    public Location save(Location location) {
        if (location.getId() == null) {
            em.persist(location);
            return location;
        } else {
            return em.merge(location);
        }
    }

    // Location 을 인공 PK 기반으로 검색 및 반환
    // 존재하지 않을 경우, null 반환
    // 응답은 Optional<> 에 담겨서 반환
    public Optional<Location> findById(Long id) {
        Location location = em.find(Location.class, id);
        return Optional.ofNullable(location);
    }

    // Location 에 저장된 모든 데이터 반환
    public List<Location> findAll() {
        return em.createQuery("SELECT L FROM Location L", Location.class)
                .getResultList();
    }

    // 인공 PK 기반으로 데이터 삭제
    public void delete(Long id) {
        Location location = em.find(Location.class, id);
        if (location != null) {
            em.remove(location);
        }
    }

    // location(장소명)을 기반으로 데이터를 하나만 반환.
    // location 속성에는 UNIQUE 제약조건 있으므로 1개 반환됨.
    // 만약, 없을 경우, null 이 담겨서 반환된다.
    public Optional<Location> findByName(String locationName) {
        List<Location> result = em.createQuery(
                "SELECT L FROM Location L WHERE L.location = :loc",
                Location.class
        ).setParameter("loc", locationName)
                .getResultList();

        return result.stream().findFirst();
    }
}
