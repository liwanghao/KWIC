package kwic.es;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class WordIndexer implements Observer {

    private HashMap<String, Integer> wordIndex_;

    public WordIndexer(HashMap<String, Integer> wordIndex) {
        wordIndex_ = wordIndex;
    }

    @Override
    public void update(Observable observable, Object arg) {

        // cast to the event object
    LineStorageChangeEvent event = (LineStorageChangeEvent) arg;

    String line = event.getArg();
    String[] words = line.split(" ");

    for(int i=0;i<words.length;i++){
        Integer count = wordIndex_.get(words[i]);
        switch(event.getType()){
            case LineStorageChangeEvent.ADD:
                if(count==null){
                    wordIndex_.put(words[i], 1);
                }else{
                    wordIndex_.put(words[i], count+1);
                }
                break;
            case LineStorageChangeEvent.DELETE:
                if(count.intValue()==1){
                    wordIndex_.remove(words[i]);
                }else{
                    wordIndex_.put(words[i], count-1);
                }
        }

    }

    }

}
