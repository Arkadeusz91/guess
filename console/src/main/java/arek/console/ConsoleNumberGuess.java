package arek.console;

import arek.Game;
import arek.MessageGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@Component
public class ConsoleNumberGuess{

    // -- Fields --
    private final Game game;
    private final MessageGenerator messageGenerator;

    // -- Constructor --
    @Autowired
    public ConsoleNumberGuess(Game game, MessageGenerator messageGenerator) {
        this.game = game;
        this.messageGenerator = messageGenerator;
    }

    // -- Events --
    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        log.info("start() --> container ready for use");

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println(messageGenerator.getMainMessage());
            System.out.println(messageGenerator.getResultMessage());

            int guess = scanner.nextInt();
            scanner.nextLine();
            game.setGuess(guess);
            game.check();

            if (game.isGameWon() || game.isGameLost()){
                System.out.println(messageGenerator.getResultMessage());
                System.out.println("Do you want to play again y/n");

                String playAgain = scanner.next().trim();
                if (!playAgain.equalsIgnoreCase("y")){
                    break;
                }

                game.reset();
            }
        }
    }
}
