package contactextraction_phase1;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
//import opennlp.tools.util.InputStreamFactory;
//import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.apache.commons.lang3.StringUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MCB110000075
 */
public class OpenNLP {
    public static POSModel model = null;
    public static PerformanceMonitor perfMon;
    public static POSTaggerME tagger;
  public static InputStream modelStream; 
    public static void main(String[] args) throws IOException {
        String InputforSentencedetector="As per Wikipedia, POS tagging is \"the process of marking up a word in a text (corpus) as corresponding to a particular part of speech, based on both its definition and its context — i.e. its relationship with adjacent and related words in a phrase, sentence, or paragraph. A simplified form of this is commonly taught to school-age children, in the identification of words as nouns, verbs, adjectives, adverbs, etc.\"\n" +
"\n" +
"To begin, any part of speech is tokenized — it is divided into tokens and then these tokens are tagged as per grammar rules by NLP for further processing. Tagging is the basic pre-processing of any POS for text retrieval and text indexing. You can see an Apache Open NLP POS tokenization example here.\n" +
"\n" +
"To get started with OpenNLP tagging, first we include following dependencies in the pom.xml file.";
        detectSentences(InputforSentencedetector);

       
        String Input="Microsoft was owned by Bill Gates.";
        try {
            POSTag(Input);
        } catch (IOException ex) {
            Logger.getLogger(OpenNLP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
 
    
    public static void POSTag(String input) throws IOException {
    System.out.println("Loading POS Models...");
         model = (POSModel) new POSModelLoader().load(new File("bin_classifiers\\binFiles\\en-pos-maxent.bin"));
        perfMon = new PerformanceMonitor(System.err, "sent");
        tagger = new POSTaggerME(model);
     
        
//        InputStream modelIn = new FileInputStream("bin_classifiers\\binFiles\\en-pos-maxent.bin");
//	POSModel model = new POSModel(modelIn);
//        POSTaggerME tagger = new POSTaggerME(model);
//        String sentance="Pierre Vinken , 61 years old , will join the board as a nonexecutive director Nov. 29 .";
//		String sent[] =sentance.split("\\s"); 	  
//String tags[] = tagger.tag(sent);
//int i=0;
//	for(String tag:tags){
//		System.out.println(sent[i]+" __tagged__ "+tag);
//		i++;
//	}
//
//
//        
//        Charset charset = Charset.forName("UTF-8");
//InputStreamFactory isf = new MarkableFileInputStreamFactory(new File("myText.txt"));
//ObjectStream<String> lineStream = new PlainTextByLineStream(isf, charset);
//        
//        
////        ObjectStream<String> lineStream;
////        lineStream = new PlainTextByLineStream(
////                new StringReader(input));
        perfMon.start();
//        String line;
        String output = "";
//        while ((line = lineStream.read()) != null) {
        if ((input != null) ||(!input.isEmpty()) ) {
            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(input);
            String[] tags = tagger.tag(whitespaceTokenizerLine);
            
            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            output = sample.toString();
            System.out.println("POS Tagged :  "+output);
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        if ((StringUtils.containsIgnoreCase(output, "_VB")) || (StringUtils.containsIgnoreCase(output, "_IN")) || (StringUtils.containsIgnoreCase(output, "_DT"))) {
//         return false;
       }
    }
    
    
 //Semtence detector   
       public static void detectSentences(String paragraph) throws IOException {
 System.out.println("Loading Sentence Detector Models...");
//         InputStream modelIn  = (POSModel) new POSModelLoader().load(new File("bin_classifiers\\binFiles\\en-sent.bin"));
//        perfMon = new PerformanceMonitor(System.err, "sent");
//        tagger = new POSTaggerME(model);

InputStream modelIn = new FileInputStream("bin_classifiers\\binFiles\\en-sent.bin");
SentenceModel smodel=null;

try {
  smodel = new SentenceModel(modelIn);
}
catch (IOException e) {
  e.printStackTrace();
}
finally {
  if (modelIn != null) {
    try {
      modelIn.close();
    }
    catch (IOException e) {
    }
  }
}

SentenceDetectorME sentenceDetector = new SentenceDetectorME(smodel);
String sentences[] = sentenceDetector.sentDetect(paragraph);
int i=0;
for(String sentence:sentences){
    i++;
    System.out.println("Sentence No "+i +" -----  Sentence : "+sentence);
}

//SentenceModel sentenceModel;
//        try (InputStream modelIn = OpenNLP.class.getName().getClass().getResourceAsStream("bin_classifiers\\binFiles\\en-sent.bin")) {
//            sentenceModel = new SentenceModel(modelIn);
//        }
//
//        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
//        String sentences[] = sentenceDetector.sentDetect(paragraph);
//        for (String sent : sentences) {
//            System.out.println(sent);
//        }
////        return sentences;
//    }
}
}
    

