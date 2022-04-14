import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DominikMatuszekFunctionsTest {
    @Test
    public void constant_function_has_got_exactly_0_arguments(){
        String result = "żart_o_gościu_na_żółtym_motorku";
        String toAdd = "ten żart jest świetny ale jeśli go zacznę opowiadać w ten sposób to chyba zostanę wyrzucony przez okno";

        Function <String, String> testFunction = Functions.constant(result); //funkcja ze Stringa w Stringa, czemu nie?
        ArrayList<String> args = new ArrayList<>();

        try {
            assertEquals(testFunction.compute(args), result);
            }
        catch(Exception e){
            fail(); //proszę tu nie rzucać żadnych exceptionów
        }

        for(int i=1;i<100;i++){
            args.add(toAdd);
            try{
                testFunction.compute(args);
                fail(); //więcej niż zero argumentów jest verboten
            }
            catch(Exception e){
                //tu ma nic nie być
            }
        }
    }
}
