/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactextraction_phase1;

/**
 *
 * @author SasiRSK
 */
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
public class JazzySpellChecker implements SpellCheckListener {

    static Logger log = Logger.getLogger(JazzySpellChecker.class.getName());
    public static String dictFile = "";
    public static String phonetFile = "";
    private SpellChecker spellCheck = null;
    private static int checkSpelling = 0;

    public JazzySpellChecker(String Word) {
        try {
            com.swabunga.spell.engine.SpellDictionary dictionary = new SpellDictionaryHashMap(new File(dictFile), new File(phonetFile));
            this.spellCheck = new SpellChecker(dictionary);
            checkSpelling = this.spellCheck.checkSpelling(new StringWordTokenizer(Word));
        } catch (Exception e) {
            //e.printStackTrace();
            log.error(" - Exception : " + e.getMessage());
        }
    }

    public void spellingError(SpellCheckEvent event) {
        List suggestions = event.getSuggestions();
        Iterator suggestedWord;
        if (suggestions.size() > 0) {
            //System.out.println("MISSPELT WORD: " + event.getInvalidWord());
            log.info("MISSPELT WORD: " + event.getInvalidWord());
            for (suggestedWord = suggestions.iterator(); suggestedWord.hasNext();) {
                //System.out.println("\tSuggested Word: " + suggestedWord.next());
                log.info("Suggested Word: " + suggestedWord.next());
            }
        } else {
//            System.out.println("MISSPELT WORD: " + event.getInvalidWord());
//            System.out.println("\tNo suggestions");
            log.info("MISSPELT WORD: " + event.getInvalidWord());
            //  System.out.println("\tNo suggestions");
        }
    }

    public static boolean CheckSpelling(String Word) {
        try {
            Word = WordUtils.capitalize(Word.toLowerCase());
            new JazzySpellChecker(Word);
            if (checkSpelling == -1) {
                return true;
            }
            if (checkSpelling == 1) {
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error(" - " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
//        System.out.println("\nLoading Jazzy Spell Checker dependencies...");
        dictFile = "Dependencies/english.0";
        phonetFile = "Dependencies/phonet.en";
        boolean isSpelled = CheckSpelling("Financial");

//        System.out.println(isSpelled);
    }
}


