import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

abstract class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void act();
}

interface Evidence {
    void inspect();
}

interface GameItem {
    String getDescription();
    boolean isClue();
}

interface DetectiveActions {
    void investigate();
    void makeDeduction();
}

class Detective extends Player implements DetectiveActions {
    private int cluesFound;
    private static final int maxClues = 2;

    public Detective(String name) {
        super(name);
        this.cluesFound = 0;
    }

    public void act() {
        System.out.println("\n" + getName() + " is on the case..");
    }

    public void investigate() {
        System.out.println(getName() + " is investigating the crime scene for clues.");
    }

    public void makeDeduction() {
        System.out.println("The clue is two words.");
        System.out.print("Where do you think the killer is headed next?");
        
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine().toLowerCase();

        if (answer.equalsIgnoreCase("Art Gallery")) {
            System.out.println("\nYou solved it! The killer was apprehended at the Art Gallery.");
            System.out.println("The killer is William Cruz, a 26 years old painter who infused ");
            System.out.println("blood intohis artwork. Completing his art, he planned to showcase");
            System.out.println(" his peculiar work to every visitor in the Art Gallery.");
            
        } else {
            System.out.println("\nThe killer evaded capture. He's at the Art Gallery.");
            System.out.println("In a dimly lit gallery, the art enthusiast was unaware that");
            System.out.println("the very bloodof the victims of the killer is infused in ");
            System.out.println("every piece around them. William watched his art,his face ");
            System.out.println("twisted with satisfaction— his dark secret hidden in plain sight,");
            System.out.println("waiting for them to feel the chill of his twisted vision.");
        }
    }

    public void findClue() {
        cluesFound++;
        System.out.println("Clue found! Total clues: " + cluesFound);
         
    }

    public int getCluesFound() {
        return cluesFound;
    }
}

class Item implements Evidence, GameItem {
    private String name;
    private String description;
    private boolean clue;

    public Item(String name, String description, boolean clue) {
        this.name = name;
        this.description = description;
        this.clue = clue;
    }

    public void inspect() {
        System.out.println("Inspecting " + name + ": " + description);
    }

    public String getDescription() {
        return description;
    }
    
    public String getName(){
        return name;
    }

    public boolean isClue() {
        return clue;
    }
}

public class DetectiveGame {

    public static Detective detective;
    public static List<Item> storyModeItems;
    public static Random random = new Random();

    public static void playStoryMode() {
        detective.act();
        setupStoryModeItems();
        int cluesFound = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nStory Mode: Find 2 clues to identify the killer's next location.");

        while (cluesFound < 2) {
            System.out.println("Items in the room:");
            for (int i = 0; i < storyModeItems.size(); i++) {
                System.out.println((i + 1) + ". " + storyModeItems.get(i).getName());
            }
            System.out.print("Choose an item to inspect (1-" + storyModeItems.size() + "): ");
            int choice = scanner.nextInt() - 1;

            if (choice >= 0 && choice < storyModeItems.size()) {
                Item item = storyModeItems.get(choice);
                item.inspect();

                if (item.isClue()) {
                    detective.findClue();
                    storyModeItems.remove(cluesFound);
                    cluesFound++;
                    
                } else {
                    System.out.println("This item doesn’t seem to provide any useful information.");
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }

        System.out.println("You’ve gathered enough clues. Make your judgement.");
        detective.makeDeduction();
    }

    public static void playSurvivalMode() {
        detective.act();
        int moves = 5;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nSurvival Mode: Find as many clues as you can in " + moves + " moves.");
        System.out.println("\nThe suspect left a note.");
        System.out.println("Hello Detective, I know you're enraged to find me.");
        System.out.println("I'm not running away. The truth is I left the clue where");
        System.out.println("I would go. Good luck!");

        while (moves > 0) {
            Item item = generateRandomItem();
            System.out.println("\nItem found: " + item.getDescription());
            System.out.print("Do you want to inspect this item? (yes/no): ");
            String choice = scanner.next();

            if (choice.equalsIgnoreCase("yes")) {
                item.inspect();
                if (item.isClue() ) {
                    detective.findClue();
                    
                } else {
                    System.out.println("This was a red herring!");
                }
            } else {
                System.out.println("You chose not to inspect.");
            }

            moves--;
            System.out.println("Moves remaining: " + moves);
            System.out.println();
        }

        System.out.println("Survival mode over! Total clues found: " + detective.getCluesFound());
        if (detective.getCluesFound() >= 3){
         System.out.println("The detective finally figured it out. The clues all pointed \nto this place.There, sitting on a bench, was the \nkiller. Bloodstains covered his clothes, like he hadn’t even tried to hide it.");
        System.out.println("The detective couldn’t believe it. It was almost too easy.");
        System.out.println("The killer looked up at him, his face completely calm.\n \"Good job, Detective,\" he said, almost like he was impressed. \nThen, with a slow, deliberate motion, he knelt down and \nraised his arms in surrender.");
        System.out.println("But there was something off about it. The killer wasn’t scared. He was just… done.\n Like he’d been waiting for this moment all along.");
        }else {
        System.out.println("Unfortunately, the detective couldn't find enough clues to solve the case.");
        }
    }

    private static void setupStoryModeItems() {
        storyModeItems = new ArrayList<>();
        storyModeItems.add(new Item("Diary", "It mentions 'ARTist'.", true));
        storyModeItems.add(new Item("Bucket", "full of red paint, possible clue", false));
        storyModeItems.add(new Item("Flyers", "It reads 'Art Gallery'.", true));
        storyModeItems.add(new Item("Scrap of Cloth", "A random piece of fabric, maybe irrelevant.", false));
        storyModeItems.add(new Item("Matchbook", "From a random hotel, seems unimportant.", false));
    }

    private static Item generateRandomItem() {
        boolean isClue = random.nextBoolean();
        if (isClue) {
            return new Item("Random Clue", "This item looks important!", true);
        } else {
            return new Item("Red Herring", "This item seems irrelevant.", false);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String player = scanner.nextLine();

        detective = new Detective(player);
        
        while (true){
        System.out.println("\nChoose a game mode:");
        System.out.println("1. Story Mode");
        System.out.println("2. Survival Mode");
        System.out.print("Enter choice: ");
        int modeChoice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Press P to start playing, " + player + ".");
        String pButton = scanner.nextLine().toUpperCase();
        
            if (modeChoice == 1) {
            playStoryMode();
        } else if (modeChoice == 2) {
            playSurvivalMode();
        } else {
            System.out.println("Invalid choice. Exiting game.");
        }
        
        System.out.print("\nDo you want to play again? (Y/N): ");
        String playAgain = scanner.nextLine().toUpperCase();
        
        if (!playAgain.equals("Y")) {
            System.out.println("Thanks for playing! Goodbye!");
        }
        }
    }
}
