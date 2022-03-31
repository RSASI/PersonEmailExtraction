/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactextraction_phase1;


import static contactextraction_phase1.ContactExtraction_Phase1.EmailRegex;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author SASIKUMAR
 */
public class MailExtraction {

//    static void ExtractMail(String Output_AddressBlock, String pagesource) {
//        Iterator cntitr = EmailRegex.iterator();
//        while (cntitr.hasNext()) {
//            String rgx = cntitr.next().toString();
//            try {
//                Pattern regex = Pattern.compile(rgx,
//                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
//                Matcher regexMatcher = regex.matcher(Output_AddressBlock);
//                if (regexMatcher.find()) {
//                    // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
//                    String relphone = regexMatcher.group(1);
//                    if (relphone==null){
//                        relphone="";
//                    }
//                    relphone = relphone.replaceAll("(?sim)<[^<>]*>", " ").replaceAll("\\s+", " ").trim();
//                    Gate_Main_NewApproach.RelevantMail.add(relphone);
//                    Gate_Main_NewApproach.AllMail.add(relphone);
//
//                }
//            } catch (PatternSyntaxException ex) {
//	// Syntax error in the regular expression
//            }
//        }
//
//        Iterator allent = EmailRegex.iterator();
//        while (allent.hasNext()) {
//            String rgx = allent.next().toString();
//            try {
//                Pattern regex = Pattern.compile(rgx,
//                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
//                Matcher regexMatcher = regex.matcher(pagesource);
//                while (regexMatcher.find()) {
//                    // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
//                    String relphone = regexMatcher.group(1);
//                    if (relphone==null){
//                        relphone="";
//                    }
//                    relphone = relphone.replaceAll("(?sim)<[^<>]*>", " ").replaceAll("\\s+", " ").trim();
////            Gate_Main_NewApproach.RelevantPhone.add(relphone);
//                    Gate_Main_NewApproach.AllMail.add(relphone);
//
//                }
//            } catch (PatternSyntaxException ex) {
//	// Syntax error in the regular expression
//                }
//        }
//    }




static void ExtractMail_Person(String Output_AddressBlock) {
        Iterator cntitr = EmailRegex.iterator();
        while (cntitr.hasNext()) {
            String rgx = cntitr.next().toString();
            try {
                Pattern regex = Pattern.compile(rgx,
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(Output_AddressBlock);
                while (regexMatcher.find()) {
                    // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                    String relphone = regexMatcher.group(1);
                    if (relphone==null){
                        relphone="";
                    }
                    relphone = relphone.replaceAll("(?sim)<[^<>]*>", " ").replaceAll("\\s+", " ").trim();
//                    Gate_Main_NewApproach.RelevantMail.add(relphone);
//                    Gate_Main_NewApproach.AllMail.add(relphone);
//                    return relphone.trim();
                    ContactExtraction_Phase1.EmailExtracted.add(relphone);

                }
            } catch (PatternSyntaxException ex) {
	// Syntax error in the regular expression
            }
        }
       
}


}

