package balt.sloboda.portal.utils;

import com.ibm.icu.text.Transliterator;

public class Transcriptor {

    private static final String id = "Any-Latin; NFD; [^\\p{Alnum}] Remove; [\\u02B9 \\u02BA] remove";

    public static String transliterate(String cyrillicString) {
        Transliterator toLatinTrans = Transliterator.getInstance(id);
        return toLatinTrans.transliterate(cyrillicString);
    }
}
