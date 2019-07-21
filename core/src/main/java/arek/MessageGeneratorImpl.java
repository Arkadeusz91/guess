package arek;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MessageGeneratorImpl implements MessageGenerator {

    public static final String MAIN_MESSAGE = "game.main.message";
    public static final String WIN = "game.win";
    public static final String LOST = "game.lost";
    public static final String INVALID_RANGE = "game.invalidRange";
    public static final String FIRST_GUESS = "game.firstGuess";
    public static final String LOWER = "game.lower";
    public static final String HIGHER = "game.higher";
    public static final String RESULT = "game.result.message";

    // -- Fields --
    private final Game game;
    private final MessageSource messageSource;

    // -- Constructor --
    @Autowired
    public MessageGeneratorImpl(Game game,MessageSource messageSource) {
        this.game = game;
        this.messageSource = messageSource;
    }

    // -- Init --
    @PostConstruct
    public void postConstruct(){
        log.debug("{}",game);
    }

    // -- Public Methods --
    @Override
    public String getMainMessage() {
        return getMessage(MAIN_MESSAGE,game.getSmallest(),game.getBiggest());
    }

    @Override
    public String getResultMessage() {
        if (game.isGameWon()){
            return getMessage(WIN,game.getNumber());
        } else if (game.isGameLost()){
            return getMessage(LOST,game.getNumber());
        } else if (!game.isValidNumberRange()){
            return getMessage(INVALID_RANGE,game.getSmallest(),game.getBiggest());
        } else if (game.getRemainingGuesses() == game.getGuessCount()){
            return getMessage(FIRST_GUESS);
        }else {
            String direction = getMessage(LOWER);

            if (game.getNumber() > game.getGuess()){
                direction = getMessage(HIGHER);
            }

            return getMessage(RESULT,direction,game.getRemainingGuesses());
        }
    }

    private String getMessage(String code,Object... args){
        return messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
    }

}
