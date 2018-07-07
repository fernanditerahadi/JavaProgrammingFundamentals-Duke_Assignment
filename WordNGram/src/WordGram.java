
public class WordGram {
    private String[] myWords;
    private int myHash;

    public WordGram(String[] source, int start, int size) {
        myWords = new String[size];
        System.arraycopy(source, start, myWords, 0, size);
    }

    public String wordAt(int index) {
        if (index < 0 || index >= myWords.length) {
            throw new IndexOutOfBoundsException("bad index in wordAt "+index);
        }
        return myWords[index];
    }

    public int length(){
        return myWords.length;
    }

    public int hashCode(){
        return this.toString().hashCode();
    }

    public String toString(){
        String ret = "";
        for(int k=0; k < myWords.length; k++) {
            ret = (ret + myWords[k] + " ");
        }
        return ret.trim();
    }

    public boolean equals(Object o) {
        WordGram other = (WordGram) o;
        if (myWords.length != other.length()){
            return false;
        }
        for (int i = 0; i < myWords.length; i++) {
            if (!myWords[i].equals(other.wordAt(i))){
                return false;
            }
        }
        return true;

    }

    public WordGram shiftAdd(String word) {
        String[] myWordsNew = new String[myWords.length];
        for (int i = 1; i < myWords.length; i++) {
            myWordsNew[i-1] = myWords[i];
        }
        myWordsNew[myWordsNew.length-1] = word;
        return new WordGram(myWordsNew, 0, myWordsNew.length);
    }
}