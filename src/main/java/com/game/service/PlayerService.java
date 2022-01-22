package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class PlayerService {
    public final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    public Player findPlayerById(Long id) {
        List<Player> list = playerRepository.findAll();
        Player result = null;
        for (Player p : list) {
            if (id == p.getId()) {
                result = p;
                break;
            }
        }
        return result;
    }

    public void save(Player player) {
        playerRepository.save(player);
    }
}
