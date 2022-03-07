import org.junit.Test;
import static org.junit.Assert.*;

public class KacperTopolskiPhoneBookTest
{
    @Test(timeout = 200)
    public void changing_format_should_be_fast() {
        int el = 2500, changes = 10000;
        PhoneBook pb = new PhoneBook(el);

        for (int i = 0; i < el; ++i)
            pb.add("" + (i + 123456789));

        for (int i = 0; i < changes; ++i) {
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            pb.changeFormat(PhoneBook.NumberFormat.DIGITS);
        }
    }
}
