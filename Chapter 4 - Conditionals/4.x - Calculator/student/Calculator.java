package student;

import constants.Buttons;
import static internal.EngineAPI.*;

public class Calculator {
    
    /**
     * This method is called when a single button is clicked.
     * You don't get notifications of multiple clicks at once, so you'll have to store data for later usage by using the EngineAPI methods.
     * 
     * @param buttonName The name of the button that was clicked. This should always match one String from the {@link Buttons} class in Buttons.java.
     */
    public static void handleButtonClick(String buttonName) {
        if (wasPreviousButtonEquals()) {
            clearExpression();
            setEntry("");
            setWasPreviousButtonEquals(false);
        }
        switch (buttonName) {
            case Buttons.CLEAR_ALL:
                clearExpression();
                clearPreviousOperationButton();
                clearPreviousResult();
            case Buttons.CLEAR_ENTRY:
                setEntry(0);
                break;

            case Buttons.ONE:
            case Buttons.TWO:
            case Buttons.THREE:
            case Buttons.FOUR:
            case Buttons.FIVE:
            case Buttons.SIX:
            case Buttons.SEVEN:
            case Buttons.EIGHT:
            case Buttons.NINE:
            case Buttons.ZERO:
                if ((getEntryAsString().equals("0") && !entryHasADecimalPoint()) || !isEntryIncomplete()) {
                    setEntry(buttonName);
                }
                else {
                    appendDigitToEntry(buttonName);
                }
                setEntryIncomplete(true);
                setWasPreviousButtonEquals(false);
                break;
            
            case Buttons.DECIMAL_POINT:
                if (!entryHasADecimalPoint()) {
                    appendDecimalPointToEntry();
                }
                break;
            
            case Buttons.PLUS:
            case Buttons.MINUS:
            case Buttons.MULTIPLIED_BY:
            case Buttons.DIVIDED_BY:
                appendToExpression(getEntryAsString() + buttonName);
                setPreviousResult(getEntryAsDouble());
                setPreviousOperationButton(buttonName);
                setEntry("0");
                setEntryIncomplete(false);
                break;
            
            case Buttons.EQUALS:
                appendToExpression(getEntryAsString());
                setWasPreviousButtonEquals(true);
                setEntryIncomplete(false);
                double result = 0;
                switch (getPreviousOperationButton()) {
                    case Buttons.PLUS:
                        result = getPreviousResult() + getEntryAsDouble();
                        break;
                    case Buttons.MINUS:
                        result = getPreviousResult() - getEntryAsDouble();
                        break;
                    case Buttons.MULTIPLIED_BY:
                        result = getPreviousResult() * getEntryAsDouble();
                        break;
                    case Buttons.DIVIDED_BY:
                        result = getEntryAsDouble() / getEntryAsDouble();
                        break;
                    
                    case "":
                        result = getEntryAsDouble();
                        break;
                
                    default:
                        break;
                }
                setEntry(result);
                setPreviousResult(result);
                break;
        
            default:
                break;
        }
    }

}
