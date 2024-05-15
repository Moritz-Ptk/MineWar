package com.teqless.minewar.teams;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameState;
import com.teqless.minewar.game.Messages;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Team {

    private final String name;

    private int lives;

    private final ArrayList<User> members;

    public Team(String name) {

        this.name = name;
        this.lives = 24;

        members = new ArrayList<>();
    }

    public void addKill(User user) {

        user.addKill();

        AtomicInteger points = new AtomicInteger(10);
        members.forEach(teamMate -> {
            if(teamMate.getUUID() != user.getUUID() && teamMate.getTeam().equals(user.getTeam())) {
                teamMate.addPoints(1);
                points.getAndDecrement();
            }
        });

        user.addPoints(points.get());

    }

    public String getName() {
        return name;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void reduceLives() {

        if(lives > 1) {
            lives -= 1;
        } else {
            lives = 0;
            Messages.broadcastMessage(Messages.TEAM_NO_LIVES_LEFT_MESSAGE.replace("%team", getName()));
            MineWar.getHandler().handleTeamElimination(this);
        }

    }

    public int getLives() {
        return lives;
    }

    public void addMember(User user) {
        members.add(user);
        user.setTeam(this);
    }

    public void removeMember(User user) {

        members.remove(user);

        if(members.isEmpty()) {

            GameState state = MineWar.getHandler().getState();

            if(state == GameState.PRE_GAME || state == GameState.IN_GAME) {
                Messages.broadcastMessage(Messages.TEAM_NO_MEMBERS_LEFT_MESSAGE.replace("%team", getName()));
                MineWar.getHandler().handleTeamElimination(this);
            }

        }

        user.setTeam(null);

    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public int getSize() {
        return getMembers().size();
    }

}
