package com.stats.StatisticProject.controller;

import com.stats.StatisticProject.entity.Game;
import com.stats.StatisticProject.service.EmailService;
import com.stats.StatisticProject.service.GameService;
import com.stats.StatisticProject.tg.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    private final EmailService emailService;
    private GameService gameService;

    private TelegramBot telegramBot;

    @Autowired
    public MainController(EmailService emailService, GameService gameService, TelegramBot telegramBot) {
        this.emailService = emailService;
        this.gameService = gameService;
        this.telegramBot = telegramBot;
    }


    @GetMapping("/home")
    public String home(Model model) {
        return "home-page";
    }

    @PostMapping("/list")
    public String list(Model model, @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "opponent", required = false) String opponent,
                       @RequestParam(value = "winOrLose", required = false) String result,
                       @RequestParam(value = "date", required = false) String date,
                       @RequestParam(value = "isDate", required = false) String isDate,
                       @RequestParam(value = "secondDate", required = false) String secondDate,
                       @RequestParam(value = "isSecondDate", required = false) String isSecondDate) {

        List<Game> games = gameService.findAll();
        boolean everything = false;

        //with specific opponent without date
        if (isDate.equals("false") && !opponent.equals("Everyone")) {
            if ("Everything".equals(result)) {
                games = gameService.findAllBetweenPlayers(name, opponent);
                everything = false;
            } else {
                games = gameService.findAllBetweenPlayersByResult(name, opponent, result);
                everything = false;
            }
        }

        //with specific opponent with date
        if (isDate.equals("true") && !opponent.equals("Everyone")) {
            String tempDate;
            if (isSecondDate != null) {
                tempDate = secondDate;
            } else {
                tempDate = date;
            }
            if ("Everything".equals(result)) {
                games = gameService.findAllBetweenPlayersByDateBetween(name, opponent, date, tempDate);
                everything = false;
            } else {
                games = gameService.findAllBetweenPlayersByResultByDateBetween(name, opponent, result, date, tempDate);
                everything = false;
            }
        }

        //without date
        if (isDate.equals("false") && opponent.equals("Everyone")) {
            if (("Everything".equals(result)) && ("Everyone".equals(name))) {
                games = gameService.findAll();
                everything = true;
            } else if ("Everything".equals(result)) {
                games = gameService.findAllByName(name);
            } else if ("Everyone".equals(name)) {
                games = gameService.findAllByResult(result);
                everything = true;
            } else {
                games = gameService.findAllByNameAndResult(name, result);
            }
        }

        //with date
        if (isDate.equals("true") && opponent.equals("Everyone")) {
            String tempDate;
            if (isSecondDate != null) {
                tempDate = secondDate;
            } else {
                tempDate = date;
            }
            if (("Everything".equals(result)) && ("Everyone".equals(name))) {
                games = gameService.findByDateBetween(date, tempDate);
                everything = true;
            } else if ("Everything".equals(result)) {
                games = gameService.findAllByNameByDateBetween(name, date, tempDate);
            } else if ("Everyone".equals(name)) {
                games = gameService.findAllByResultByDateBetween(result, date, tempDate);
                everything = true;
            } else {
                games = gameService.findAllByNameAndResultByDate(name, result, date, tempDate);
            }
        }

        int scoreList = 0;
        int opponentsScoreList = 0;
        int gamesAmount = games.size();
        int winAmount = 0;
        int loseAmount = 0;
        for (Game game : games) {
            scoreList += game.getMyScore();
            opponentsScoreList += game.getOpponentScore();
            if (Objects.equals(game.winOrLose, "W")) {
                winAmount++;
            } else if (Objects.equals(game.winOrLose, "L")) {
                loseAmount++;
            }
        }

        int allScore = scoreList + opponentsScoreList;

        model.addAttribute("winAmount", winAmount);
        model.addAttribute("loseAmount", loseAmount);
        model.addAttribute("gamesAmount", gamesAmount);
        model.addAttribute("date", date);
        model.addAttribute("secondDate", secondDate);
        model.addAttribute("isEverything", everything);
        model.addAttribute("allScore", allScore);
        model.addAttribute("scoreList", scoreList);
        model.addAttribute("opponentsScoreList", opponentsScoreList);
        model.addAttribute("games", games);
        model.addAttribute("player", name);
        model.addAttribute("result", result);

        return "list-page";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {

        Game game = new Game();
        LocalDate defaultDate = LocalDate.of(2017, 6, 1);

        model.addAttribute("newDate", defaultDate);
        model.addAttribute("game", game);

        return "add-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("game") Game game) {
        boolean opponentWin = false;

        if (game.getMyScore() > game.getOpponentScore()) {
            game.setWinOrLose("W");
        } else {
            game.setWinOrLose("L");
            opponentWin = true;
        }

        String result = "L";

        if (game.getWinOrLose().equals("W")) {
            result = "L";
        } else {
            result = "W";
        }

        Game opponentsGame = new Game(game.getOpponent(), game.getPerson(), result, game.getOpponentScore(), game.getMyScore(), game.getDate());

        String personName = game.getPerson();
        String opponentName = game.getOpponent();

        gameService.save(game);
        gameService.save(opponentsGame);
        emailService.sendMailTo(emailService.getEmail(personName),
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #00FF00; }" +
                        ".container { max-width: 600px; margin: auto; padding: 20px; background-color: #fff; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,.1); }" +
                        "h1 { color: #333; text-align: center; }" +
                        "p { text-align: center; }" +
                        ".result { font-weight: bold; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<h1>Game Result</h1>" +
                        "<p>You played against " + opponentName + " and you " + game.winOrLose + " the game with score: " + game.getMyScore()  + "</p>" +
                        "<p>" + personName + " " + game.getMyScore() + " vs " + opponentName + " " + game.getOpponentScore() + "</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>");

        String opponentWinOrLose = opponentWin ? "won" : "lost";
        emailService.sendMailTo(emailService.getEmail(opponentName),
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #FF0000; }" +
                        ".container { max-width: 600px; margin: auto; padding: 20px; background-color: #fff; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,.1); }" +
                        "h1 { color: #333; text-align: center; }" +
                        "p { text-align: center; }" +
                        ".result { font-weight: bold; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<h1>Game Result</h1>" +
                        "<p>You played against " + personName + " and you " + opponentWinOrLose + " the game with score: " + game.getOpponentScore()  + "</p>" +
                        "<p>" + personName + " " + game.getMyScore() + " vs " + opponentName + " " + game.getOpponentScore() + "</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>");

//        telegramBot.sendMessageToAdmin(opponentWinOrLose);

        return "redirect:/showFormForAdd";
    }

    //delete
    @GetMapping("/delete")
    public String delete(@RequestParam Long gameIdForDelete) {

        gameService.deleteById(gameIdForDelete);

        return "redirect:/home";
    }
}