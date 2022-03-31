package contactextraction_phase1;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MCB110000075
 */


public class CoreNLP {
 //For CoreNLP
    public static StanfordCoreNLP pipeline;
    public static Properties props;

    public static void main(String[] args) throws IOException {
        
            System.out.println("Loading CoreNLP ....");
                        //      model = new POSModelLoader().load(new File(MethodRepository.opennlpLocation));
            props = new Properties();
//            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
            props.put("annotators", "tokenize, ssplit, pos, lemma, ner");

                pipeline = new StanfordCoreNLP(props);
//                         String Input="Microsoft was owned by Bill Gates.";
//                        String output="";
//                        
//                        
//                        String POS =CoreNLP_ExtractNames(Input);
//        System.out.println("POS "+POS);
String fileName = "Input.txt";
File file = new File("OutputPOS.txt");
      
      // creates the file
      file.createNewFile();
      
      // creates a FileWriter Object
      FileWriter writer = new FileWriter(file); 
      
File fileread = new File(fileName);
FileReader fr = new FileReader(fileread);
BufferedReader br = new BufferedReader(fr);
String line;
while((line = br.readLine()) != null){
    //process the line
    System.out.println(line);
    String POS =CoreNLP.CoreNLP_ExtractNames(line);
    writer.write(line+"\t"+POS+"\n");
//    insert_query(line,connection);
}
      writer.flush();
      writer.close();


br.close();
        
        

    }
    public static boolean CoreNLP_IsName(String Name) throws IOException, InterruptedException {
        boolean deciderFlag = false;
        int truecount = 0;
        int falsecount = 0;
        //    Name=Name.replaceAll("(?sm)[A-Z](\\.)*(\\s|\\s*$)", "").trim();
        //  Name=Name.replaceAll(",", "").trim();
        String final_Name = "";
        String final_word = "";
        String final_pos = "";
        String final_lemma = "", final_NER = "";
        String classifiedString = "";

        {
        edu.stanford.nlp.pipeline.Annotation document = pipeline.process(Name);
        List<CoreLabel> tokens=document.get(CoreAnnotations.TokensAnnotation.class);
     for (CoreLabel token: tokens) 
      {
//    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
    String word = token.get(CoreAnnotations.TextAnnotation.class);
//    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
    String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//
//          System.out.println(" Word  - "+word);
//          System.out.println(" Lemma  - "+lemma);
//          System.out.println(" POS  - "+pos);
//          System.out.println(" NER  - "+ner);
//            }
                if (ner.contains("PERSON")) {
                    final_word += word + " ";
                    truecount++;
                    // deciderFlag=true;
                    // final_lemma+=lemma+"|";
                    // final_pos+=pos+"|";
                    //final_NER+=ner+"|";
                } else {
                    falsecount++;
                    // deciderFlag=false;
                }

            }
            if (truecount >= falsecount) {
                return true;
            } else {
                return false;
            }
            // return deciderFlag;

        }
    }
public static String CoreNLP_ExtractNames(String Name)
    {
        String tempConsolidatedScore="";
       // Name=Name.replaceAll("(?sm)[A-Z](\\.)*(\\s|\\s*$)", "").trim();
        //Name=Name.replaceAll(",", "").trim();
        int truecount=0;
        int falsecount=0;
        String final_Name="";
       
        String final_pos="";
        String final_lemma="",final_NER="";
        String classifiedString=""; 
         String final_word="";
        edu.stanford.nlp.pipeline.Annotation document = pipeline.process(Name);
        List<CoreLabel> tokens=document.get(CoreAnnotations.TokensAnnotation.class);
     for (CoreLabel token: tokens) 
      {
//    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
    String word = token.get(CoreAnnotations.TextAnnotation.class);
//    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
    String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
    
//          System.out.println(" Word  - "+word);
//          System.out.println(" Lemma  - "+lemma);
//          System.out.println(" POS  - "+pos);
//          System.out.println(" NER  - "+ner);
   
     if(ner.contains("PERSON"))
        {
            final_word+=word+" ";
            truecount++;
        }else{
         falsecount++;
    }
      if(truecount>=falsecount){
         return final_word.replaceAll("(?sim\\s+)"," ").trim();
     }
    }
//     final_pos=final_pos.replaceAll("(?sim)^\\s*\\|\\s*", "");
//     final_pos=final_pos.replaceAll("(?sim)\\s*\\|\\s*$", "");
//   return final_pos.trim();
          return "";
}
}
