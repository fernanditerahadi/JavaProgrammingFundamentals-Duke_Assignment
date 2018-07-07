import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import edu.duke.*;


public class VigenereBreaker {

    public String sliceString(String message, int startIndex, int sliceRange) {
        StringBuilder output = new StringBuilder();
        for (int i = startIndex; i < message.length(); i+=sliceRange) {
            output.append(message.charAt(i));
        }
        return output.toString();
    }

    public int[] tryKeyLength(String encrypted, int keyLength, char mostCommon) {
        int[] key = new int[keyLength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        String[] encryptedMessage = new String[keyLength];
        for (int i = 0; i < keyLength; i++) {
            encryptedMessage[i] = sliceString(encrypted, i, keyLength);
            key[i] = cc.getKey(encryptedMessage[i]);
        }
        return key;
    }


    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> dictionary = new HashSet<>();
        for(String word : fr.lines()){
            dictionary.add(word.toLowerCase());
        }
        return dictionary;
    }

    public char mostCommonChar(HashSet<String> dictionary){
        HashMap<Character, Integer> commonChar = new HashMap<>();
        for(String s : dictionary){
            for (int i = 0; i < s.length(); i++) {
                char currChar = s.charAt(i);
                if(!commonChar.containsKey(currChar)){
                    commonChar.put(currChar, 1);
                }
                else{
                    commonChar.put(currChar, commonChar.get(currChar)+1);
                }
            }
        }
        int highestCharIndex = 0;
        char character = 0;
        for (char c: commonChar.keySet()){
            if(commonChar.get(c) > highestCharIndex){
                highestCharIndex = commonChar.get(c);
                character = c;
            }
        }
        return character;
    }

    public int countWords(String message, HashSet<String> dictionary){
        int wordCounts = 0;
        String[] words = message.split("\\W+");
        for(String word : words){
            if(dictionary.contains(word.toLowerCase())){
                wordCounts += 1;
                }
            }
        return wordCounts;
    }

    public ArrayList<Object> breakForAllLanguages(String encrypted, HashMap<String, HashSet<String>> langWordsMap){
        ArrayList<Object> result = new ArrayList<>();
        String decrypted = null;
        String lang = null;
        int wordCounts = 0;
        int[] key = new int[0];
        for(String currLang : langWordsMap.keySet()){
            ArrayList<Object> output = breakForLanguage(encrypted, langWordsMap.get(currLang));
            String currDecrypted = String.valueOf(output.get(0));
            int currWordCounts = (int) output.get(1);
            int[] currKey = (int[]) output.get(2);
            if(currWordCounts > wordCounts){
                wordCounts = currWordCounts;
                decrypted = currDecrypted;
                key = currKey;
                lang = currLang;

            }
        }
        result.add(decrypted);
        result.add(wordCounts);
        result.add(key);
        result.add(lang);
        return result;
    }


    public ArrayList<Object> breakForLanguage(String encrypted, HashSet<String> dictionary){
        ArrayList<Object> output = new ArrayList<>();
        String decrypted = null;
        char commonChar = mostCommonChar(dictionary);
        int wordCounts = 0;
        int[] key = new int[0];
        for (int i = 1; i < 101; i++) {
            int[] currKey = tryKeyLength(encrypted,i,commonChar);
            VigenereCipher vc = new VigenereCipher(currKey);
            String currDecrypted = vc.decrypt(encrypted);
            int currWordCounts = countWords(currDecrypted,dictionary);
            if( currWordCounts > wordCounts){
                wordCounts = currWordCounts;
                decrypted = currDecrypted;
                key = currKey;
            }
        }
        output.add(decrypted);
        output.add(wordCounts);
        output.add(key);
        return output;
    }

    public ArrayList<Object> breakVigenere () {
        System.out.println("Select a message file, i.e messages/secretmessage1.txt: ");
        FileResource fr = new FileResource();
        System.out.println("File selected"+"\n");
        String message = fr.asString();
        System.out.println("Select multiple dictionary files, i.e dictionaries/Spanish, dictionaries/English, dictionaries/Portuguese: ");
        DirectoryResource dr = new DirectoryResource();
        System.out.println("Selected dictionaries: ");
        for (File f: dr.selectedFiles()) {
            String fName = f.getName();
            System.out.print(fName + ", ");
        }
        System.out.println("\n");
        HashMap<String, HashSet<String>> langWordsMap = new HashMap<>();
        int counter = 0;
        for(File f: dr.selectedFiles()){
            String fileName = f.getName();
            System.out.print("Creating "+fileName+" Dictionary ");
            langWordsMap.put(fileName,readDictionary(new FileResource(f)));
            counter += 1;
            if (counter % 3 == 0) {
                System.out.println(" ");
            }
        }
        System.out.println("\n");
        return breakForAllLanguages(message, langWordsMap);
    }
}

