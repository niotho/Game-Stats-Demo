package com.stats.StatisticProject.dao;

import com.stats.StatisticProject.entity.Game;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private EntityManager entityManager;

    @Autowired
    public GameRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Game> findAll() {

        TypedQuery<Game> theQuery = entityManager.createQuery("FROM Game", Game.class);

        List<Game> games = theQuery.getResultList();

        return games;
    }

    @Override
    public Game findById(Long theId) {
        return entityManager.find(Game.class, theId);
    }

    @Override
    public List<Game> findAllFromName(String name) {

        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE person = :name", Game.class);
        query.setParameter("name", name);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllByResult(String result) {

        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE winOrLose = :result", Game.class);
        query.setParameter("result", result);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllByNameAndResult(String name, String result) {

        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE winOrLose = :result AND person = :name", Game.class);
        query.setParameter("result", result);
        query.setParameter("name", name);

        return query.getResultList();
    }

    @Override
    public List<Game> findByDate(String date) {

        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE date = :date", Game.class);
        query.setParameter("date", date);

        return query.getResultList();
    }

    @Override
    public List<Game> findByDateBetween(String startDate, String endDate) {

        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE date BETWEEN :startDate AND :endDate", Game.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllByNameByDateBetween(String name, String startDate, String endDate) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE person = :name AND date BETWEEN :startDate AND :endDate", Game.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("name", name);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllByResultByDateBetween(String result, String startDate, String endDate) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE winOrLose = :result AND date BETWEEN :startDate AND :endDate", Game.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("result", result);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllByNameAndResultByDateBetween(String name, String result, String startDate, String endDate) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE winOrLose = :result AND person = :name AND date BETWEEN :startDate AND :endDate", Game.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("name", name);
        query.setParameter("result", result);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllBetweenPlayers(String name, String opponent) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE person = :name AND opponent = :opponent", Game.class);
        query.setParameter("name", name);
        query.setParameter("opponent", opponent);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllBetweenPlayersByResult(String name, String opponent, String result) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE person = :name AND opponent = :opponent AND winOrLose = :result", Game.class);
        query.setParameter("name", name);
        query.setParameter("opponent", opponent);
        query.setParameter("result", result);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllBetweenPlayersByDateBetween(String name, String opponent, String startDate, String endDate) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE opponent = :opponent AND person = :name AND date BETWEEN :startDate AND :endDate", Game.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("name", name);
        query.setParameter("opponent", opponent);

        return query.getResultList();
    }

    @Override
    public List<Game> findAllBetweenPlayersByResultByDateBetween(String name, String opponent, String result, String startDate, String endDate) {
        TypedQuery<Game> query = entityManager.createQuery(
                "FROM Game WHERE opponent = :opponent AND person = :name AND winOrLose = :result AND date BETWEEN :startDate AND :endDate", Game.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("name", name);
        query.setParameter("opponent", opponent);
        query.setParameter("result", result);

        return query.getResultList();
    }

    @Override
    public void addPlayer(Game game) {
        entityManager.persist(game);
    }

    @Override
    public void updatePlayer(Game game) {
        entityManager.merge(game);
    }

    @Override
    public void deletePlayerById(Long theId) {

        Game game = entityManager.find(Game.class, theId);

        entityManager.remove(game);
    }
}
