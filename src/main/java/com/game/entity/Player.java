package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;


    //конструктор который принимает "banned"
    public Player(Long id, String name, String title, Race race, Profession profession, Integer experience, Integer level, Integer untilNextLevel, Date birthday, boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        //уровень и оставшийся до следующего уровня опыт считаются исходя из текущего опыта
        this.level = levelCalculation(experience);
        this.untilNextLevel = untilNextLevelCalculation(levelCalculation(experience), experience);
        this.birthday = birthday;
        this.banned = banned;
    }
    //конструктор который не принимает "banned"
    public Player(Long id, String name, String title, Race race, Profession profession, Integer experience, Integer level, Integer untilNextLevel, Date birthday) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = levelCalculation(experience);
        this.untilNextLevel = untilNextLevelCalculation(levelCalculation(experience), experience);
        this.birthday = birthday;
        this.banned = false;
    }
    // пустой конструктор на всякий случай
    public Player() {
    }
    // геттеры и сеттеры для всего подряд

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }
    // сеттер опыта вызывает сеттеры полей, зависящих от значения поля опыта
    public void setExperience(Integer experience) {
        this.experience = experience;
        this.setLevel();
        this.setUntilNextLevel();
    }

    public Integer getLevel() {
        return level;
    }

    //сеттер уровня устанавливает значение в зависимости от опыта
    public void setLevel() {
        this.level = levelCalculation(experience);
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    //сеттер оставшегося для следующего уровня опыта устанавливает значение в зависимости от текущего опыта
    public void setUntilNextLevel() {
        this.untilNextLevel = untilNextLevelCalculation(levelCalculation(experience), experience);
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean isBannedNull() {
        if (banned == null) {
            return true;
        }
        return false;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    //метод который считает уровень исходя из опыта
    private Integer levelCalculation(Integer experience) {
        Double squareRoot = Double.valueOf(2500 + 200 * experience);
        Integer square = (int) Math.round(Math.sqrt(squareRoot));
        Integer result = (square - 50) / 100;
        return result;
    }
    //метод который считает оставшийся до следующего уровня опыт исходя из текущих уровня и опыта
    private Integer untilNextLevelCalculation(Integer level, Integer experience) {
        Integer result = 50 * (level + 1) * (level + 2) - experience;
        return result;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "\n"
                + "Имя: " + getName() + "\n"
                + "Титул: " + getTitle();
    }
}
