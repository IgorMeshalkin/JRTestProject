package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.game.controller.PlayerOrder.ID;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "title", required = false) String title,
                                                      @RequestParam(value = "race", required = false) Race race,
                                                      @RequestParam(value = "profession", required = false) Profession profession,
                                                      @RequestParam(value = "after", required = false) Long after,
                                                      @RequestParam(value = "before", required = false) Long before,
                                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                      @RequestParam(value = "order", required = false) PlayerOrder order,
                                                      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

            if (pageSize == null) {
                pageSize = 3;
            }
            if (pageNumber == null) {
                pageNumber = 0;
            }
            if (order == null) {
                order = ID;
            }
        PlayerOrder finalOrder = order;
        List<Player> allPlayers = playerService.findAll().stream().sorted(((player1, player2) -> {
            if (PlayerOrder.LEVEL.equals(finalOrder)) {
                return player1.getLevel().compareTo(player2.getLevel());
            }
            if (PlayerOrder.BIRTHDAY.equals(finalOrder)) {
                return player1.getBirthday().compareTo(player2.getBirthday());
            }

            if (PlayerOrder.EXPERIENCE.equals(finalOrder)) {
                return player1.getExperience().compareTo(player2.getExperience());
            }
            if (PlayerOrder.NAME.equals(finalOrder)) {
                return player1.getName().compareTo(player2.getName());
            }
            return player1.getId().compareTo(player2.getId());
        }))
                    .filter(player -> name == null || player.getName().contains(name))
                    .filter(player -> title == null || player.getTitle().contains(title))
                    .filter(player -> race == null || player.getRace().equals(race))
                    .filter(player -> profession == null || player.getProfession().equals(profession))
                    .filter(player -> after == null || player.getBirthday().getTime() > after )
                    .filter(player -> before == null || player.getBirthday().getTime() < before)
                    .filter(player -> banned == null || player.isBanned() == banned)
                    .filter(player -> minExperience == null || player.getExperience() >= minExperience)
                    .filter(player -> maxExperience == null || player.getExperience() <= maxExperience)
                    .filter(player -> minLevel == null || player.getLevel() >= minLevel)
                    .filter(player -> maxLevel == null || player.getLevel() <= maxLevel)
                    .skip((long) pageSize * pageNumber)
                    .limit(pageSize)
                    .collect(Collectors.toList());

        return new ResponseEntity<>(allPlayers, HttpStatus.OK);
    }



    @GetMapping("/players/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "title", required = false) String title,
                                                   @RequestParam(value = "race", required = false) Race race,
                                                   @RequestParam(value = "profession", required = false) Profession profession,
                                                   @RequestParam(value = "after", required = false) Long after,
                                                   @RequestParam(value = "before", required = false) Long before,
                                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        List<Player> allPlayers = playerService.findAll().stream()
                .filter(player -> name == null || player.getName().equals(name) || player.getName().contains(name))
                .filter(player -> title == null || player.getTitle().equals(title) || player.getTitle().contains(title))
                .filter(player -> race == null || player.getRace().equals(race))
                .filter(player -> profession == null || player.getProfession().equals(profession))
                .filter(player -> after == null || player.getBirthday().getTime() >= after)
                .filter(player -> before == null || player.getBirthday().getTime() <= before)
                .filter(player -> banned == null || player.isBanned() == banned)
                .filter(player -> minExperience == null || player.getExperience() >= minExperience)
                .filter(player -> maxExperience == null || player.getExperience() <= maxExperience)
                .filter(player -> minLevel == null || player.getLevel() >= minLevel)
                .filter(player -> maxLevel == null || player.getLevel() <= maxLevel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(allPlayers.size(), HttpStatus.OK);
    }


    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable(value = "id", required = false) Long id) {

        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!playerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        playerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable(value = "id", required = false) Long id) {

        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!playerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player currentPlayer = playerService.findPlayerById(id);

        return new ResponseEntity<>(currentPlayer, HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {

        if (player.getName() == null
                || player.getTitle() == null
                || player.getRace() == null
                || player.getProfession() == null
                || player.getBirthday() == null
                || player.getExperience() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getName().length() > 12 || player.getTitle().length() > 30
                || player.getExperience() < 0 || player.getExperience() > 10000000L
                || player.getBirthday().getTime() < 946684800000L || player.getBirthday().getTime() > 32535215999000L
                || player.getName().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player result = new Player();
        if (!player.isBannedNull()) {
            result.setBanned(player.isBanned());
        }
        result.setName(player.getName());
        result.setTitle(player.getTitle());
        result.setRace(player.getRace());
        result.setProfession(player.getProfession());
        result.setBirthday(player.getBirthday());
        result.setExperience(player.getExperience());

        playerService.save(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id", required = false) Long id,
                                               @RequestBody Player player) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!playerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player currentPlayer = playerService.findPlayerById(id);

        if (player.getName() != null) {
            if (player.getName().length() > 12 || player.getName().equals("")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            currentPlayer.setName(player.getName());
        }

        if (player.getTitle() != null) {
            if (player.getTitle().length() > 30) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            currentPlayer.setTitle(player.getTitle());
        }

        if (player.getRace() != null) {
            currentPlayer.setRace(player.getRace());
        }

        if (player.getProfession() != null) {
            currentPlayer.setProfession(player.getProfession());
        }

        if (player.getBirthday() != null) {
            Long minBirthday = 946684800000L;
            Long maxBirthday = 32535215999000L;
            Long currentBirthday = player.getBirthday().getTime();
            if (currentBirthday < minBirthday || currentBirthday > maxBirthday) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            currentPlayer.setBirthday(player.getBirthday());
        }

        if (!player.isBannedNull()) {
            currentPlayer.setBanned(player.isBanned());
        }

        if (player.getExperience() != null) {
            if (player.getExperience() < 0 || player.getExperience() > 10000000L) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            currentPlayer.setExperience(player.getExperience());
        }

        playerService.save(currentPlayer);
        return new ResponseEntity<>(currentPlayer, HttpStatus.OK);
    }
}





