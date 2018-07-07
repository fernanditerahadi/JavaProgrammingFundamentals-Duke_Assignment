import edu.duke.*;

import java.util.Scanner;

public class MarkovRunner {
    public void runModel(IMarkovModel markov, String text, int size){ 
        markov.setTraining(text); 
        System.out.println("\nRandom text generated with " + markov);
        String st = markov.getRandomText(size);
        printOut(st);
    }

    public int[] userInputDialog() {
        int[] output = new int[2];
        Scanner sc = new Scanner(System.in);
        System.out.print("Please input number of Order (N), i.e. '5': ");
        output[0] = sc.nextInt();
        sc.nextLine();
        System.out.print("Please input number of words to be printed out, i.e. '100': " );
        output[1] = sc.nextInt();
        sc.nextLine();
        sc.close();
        return output;
    }

    public void runMarkov() {
        System.out.println("***Markov Text Generator***\n");
        System.out.println("Please select a *.txt file from the data folder, i.e. data/alice.txt.\n");
        FileResource fr = new FileResource(); 
        String st = fr.asString(); 
        st = st.replace('\n', ' ');
        int[] input = userInputDialog();
        MarkovWord markovWord = new MarkovWord(input[0]);
        runModel(markovWord, st, input[1]);
    }

    private void printOut(String s){
        String[] words = s.split("\\s+");
        int psize = 0;
        System.out.println("----------------------------------------------------");
        for(int k=0; k < words.length; k++){
            System.out.print(words[k]+ " ");
            psize += words[k].length() + 1;
            if (psize > 60) {
                System.out.println(); 
                psize = 0;
            } 
        } 
        System.out.println("\n--------------------------------------------------");
    }

}
