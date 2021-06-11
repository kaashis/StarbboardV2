package com.starboard;

import com.starboard.items.*;
import com.starboard.util.CommandMatch;
import com.starboard.util.ConsoleColors;
import com.starboard.util.Prompt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private static Room currentRoom;
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Prompt.showWelcome();
        int alienNumber = chooseLevel();

        boolean endGame = false;
        // create weapons list
        List<Weapon> weaponsList = CreateItems.createWeapons();

        // create healing items list
        List<HealingItem> healingItemsList = CreateItems.createHealingItems();

        // combine the lists to a map
        Map<String, GameItem> items = new HashMap<>();
        for (Weapon weapon : weaponsList) {
            items.put(weapon.getName(), weapon);
        }

        for (HealingItem healingItem : healingItemsList) {
            items.put(healingItem.getName(), healingItem);
        }

        // Initialization: create a list of Room objects
        List<Room> roomsList = CreateRooms.create();
        // add item to each container contents
        for (Room room : roomsList) {
            for (Container container : room.getContainers().values()) {
                container.addItem(items.get(container.getName()));
            }
        }

        // Initialize start room
        currentRoom = roomsList.get(0);

        Prompt.showMap();

        // show commands
        Prompt.showCommands();

        //Training mode
        if (alienNumber == 0){

            training();
        }
        Prompt.showIntroduction();

        //initialize player
        Player player = new Player();
        Alien aliens = new Alien(100, alienNumber);

        while (!endGame) {

            aliens.setRoom(currentRoom);
            aliens.setExisted(false);
            aliens.setShowUpChance();
            aliens.showUp();
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);

            if (aliens.isExisted()){
                Battle battle = new Battle(aliens, player, currentRoom);
                battle.fight();
                if(battle.isWinning()){
                    System.out.println("Keep moving!");
                    Prompt.showStatus(currentRoom);
                    Prompt.showInventory(player);
                }else break;
            }

            String[] parsedInputs = InputHandler.input(currentRoom);

            CommandMatch.matchCommand(parsedInputs,player);

            if (currentRoom.getName().equals("pod")) {
                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
                System.out.println("Congratulations! You successfully escape from the ship!");
                ConsoleColors.reset();
                endGame = true;
            }
        }
    }

    private static void training() {
        System.out.println("Entering training mode");

    }

    public static int chooseLevel(){
        System.out.println("Please choose from following levels\n" +
                "0. \t\t\t\tTraining\n"+
                "1. \t\t\t\tEasy\n"+
                "2. \t\t\t\tIntermediate\n"+
                "3. \t\t\t\tHard\n"+
                "4. \t\t\t\tHell");
        System.out.print("Enter the number\n>");
        Scanner sc = new Scanner(System.in);

        int choice = sc.nextInt();
        return choice * 2;


    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        Game.currentRoom = currentRoom;
    }
}