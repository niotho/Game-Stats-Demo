package com.stats.StatisticProject.service;

import com.stats.StatisticProject.dao.GameRepository;
import com.stats.StatisticProject.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {

    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    //find
    public List<Game> findAll(){
        return gameRepository.findAll();
    }
    public List<Game> findAllByName(String name){
        return gameRepository.findAllFromName(name);
    }
    public List<Game> findAllByResult(String result){
        return  gameRepository.findAllByResult(result);
    }
    public List<Game> findAllByNameAndResult(String name, String result){
        return gameRepository.findAllByNameAndResult(name, result);
    }
    public List<Game> findByDate(String Date){
        return gameRepository.findByDate(Date);
    }
    public List<Game> findByDateBetween(String startDate, String endDate){
        return gameRepository.findByDateBetween(startDate, endDate);
    }
    public List<Game> findAllByNameByDateBetween(String name, String startDate, String endDate){
        return gameRepository.findAllByNameByDateBetween(name, startDate, endDate);
    }
    public List<Game> findAllByResultByDateBetween(String result, String startDate, String endDate){
        return gameRepository.findAllByResultByDateBetween(result, startDate, endDate);
    }
    public List<Game> findAllByNameAndResultByDate(String name, String result, String startDate, String endDate){
        return gameRepository.findAllByNameAndResultByDateBetween(name, result, startDate, endDate);
    }
    public List<Game> findAllBetweenPlayers(String name, String opponent){
        return gameRepository.findAllBetweenPlayers(name, opponent);
    }
    public List<Game> findAllBetweenPlayersByResult(String name, String opponent, String result){
        return gameRepository.findAllBetweenPlayersByResult(name, opponent, result);
    }
    public List<Game> findAllBetweenPlayersByDateBetween(String name, String opponent, String startDate, String endDate){
        return gameRepository.findAllBetweenPlayersByDateBetween(name, opponent, startDate, endDate);
    }
    public List<Game> findAllBetweenPlayersByResultByDateBetween(String name, String opponent, String result, String startDate, String endDate){
        return gameRepository.findAllBetweenPlayersByResultByDateBetween(name, opponent, result, startDate, endDate);
    }
    public Game findById(Long theId){
        return gameRepository.findById(theId);
    }

    //create
    @Transactional
    public void save(Game game){
        gameRepository.addPlayer(game);
    }

    //update
    @Transactional
    public void update(Game game){
        gameRepository.updatePlayer(game);
    }

    //delete
    @Transactional
    public void deleteById(Long theId){
        gameRepository.deletePlayerById(theId);
    }
}














