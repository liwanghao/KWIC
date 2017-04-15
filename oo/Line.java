package kwic.oo;

import java.util.ArrayList;

/**
 * @author Wanghao
 *
 */
public class Line {
    private ArrayList<String> words_ = new ArrayList<>();

    /**
     * @param c new character
     * @param position character index in the word
     * @param word word index in the line
     * @return void
     * @see #getChar
     * @see #addChar
     * @see #deleteChar
     */
    public void setChar(char c, int position, int word) {
        // get the specified word
        String current_word = words_.get(word);
        // Get character representation of the specified word
        char[] chars = current_word.toCharArray();
        // set the new character at the specified positon
        chars[position] = c;
        // make new string represenation of the word
        current_word = new String(chars);
        // replace the old word with the new one
        words_.set(word, current_word);
    }

    /**
     * @param position character index in the word
     * @param word word index in the line
     * @return char
     * @see #setChar
     * @see #addChar
     * @see #deleteChar
     */
    public char getChar(int position, int word) {
        return words_.get(word).charAt(position);
    }

    /**
     * @param c new character
     * @param word word index in the line
     * @return void
     * @see #setChar
     * @see #getChar
     * @see #deleteChar
     */
    public void addChar(char c, int word) {

        // get the specified word
        String current_word = words_.get(word);

        // create a new character array with the length of
        // the old word increased by 1
        char[] chars = new char[current_word.length() + 1];

        // copy the old word into the new character array
        current_word.getChars(0, chars.length - 1, chars, 0);

        // add the new character at the end of the word
        chars[chars.length - 1] = c;

        // make new string represenation of the word
        current_word = new String(chars);

        // replace the old word with the new one
        words_.set(word, current_word);
    }

    /**
     * @param position character index in the word
     * @param word word index in the line
     * @return void
     * @see #setChar
     * @see #getChar
     * @see #addChar
     */
    public void deleteChar(int position, int word) {

        // get the specified word
        String current_word = words_.get(word);

        // create a new character array with the length of
        // the old word decreased by 1
        char[] chars = new char[current_word.length() - 1];

        // copy the old word into the new character array
        // skip the character that should be deleted
        current_word.getChars(0, position, chars, 0);
        current_word.getChars(position + 1, chars.length + 1, chars, position);

        // make new string represenation of the word
        current_word = new String(chars);

        // replace the old word with the new one
        words_.set(word, current_word);
    }

    /**
     * @param word word index in the line
     * @return int
     */
    public int getCharCount(int word) {
        return words_.get(word).length();
    }

    /**
     * @param chars new word
     * @param word word index in the line
     */
    public void setWord(char[] chars, int word) {
        setWord(new String(chars), word);
    }

    /**
     * @param chars new word
     * @param word word index in the line
     */
    public void setWord(String chars, int word) {
        words_.set(word, chars);
    }

    /**
     * @param word index in the line
     * @return String
     */
    public String getWord(int word) {
        return words_.get(word);
    }

    /**
     * @param chars new word
     * @return void
     */
    public void addWord(char[] chars) {
        addWord(new String(chars));
    }

    /**
     * @param chars new word
     * @return void
     */
    public void addWord(String chars) {
        words_.add(chars);
    }

    /**
     *
     */
    public void addEmptyWord() {
        words_.add(new String());
    }

    /**
     * @param word word index in the line
     */
    public void deleteWord(int word) {
        words_.remove(word);
    }

    /**
     * @return int
     */
    public int getWordCount() {
        return words_.size();
    }
}
