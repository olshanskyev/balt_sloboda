package balt.sloboda.portal.utils;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void transcriptorTest() {
        Assert.assertEquals("abvgdeezzijklmnoprstufhccssyeua", Transcriptor.transliterate("а б в г д е ё ж з и й к л м н о п р с т у ф х ц ч ш щ ъ ы ь э ю я"));
    }
}
